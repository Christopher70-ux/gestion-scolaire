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
  <link rel="icon" type="image/png" href="/monapp/assets/images/favicon.ico">
  <link rel="stylesheet" href="/monapp/assets/libs/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="/monapp/assets/libs/bootstrap-icons/bootstrap-icons.css">
  <link rel="stylesheet" href="/monapp/assets/libs/apexcharts/apexcharts.css">
  <link rel="stylesheet" href="/monapp/assets/libs/flatpickr/flatpickr.min.css">
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
            <h1 class="h3 mb-1">Étudiants</h1>
            <p class="text-muted mb-0">Liste et gestion des étudiants</p>
          </div>
          <button class="btn btn-primary">+ Ajouter</button>
        </div>

        <div class="card shadow-sm border-0">
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    <th>Nom</th>
                    <th>Email</th>
                    <th>Classe</th>
                    <th>Statut</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>Amine Benali</td>
                    <td>amine@monapp.fr</td>
                    <td>Licence 2</td>
                    <td><span class="badge bg-success">Actif</span></td>
                  </tr>
                  <tr>
                    <td>Sarah Diallo</td>
                    <td>sarah@monapp.fr</td>
                    <td>Master 1</td>
                    <td><span class="badge bg-warning text-dark">À suivre</span></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />
  </div>

  <script src="/monapp/assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="/monapp/assets/js/dashboard.js"></script>
</body>
</html>
