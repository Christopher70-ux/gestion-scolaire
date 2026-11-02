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
          <button class="btn btn-primary">Imprimer</button>
        </div>

        <div class="row g-4">
          <div class="col-md-4">
            <div class="card border-0 shadow-sm h-100">
              <div class="card-body">
                <h6 class="text-muted">Semestre 1</h6>
                <h3 class="mb-0">14.8</h3>
              </div>
            </div>
          </div>
          <div class="col-md-4">
            <div class="card border-0 shadow-sm h-100">
              <div class="card-body">
                <h6 class="text-muted">Semestre 2</h6>
                <h3 class="mb-0">15.9</h3>
              </div>
            </div>
          </div>
          <div class="col-md-4">
            <div class="card border-0 shadow-sm h-100">
              <div class="card-body">
                <h6 class="text-muted">Moyenne générale</h6>
                <h3 class="mb-0">15.3</h3>
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
