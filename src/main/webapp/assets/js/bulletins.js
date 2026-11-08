// assets/js/bulletins.js

const API_BULLETINS = '/api/bulletins';

let tousLesBulletins = [];

// ============================================================
// Charger les bulletins
// ============================================================
async function chargerBulletins(query = '') {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '<tr><td colspan="8" class="text-center">Chargement...</td></tr>';

    try {
        const resp = await fetch(API_BULLETINS);
        const data = await resp.json();

        if (!data.success) {
            tbody.innerHTML = `<tr><td colspan="8" class="text-danger">${data.message}</td></tr>`;
            return;
        }

        tousLesBulletins = data.data || [];

        // Filtre côté client (recherche)
        const filtres = query 
            ? tousLesBulletins.filter(b => 
                (b.etudiantNom || '').toLowerCase().includes(query.toLowerCase()) ||
                (b.email || '').toLowerCase().includes(query.toLowerCase()))
            : tousLesBulletins;

        if (filtres.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted">Aucun étudiant trouvé</td></tr>';
            document.getElementById('paginationInfo').textContent = '0 étudiant(s)';
            return;
        }

        tbody.innerHTML = filtres.map(b => {
            const moyenne = b.moyenneGenerale || 0;
            const badge = moyenne >= 10 ? 'bg-success' : 'bg-danger';
            const nbNotes = (b.notes && b.notes.length) || 0;
            
            return `
                <tr>
                    <td>${b.etudiantId}</td>
                    <td><strong>${escapeHtml(b.etudiantNom)}</strong></td>
                    <td>${escapeHtml(b.email)}</td>
                    <td>${escapeHtml(b.filiere)}</td>
                    <td>${escapeHtml(b.niveau)}</td>
                    <td class="text-center">${nbNotes}</td>
                    <td class="text-center">
                        <span class="badge ${badge}" style="font-size: 1em;">
                            ${moyenne.toFixed(2)} / 20
                        </span>
                    </td>
                    <td class="text-center">
                        <div class="d-flex justify-content-center gap-1">
                            <button class="btn btn-sm btn-primary" 
                                    onclick="ouvrirBulletin(${b.etudiantId})" title="Voir">
                                <i class="bi bi-eye"></i>
                            </button>
                            <a href="/api/export/bulletin/${b.etudiantId}" 
                               class="btn btn-sm btn-outline-primary" title="Exporter CSV">
                                <i class="bi bi-file-earmark-arrow-down"></i>
                            </a>
                        </div>
                    </td>
                </tr>
            `;
        }).join('');

        document.getElementById('paginationInfo').textContent = 
            `${filtres.length} étudiant(s)`;

    } catch (err) {
        console.error(err);
        tbody.innerHTML = `<tr><td colspan="8" class="text-danger">Erreur réseau</td></tr>`;
    }
}

// ============================================================
// Ouvrir le bulletin d'un étudiant
// ============================================================
function ouvrirBulletin(etudiantId) {
    const b = tousLesBulletins.find(x => x.etudiantId === etudiantId);
    if (!b) return;

    // En-tête
    document.getElementById('bulletinTitle').textContent = `📄 Bulletin de ${b.etudiantNom}`;
    document.getElementById('bEtudiant').textContent = b.etudiantNom;
    document.getElementById('bEmail').textContent = b.email;
    document.getElementById('bFiliere').textContent = b.filiere;
    document.getElementById('bNiveau').textContent = b.niveau;

    // Tableau des notes
    const tbody = document.getElementById('bNotes');
    tbody.innerHTML = (b.notes || []).map(n => {
        const ponderee = (n.note * n.coefficient).toFixed(2);
        const badge = n.note >= 10 ? 'bg-success' : 'bg-danger';
        return `
            <tr>
                <td><span class="badge bg-info text-dark">${escapeHtml(n.coursCode)}</span></td>
                <td>${escapeHtml(n.coursIntitule)}</td>
                <td class="text-center">
                    <span class="badge ${badge}">${n.note.toFixed(2)}</span>
                </td>
                <td class="text-center">${n.coefficient}</td>
                <td class="text-center"><strong>${ponderee}</strong></td>
            </tr>
        `;
    }).join('');

    // Totaux
    document.getElementById('bTotalCoef').textContent = b.totalCoefficients;
    document.getElementById('bTotalPoints').textContent = b.totalPoints.toFixed(2);
    document.getElementById('bMoyenne').textContent = b.moyenneGenerale.toFixed(2) + ' / 20';

    // Lien d'export
    document.getElementById('bExportCsv').href = 
        `/api/export/bulletin/${etudiantId}`;

    new bootstrap.Modal(document.getElementById('bulletinModal')).show();
}

// ============================================================
// Recherche
// ============================================================
let timerRecherche = null;
document.getElementById('searchInput').addEventListener('input', (e) => {
    clearTimeout(timerRecherche);
    timerRecherche = setTimeout(() => chargerBulletins(e.target.value.trim()), 300);
});

// ============================================================
// Utilitaires
// ============================================================
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
chargerBulletins();