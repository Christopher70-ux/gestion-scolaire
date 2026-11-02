<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard | MonApp</title>

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
        <div class="row g-4">
          <div class="col-md-3">
            <div class="card shadow-sm border-0 h-100">
              <div class="card-body">
                <h6 class="text-muted">Étudiants</h6>
                <h3 class="mb-0">1,248</h3>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="card shadow-sm border-0 h-100">
              <div class="card-body">
                <h6 class="text-muted">Cours</h6>
                <h3 class="mb-0">64</h3>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="card shadow-sm border-0 h-100">
              <div class="card-body">
                <h6 class="text-muted">Notes</h6>
                <h3 class="mb-0">9,832</h3>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="card shadow-sm border-0 h-100">
              <div class="card-body">
                <h6 class="text-muted">Bulletins</h6>
                <h3 class="mb-0">420</h3>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />
  </div>

  <script src="/monapp/assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="/monapp/assets/libs/apexcharts/apexcharts.min.js"></script>
  <script src="/monapp/assets/js/dashboard.js"></script>
</body>
</html>
