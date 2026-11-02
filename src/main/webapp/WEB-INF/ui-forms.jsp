<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("activePage", "/ui-forms");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Formulaires | MonApp</title>
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
            <h1 class="h3 mb-4">Formulaire</h1>
            <form class="row g-3">
              <div class="col-md-6">
                <label class="form-label">Nom</label>
                <input type="text" class="form-control" placeholder="Votre nom">
              </div>
              <div class="col-md-6">
                <label class="form-label">Email</label>
                <input type="email" class="form-control" placeholder="nom@exemple.fr">
              </div>
              <div class="col-12">
                <label class="form-label">Message</label>
                <textarea class="form-control" rows="4" placeholder="Saisissez votre message..."></textarea>
              </div>
              <div class="col-12">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
              </div>
            </form>
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
