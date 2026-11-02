// assets/js/notes.js

const API_NOTES = '/monapp/api/notes';
const API_ETUDIANTS = '/monapp/api/etudiants';
const API_COURS = '/monapp/api/cours';

let idASupprimer = null;
let idAModifier = null;
let etudiants = [];
let coursList = [];

// ============================================================
// Charger la liste des étudiants (pour les selects)
// ============================================================
async function chargerEtudiants() {
    try {
        const resp = await fetch(API_ETUDIANTS);
        const data = await resp.json();
        if (data.success) etudiants = data.data || [];
    } catch (err) {
        console.error('Erreur chargement étudiants', err);
    }
}

// ============================================================
// Charger la liste des cours (pour les selects)
// ============================================================
async function chargerCoursList() {
    try {
        const resp = await fetch(API_COURS);
        const data = await resp.json();
        if (data.success) coursList = data.data || [];
    } catch (err) {
        console.error('Erreur chargement cours', err);
    }
}

// ============================================================
// Remplir un <select> avec la liste
// ============================================================
function remplirSelect(selectId, items, valueField, labelFn, selected = null) {
    const select = document.getElementById(selectId);
    const placeholder = select.querySelector('option[value=""]');
    select.innerHTML = '';
    if (placeholder) select.appendChild(placeholder);

    items.forEach(item => {
        const option = document.createElement('option');
        option.value = item[valueField];
        option.textContent = labelFn(item);
        if (selected !== null && item[valueField] == selected) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

// ============================================================
// Charger et afficher les notes
// ============================================================
async function chargerNotes(query = '') {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '<tr><td colspan="8" class="text-center">Chargement...</td></tr>';

    try {
        const url = query ? `${API_NOTES}?q=${encodeURIComponent(query)}` : API_NOTES;
        const resp = await fetch(url);
        const data = await resp.json();

        if (!data.success) {
            tbody.innerHTML = `<tr><td colspan="8" class="text-danger">${data.message}</td></tr>`;
            return;
        }

        if (!data.data || data.data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted">Aucune note trouvée</td></tr>';
            document.getElementById('paginationInfo').textContent = '0 note(s)';
            return;
        }

        tbody.innerHTML = data.data.map(n => {
            const ponderee = (n.note * n.coefficient).toFixed(2);
            const badge = n.note >= 10 ? 'bg-success' : 'bg-danger';
            return `
                <tr>
                    <td>${n.id}</td>
                    <td>${escapeHtml(n.etudiantNom)}</td>
                    <td>
                        <span class="badge bg-info text-dark">${escapeHtml(n.coursCode)}</span>
                        ${escapeHtml(n.coursIntitule)}
                    </td>
                    <td class="text-center">
                        <span class="badge ${badge}">${n.note.toFixed(2)}</span>
                    </td>
                    <td class="text-center">${n.coefficient}</td>
                    <td class="text-center"><strong>${ponderee}</strong></td>
                    <td>${n.dateNote || '-'}</td>
                    <td class="text-center">
                        <div class="d-flex justify-content-center gap-1">
                            <button class="btn btn-sm btn-primary" onclick="ouvrirModification(${n.id})">
                                <i class="bi bi-pencil"></i>
                            </button>
                            <button class="btn btn-sm btn-danger" 
                                    onclick="ouvrirSuppression(${n.id}, '${escapeJs(n.etudiantNom)}')">
                                <i class="bi bi-trash"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `;
        }).join('');

        document.getElementById('paginationInfo').textContent = `${data.data.length} note(s)`;

    } catch (err) {
        console.error(err);
        tbody.innerHTML = `<tr><td colspan="8" class="text-danger">Erreur réseau</td></tr>`;
    }
}

// ============================================================
// Recherche
// ============================================================
let timerRecherche = null;
document.getElementById('searchInput').addEventListener('input', (e) => {
    clearTimeout(timerRecherche);
    timerRecherche = setTimeout(() => chargerNotes(e.target.value.trim()), 300);
});

// ============================================================
// AJOUT
// ============================================================
document.getElementById('btnAjouter').addEventListener('click', () => {
    // Remplir les selects
    remplirSelect('addEtudiantId', etudiants, 'id', e => `${e.nom} (${e.email})`);
    remplirSelect('addCoursId', coursList, 'id', c => `${c.code} - ${c.intitule}`);
    
    document.getElementById('addNote').value = '10';
    document.getElementById('addDate').value = new Date().toISOString().split('T')[0];
    new bootstrap.Modal(document.getElementById('addModal')).show();
});

document.getElementById('confirmAdd').addEventListener('click', async () => {
    const params = new URLSearchParams({
        etudiantId: document.getElementById('addEtudiantId').value,
        coursId: document.getElementById('addCoursId').value,
        note: document.getElementById('addNote').value,
        dateNote: document.getElementById('addDate').value
    });

    try {
        const resp = await fetch(API_NOTES, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params.toString()
        });
        const data = await resp.json();

        bootstrap.Modal.getInstance(document.getElementById('addModal')).hide();
        afficherMessage(data.message, data.success);

        if (data.success) chargerNotes(document.getElementById('searchInput').value.trim());

    } catch (err) {
        console.error(err);
        afficherMessage('Erreur réseau', false);
    }
});

// ============================================================
// SUPPRESSION
// ============================================================
function ouvrirSuppression(id, nom) {
    idASupprimer = id;
    document.getElementById('deleteNom').textContent = nom;
    new bootstrap.Modal(document.getElementById('deleteModal')).show();
}

document.getElementById('confirmDelete').addEventListener('click', async () => {
    if (!idASupprimer) return;

    try {
        const resp = await fetch(`${API_NOTES}/${idASupprimer}`, { method: 'DELETE' });
        const data = await resp.json();

        bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();
        afficherMessage(data.message, data.success);

        if (data.success) chargerNotes(document.getElementById('searchInput').value.trim());

    } catch (err) {
        console.error(err);
        afficherMessage('Erreur réseau', false);
    } finally {
        idASupprimer = null;
    }
});

// ============================================================
// MODIFICATION
// ============================================================
async function ouvrirModification(id) {
    try {
        const resp = await fetch(`${API_NOTES}/${id}`);
        const data = await resp.json();

        if (!data.success) {
            afficherMessage(data.message, false);
            return;
        }

        const n = data.data;
        idAModifier = n.id;

        // Remplir les selects
        remplirSelect('editEtudiantId', etudiants, 'id', 
            e => `${e.nom} (${e.email})`, n.etudiantId);
        remplirSelect('editCoursId', coursList, 'id', 
            c => `${c.code} - ${c.intitule}`, n.coursId);

        document.getElementById('editNote').value = n.note;
        document.getElementById('editDate').value = n.dateNote || '';

        new bootstrap.Modal(document.getElementById('editModal')).show();

    } catch (err) {
        console.error(err);
        afficherMessage('Erreur réseau', false);
    }
}

document.getElementById('confirmEdit').addEventListener('click', async () => {
    if (!idAModifier) return;

    const params = new URLSearchParams({
        note: document.getElementById('editNote').value,
        dateNote: document.getElementById('editDate').value
    });

    try {
        const resp = await fetch(`${API_NOTES}/${idAModifier}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params.toString()
        });
        const data = await resp.json();

        bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
        afficherMessage(data.message, data.success);

        if (data.success) chargerNotes(document.getElementById('searchInput').value.trim());

    } catch (err) {
        console.error(err);
        afficherMessage('Erreur réseau', false);
    } finally {
        idAModifier = null;
    }
});

// ============================================================
// Utilitaires
// ============================================================
function afficherMessage(msg, succes) {
    const div = document.getElementById('message');
    div.className = `alert alert-${succes ? 'success' : 'danger'}`;
    div.textContent = msg;
    div.classList.remove('d-none');
    setTimeout(() => div.classList.add('d-none'), 3000);
}

function escapeHtml(str) {
    if (str === null || str === undefined) return '';
    const div = document.createElement('div');
    div.textContent = String(str);
    return div.innerHTML;
}

function escapeJs(str) {
    if (str === null || str === undefined) return '';
    return String(str).replace(/\\/g, '\\\\').replace(/'/g, "\\'").replace(/"/g, '\\"');
}

// ============================================================
// Initialisation
// ============================================================
async function initialiser() {
    await Promise.all([chargerEtudiants(), chargerCoursList()]);
    await chargerNotes();
}

initialiser();