<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("activePage", "/tables-etudiants");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Étudiants | MonApp</title>
  <link rel="icon" type="image/png" href="assets/images/favicon.ico">
  <link rel="stylesheet" href="assets/libs/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="assets/libs/bootstrap-icons/bootstrap-icons.css">
  <link rel="stylesheet" href="assets/libs/apexcharts/apexcharts.css">
  <link rel="stylesheet" href="assets/libs/flatpickr/flatpickr.min.css">
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
            <h1 class="h3 mb-1">Étudiants</h1>
            <p class="text-muted mb-0">Liste et gestion des étudiants</p>
          </div>
          <button id="btnAjouter" class="btn btn-primary">+ Ajouter</button>
        </div>

        <div class="card shadow-sm border-0">
          <div class="card-body p-0">
            <div class="p-3 border-bottom bg-light">
              <div class="input-group" style="max-width: 340px;">
                <span class="input-group-text"><i class="bi bi-search"></i></span>
                <input id="searchInput" type="text" class="form-control" placeholder="Rechercher un étudiant...">
              </div>
            </div>

            <div id="message" class="alert alert-success d-none mx-3 mt-3 mb-0" role="alert"></div>

            <div class="table-responsive">
              <table class="table align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Email</th>
                    <th>Téléphone</th>
                    <th>Filière</th>
                    <th>Niveau</th>
                    <th>Date</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody id="tableBody">
                  <tr>
                    <td colspan="8" class="text-center text-muted">Chargement...</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </main>

    <div class="modal fade" id="addModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Ajouter un étudiant</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label class="form-label">Nom</label>
              <input id="addNom" type="text" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Email</label>
              <input id="addEmail" type="email" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Téléphone</label>
              <input id="addTelephone" type="text" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Filière</label>
              <input id="addFiliere" type="text" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Niveau</label>
              <input id="addNiveau" type="text" class="form-control">
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
            <h5 class="modal-title">Modifier l’étudiant</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <input id="editId" type="hidden">
            <div class="mb-3">
              <label class="form-label">Nom</label>
              <input id="editNom" type="text" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Email</label>
              <input id="editEmail" type="email" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Téléphone</label>
              <input id="editTelephone" type="text" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Filière</label>
              <input id="editFiliere" type="text" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Niveau</label>
              <input id="editNiveau" type="text" class="form-control">
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
            <p class="mb-0">Voulez-vous vraiment supprimer <strong id="deleteNom">cet étudiant</strong> ?</p>
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
  <script src="assets/js/etudiants.js"></script>
</body>
</html>
