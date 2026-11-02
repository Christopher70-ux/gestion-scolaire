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
            <h1 class="h3 mb-1">Cours</h1>
            <p class="text-muted mb-0">Catalogue des enseignements</p>
          </div>
          <button class="btn btn-primary">+ Nouveau cours</button>
        </div>

        <div class="row g-4">
          <div class="col-md-6">
            <div class="card shadow-sm border-0 h-100">
              <div class="card-body">
                <h5>Programmation Java</h5>
                <p class="text-muted mb-0">12 séances · 3 groupes</p>
              </div>
            </div>
          </div>
          <div class="col-md-6">
            <div class="card shadow-sm border-0 h-100">
              <div class="card-body">
                <h5>Base de données</h5>
                <p class="text-muted mb-0">8 séances · 2 groupes</p>
              </div>
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
