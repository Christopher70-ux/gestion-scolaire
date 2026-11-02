// assets/js/membres.js

const API = '/monapp/api/etudiants';   // ← adapte à ton contextPath

let idASupprimer = null;
let idAModifier = null;

// ============================================================
// Charger et afficher les membres
// ============================================================
async function chargerMembres(query = '') {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '<tr><td colspan="8" class="text-center">Chargement...</td></tr>';

    try {
        const url = query ? `${API}?q=${encodeURIComponent(query)}` : API;
        const resp = await fetch(url);
        const data = await resp.json();

        if (!data.success) {
            tbody.innerHTML = `<tr><td colspan="8" class="text-danger">${data.message}</td></tr>`;
            return;
        }

        if (data.data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted">Aucun membre trouvé</td></tr>';
            return;
        }

        tbody.innerHTML = data.data.map(m => `
            <tr>
                <td>${m.id}</td>
                <td class="table-order-id">${escapeHtml(m.nom)}</td>
                <td class="table-product-name">${escapeHtml(m.email)}</td>
                <td class="table-product-name">${escapeHtml(m.telephone)}</td>
                <td class="table-product-name">${escapeHtml(m.filiere)}</td>
                <td class="table-product-name">${escapeHtml(m.niveau)}</td>
                <td class="table-product-name">${escapeHtml(m.date_inscription)}</td>
    
                <td>
                    <div class="d-flex justify-content-center gap-1">
                        <button class="btn btn-sm btn-primary" 
                                onclick="ouvrirModification(${m.id})">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" 
                                onclick="ouvrirSuppression(${m.id}, '${escapeJs(m.nom)}')">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');

    } catch (err) {
        console.error(err);
        tbody.innerHTML = `<tr><td colspan="6" class="text-danger">Erreur réseau</td></tr>`;
    }
}

// ============================================================
// Recherche (avec délai anti-rebond)
// ============================================================
let timerRecherche = null;
document.getElementById('searchInput').addEventListener('input', (e) => {
    clearTimeout(timerRecherche);
    timerRecherche = setTimeout(() => chargerMembres(e.target.value.trim()), 300);
});

// ============================================================
// SUPPRESSION — ouvrir modal
// ============================================================
function ouvrirSuppression(id, nom) {
    idASupprimer = id;
    document.getElementById('deleteNom').textContent = nom;
    new bootstrap.Modal(document.getElementById('deleteModal')).show();
}

// Confirmer suppression
document.getElementById('confirmDelete').addEventListener('click', async () => {
    if (!idASupprimer) return;

    try {
        const resp = await fetch(`${API}/${idASupprimer}`, { method: 'DELETE' });
        const data = await resp.json();

        bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();
        afficherMessage(data.message, data.success);

        if (data.success) chargerMembres(document.getElementById('searchInput').value.trim());

    } catch (err) {
        console.error(err);
        afficherMessage('Erreur réseau', false);
    } finally {
        idASupprimer = null;
    }
});

// ============================================================
// MODIFICATION — ouvrir modal
// ============================================================
async function ouvrirModification(id) {
    try {
        const resp = await fetch(`${API}/${id}`);
        const data = await resp.json();

        if (!data.success) {
            afficherMessage(data.message, false);
            return;
        }

        const m = data.data;
        idAModifier = m.id;
        document.getElementById('editId').value = m.id;
        document.getElementById('editNom').value = m.nom;
        document.getElementById('editEmail').value = m.email;
        document.getElementById('editTelephone').value = m.telephone;
        document.getElementById('editFiliere').value = m.filiere;
        document.getElementById('editNiveau').value = m.niveau

        new bootstrap.Modal(document.getElementById('editModal')).show();

    } catch (err) {
        console.error(err);
        afficherMessage('Erreur réseau', false);
    }
}

// Confirmer modification
document.getElementById('confirmEdit').addEventListener('click', async () => {
    if (!idAModifier) return;

    const params = new URLSearchParams({
        nom: document.getElementById('editNom').value,
        email: document.getElementById('editEmail').value,
        telephone : document.getElementById('editTelephone').value,
        filiere : document.getElementById('editFiliere').value,
        niveau : document.getElementById('editNiveau').value
    });

    try {
        const resp = await fetch(`${API}/${idAModifier}?${params}`, { method: 'PUT' });
        const data = await resp.json();

        bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
        afficherMessage(data.message, data.success);

        if (data.success) chargerMembres(document.getElementById('searchInput').value.trim());

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
chargerMembres();

// ============================================================
// AJOUT — ouvrir modal
// ============================================================
document.getElementById('btnAjouter').addEventListener('click', () => {
    // Vider les champs
    document.getElementById('addNom').value = '';
    document.getElementById('addEmail').value = '';
    document.getElementById('addTelephone').value = '';
    document.getElementById('addFiliere').value = '';
    document.getElementById('addNiveau').value = '';
    
    new bootstrap.Modal(document.getElementById('addModal')).show();
});

// Confirmer ajout
document.getElementById('confirmAdd').addEventListener('click', async () => {
    const params = new URLSearchParams({
        nom: document.getElementById('addNom').value,
        email: document.getElementById('addEmail').value,
        telephone: document.getElementById('addTelephone').value,
        filiere: document.getElementById('addFiliere').value,
        niveau: document.getElementById('addNiveau').value
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
        
        if (data.success) chargerMembres(document.getElementById('searchInput').value.trim());
        
    } catch (err) {
        console.error(err);
        afficherMessage('Erreur réseau', false);
    }
});