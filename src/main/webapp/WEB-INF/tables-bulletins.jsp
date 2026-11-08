<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("activePage", "/tables-bulletins");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Bulletins | MonApp</title>
  <link rel="icon" type="image/png" href="/monapp/assets/images/favicon.ico">
  <link rel="stylesheet" href="/monapp/assets/libs/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="/monapp/assets/libs/bootstrap-icons/bootstrap-icons.css">
  <link rel="stylesheet" href="/monapp/assets/css/main.css">
</head>
<body>
  <jsp:include page="/WEB-INF/includes/sidebar.jsp" />

  <div class="main-wrapper">
    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="page-content">
      <div class="container-fluid py-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <div>
            <h1 class="h3 mb-1">Bulletins</h1>
            <p class="text-muted mb-0">Résultats et évaluations</p>
          </div>
          <button class="btn btn-primary" id="btnImprimer">Imprimer</button>
        </div>

        <div class="card shadow-sm border-0">
          <div class="card-body p-0">
            <div class="p-3 border-bottom bg-light">
              <div class="input-group" style="max-width: 340px;">
                <span class="input-group-text"><i class="bi bi-search"></i></span>
                <input id="searchInput" type="text" class="form-control" placeholder="Rechercher un étudiant...">
              </div>
            </div>

            <div class="table-responsive">
              <table class="table align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    <th>ID</th>
                    <th>Étudiant</th>
                    <th>Email</th>
                    <th>Filière</th>
                    <th>Niveau</th>
                    <th>Nb notes</th>
                    <th>Moyenne</th>
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

            <div class="p-3 border-top text-muted small" id="paginationInfo">0 étudiant(s)</div>
          </div>
        </div>
      </div>
    </main>

    <div class="modal fade" id="bulletinModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-xl modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="bulletinTitle">Bulletin</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="row mb-3">
              <div class="col-md-6">
                <p class="mb-1 text-muted">Étudiant</p>
                <strong id="bEtudiant">-</strong>
              </div>
              <div class="col-md-6">
                <p class="mb-1 text-muted">Email</p>
                <strong id="bEmail">-</strong>
              </div>
            </div>
            <div class="row mb-3">
              <div class="col-md-6">
                <p class="mb-1 text-muted">Filière</p>
                <strong id="bFiliere">-</strong>
              </div>
              <div class="col-md-6">
                <p class="mb-1 text-muted">Niveau</p>
                <strong id="bNiveau">-</strong>
              </div>
            </div>

            <table class="table table-bordered align-middle">
              <thead class="table-light">
                <tr>
                  <th>Code</th>
                  <th>Cours</th>
                  <th>Note</th>
                  <th>Coeff.</th>
                  <th>Points</th>
                </tr>
              </thead>
              <tbody id="bNotes">
                <tr><td colspan="5" class="text-center text-muted">Aucune donnée</td></tr>
              </tbody>
            </table>

            <div class="row mt-4">
              <div class="col-md-4">
                <div class="border rounded p-3">
                  <div class="text-muted small">Total coeff.</div>
                  <div class="fw-bold fs-5" id="bTotalCoef">0</div>
                </div>
              </div>
              <div class="col-md-4">
                <div class="border rounded p-3">
                  <div class="text-muted small">Total points</div>
                  <div class="fw-bold fs-5" id="bTotalPoints">0.00</div>
                </div>
              </div>
              <div class="col-md-4">
                <div class="border rounded p-3">
                  <div class="text-muted small">Moyenne</div>
                  <div class="fw-bold fs-5" id="bMoyenne">0.00 / 20</div>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <a id="bExportCsv" class="btn btn-outline-primary" href="#">Exporter CSV</a>
            <button class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />
  </div>

  <script src="/monapp/assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="/monapp/assets/js/dashboard.js"></script>
  <script src="/monapp/assets/js/bulletins.js"></script>
</body>
</html>
