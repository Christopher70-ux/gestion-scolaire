<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("activePage", "/page-blank");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Page vide | MonApp</title>
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
        <div class="card border-0 shadow-sm">
          <div class="card-body text-center py-5">
            <h1 class="h3 mb-2">Page vide</h1>
            <p class="text-muted mb-0">Cet espace est prêt pour un nouveau module.</p>
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
