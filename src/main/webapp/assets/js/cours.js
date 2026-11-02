// assets/js/cours.js

const API = '/monapp/api/cours';

let idASupprimer = null;
let idAModifier = null;

// ============================================================
// Charger et afficher les cours
// ============================================================
async function chargerCours(query = '') {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '<tr><td colspan="6" class="text-center">Chargement...</td></tr>';

    try {
        const url = query ? `${API}?q=${encodeURIComponent(query)}` : API;
        const resp = await fetch(url);
        const data = await resp.json();

        if (!data.success) {
            tbody.innerHTML = `<tr><td colspan="6" class="text-danger">${data.message}</td></tr>`;
            return;
        }

        if (!data.data || data.data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">Aucun cours trouvé</td></tr>';
            document.getElementById('paginationInfo').textContent = '0 cours';
            return;
        }

        tbody.innerHTML = data.data.map(c => `
            <tr>
                <td>${c.id}</td>
                <td><span class="badge bg-info text-dark">${escapeHtml(c.code)}</span></td>
                <td>${escapeHtml(c.intitule)}</td>
                <td class="text-center"><span class="badge bg-primary">${c.coefficient}</span></td>
                <td class="text-center">${c.volumeHeures} h</td>
                <td class="text-center">
                    <div class="d-flex justify-content-center gap-1">
                        <button class="btn btn-sm btn-primary" onclick="ouvrirModification(${c.id})" title="Modifier">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" 
                                onclick="ouvrirSuppression(${c.id}, '${escapeJs(c.code)}')" title="Supprimer">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');

        document.getElementById('paginationInfo').textContent = `${data.data.length} cours`;

    } catch (err) {
        console.error(err);
        tbody.innerHTML = `<tr><td colspan="6" class="text-danger">Erreur réseau</td></tr>`;
    }
}

// ============================================================
// Recherche avec délai anti-rebond
// ============================================================
let timerRecherche = null;
document.getElementById('searchInput').addEventListener('input', (e) => {
    clearTimeout(timerRecherche);
    timerRecherche = setTimeout(() => chargerCours(e.target.value.trim()), 300);
});

// ============================================================
// AJOUT — ouvrir modal
// ============================================================
document.getElementById('btnAjouter').addEventListener('click', () => {
    document.getElementById('addCode').value = '';
    document.getElementById('addIntitule').value = '';
    document.getElementById('addCoefficient').value = '1';
    document.getElementById('addVolumeHeures').value = '0';
    new bootstrap.Modal(document.getElementById('addModal')).show();
});

document.getElementById('confirmAdd').addEventListener('click', async () => {
    const params = new URLSearchParams({
        code: document.getElementById('addCode').value.trim(),
        intitule: document.getElementById('addIntitule').value.trim(),
        coefficient: document.getElementById('addCoefficient').value,
        volumeHeures: document.getElementById('addVolumeHeures').value
    });

    try {
        const resp = await fetch(API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params.toString()
        });
        const data = await resp.json();

        bootstrap.Modal.getInstance(document.getElementById('addModal')).hide();
        afficherMessage(data.message, data.success);

        if (data.success) chargerCours(document.getElementById('searchInput').value.trim());

    } catch (err) {
        console.error(err);
        afficherMessage('Erreur réseau', false);
    }
});

// ============================================================
// SUPPRESSION
// ============================================================
function ouvrirSuppression(id, code) {
    idASupprimer = id;
    document.getElementById('deleteNom').textContent = code;
    new bootstrap.Modal(document.getElementById('deleteModal')).show();
}

document.getElementById('confirmDelete').addEventListener('click', async () => {
    if (!idASupprimer) return;

    try {
        const resp = await fetch(`${API}/${idASupprimer}`, { method: 'DELETE' });
        const data = await resp.json();

        bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();
        afficherMessage(data.message, data.success);

        if (data.success) chargerCours(document.getElementById('searchInput').value.trim());

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
        const resp = await fetch(`${API}/${id}`);
        const data = await resp.json();

        if (!data.success) {
            afficherMessage(data.message, false);
            return;
        }

        const c = data.data;
        idAModifier = c.id;
        document.getElementById('editId').value = c.id;
        document.getElementById('editCode').value = c.code || '';
        document.getElementById('editIntitule').value = c.intitule || '';
        document.getElementById('editCoefficient').value = c.coefficient || 1;
        document.getElementById('editVolumeHeures').value = c.volumeHeures || 0;

        new bootstrap.Modal(document.getElementById('editModal')).show();

    } catch (err) {
        console.error(err);
        afficherMessage('Erreur réseau', false);
    }
}

document.getElementById('confirmEdit').addEventListener('click', async () => {
    if (!idAModifier) return;

    const params = new URLSearchParams({
        code: document.getElementById('editCode').value.trim(),
        intitule: document.getElementById('editIntitule').value.trim(),
        coefficient: document.getElementById('editCoefficient').value,
        volumeHeures: document.getElementById('editVolumeHeures').value
    });

    try {
        const resp = await fetch(`${API}/${idAModifier}?${params}`, { method: 'PUT' });
        const data = await resp.json();

        bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
        afficherMessage(data.message, data.success);

        if (data.success) chargerCours(document.getElementById('searchInput').value.trim());

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

// Chargement initial
chargerCours();