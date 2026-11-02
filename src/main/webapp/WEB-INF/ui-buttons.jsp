<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("activePage", "/ui-buttons");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Boutons | MonApp</title>
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
        <div class="card border-0 shadow-sm">
          <div class="card-body">
            <h1 class="h3 mb-4">Boutons</h1>
            <div class="d-flex flex-wrap gap-2">
              <button class="btn btn-primary">Primary</button>
              <button class="btn btn-secondary">Secondary</button>
              <button class="btn btn-success">Success</button>
              <button class="btn btn-warning">Warning</button>
              <button class="btn btn-danger">Danger</button>
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
