<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("activePage", "/tables-cours");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cours | MonApp</title>
  <link rel="icon" type="image/png" href="assets/images/favicon.ico">
  <link rel="stylesheet" href="assets/libs/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="assets/libs/bootstrap-icons/bootstrap-icons.css">
  <link rel="stylesheet" href="assets/css/main.css">
</head>
<body>
  <jsp:include page="/WEB-INF/includes/sidebar.jsp" />

  <div class="main-wrapper">
    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="page-content">
      <div class="container-fluid py-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <div>
            <h1 class="h3 mb-1">Cours</h1>
            <p class="text-muted mb-0">Catalogue des enseignements</p>
          </div>
          <button id="btnAjouter" class="btn btn-primary">+ Nouveau cours</button>
        </div>

        <div class="card shadow-sm border-0">
          <div class="card-body p-0">
            <div class="p-3 border-bottom bg-light">
              <div class="input-group" style="max-width: 340px;">
                <span class="input-group-text"><i class="bi bi-search"></i></span>
                <input id="searchInput" type="text" class="form-control" placeholder="Rechercher un cours...">
              </div>
            </div>

            <div id="message" class="alert alert-success d-none mx-3 mt-3 mb-0" role="alert"></div>

            <div class="table-responsive">
              <table class="table align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    <th>ID</th>
                    <th>Code</th>
                    <th>Intitulé</th>
                    <th>Coefficient</th>
                    <th>Volume</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody id="tableBody">
                  <tr>
                    <td colspan="6" class="text-center text-muted">Chargement...</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div class="p-3 border-top text-muted small" id="paginationInfo">0 cours</div>
          </div>
        </div>
      </div>
    </main>

    <div class="modal fade" id="addModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Ajouter un cours</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label class="form-label">Code</label>
              <input id="addCode" type="text" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Intitulé</label>
              <input id="addIntitule" type="text" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Coefficient</label>
              <input id="addCoefficient" type="number" min="1" value="1" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Volume heures</label>
              <input id="addVolumeHeures" type="number" min="0" value="0" class="form-control">
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
            <button id="confirmAdd" class="btn btn-primary">Enregistrer</button>
          </div>
        </div>
      </div>
    </div>

    <div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Modifier le cours</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <input id="editId" type="hidden">
            <div class="mb-3">
              <label class="form-label">Code</label>
              <input id="editCode" type="text" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Intitulé</label>
              <input id="editIntitule" type="text" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Coefficient</label>
              <input id="editCoefficient" type="number" min="1" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Volume heures</label>
              <input id="editVolumeHeures" type="number" min="0" class="form-control">
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
            <button id="confirmEdit" class="btn btn-primary">Enregistrer</button>
          </div>
        </div>
      </div>
    </div>

    <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-sm">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Supprimer</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <p class="mb-0">Voulez-vous vraiment supprimer <strong id="deleteNom">ce cours</strong> ?</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
            <button id="confirmDelete" class="btn btn-danger">Supprimer</button>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />
  </div>

  <script src="assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="assets/js/dashboard.js"></script>
  <script src="assets/js/cours.js"></script>
</body>
</html>
