<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("activePage", "/page-404");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>404 | MonApp</title>
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
        <div class="card border-0 shadow-sm text-center py-5">
          <div class="card-body">
            <h1 class="display-1 fw-bold text-primary">404</h1>
            <h2 class="h4 mb-3">Page introuvable</h2>
            <p class="text-muted">La ressource demandée n’existe pas ou a été déplacée.</p>
            <a href="dashboard" class="btn btn-primary">Retour au dashboard</a>
          </div>
        </div>
      </div>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />
  </div>

  <script src="assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="assets/js/dashboard.js"></script>
</body>
</html>
