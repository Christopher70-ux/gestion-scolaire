<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    HttpSession userSession = request.getSession(false);
    String userNom = userSession != null ? (String) userSession.getAttribute("userNom") : null;
    String userEmail = userSession != null ? (String) userSession.getAttribute("userEmail") : null;
    boolean isConnected = userNom != null && !userNom.trim().isEmpty();
%>

<header class="navbar-custom">
  <div class="navbar-left">
    <button class="sidebar-toggle-btn me-2" id="sidebar-toggle" aria-label="Toggle Navigation">
      <i class="bi bi-list"></i>
    </button>

    <div class="dropdown ms-2">
      <button class="btn-quick-action dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false" id="quick-actions-dropdown">
        <i class="bi bi-plus-lg"></i>
        <span>Create</span>
      </button>
      <ul class="dropdown-menu dropdown-menu-quick-action" aria-labelledby="quick-actions-dropdown">
        <li class="dropdown-header">Quick Action Shortcuts</li>
        <li><a class="dropdown-item" href="#"><i class="bi bi-file-earmark-plus"></i> New Invoice</a></li>
        <li><a class="dropdown-item" href="#"><i class="bi bi-person-plus"></i> New User</a></li>
        <li><a class="dropdown-item" href="#"><i class="bi bi-box-seam"></i> New Product</a></li>
        <li><hr class="dropdown-divider"></li>
        <li><a class="dropdown-item" href="#"><i class="bi bi-gear"></i> System Settings</a></li>
      </ul>
    </div>
  </div>

  <div class="navbar-search-wrapper">
    <input type="text" class="navbar-search-input" placeholder="Search anything in Spark..." id="main-search">
    <button class="navbar-search-btn" aria-label="Search">
      <i class="bi bi-search"></i>
    </button>
  </div>

  <div class="navbar-actions">
    <button class="navbar-action-btn me-1" aria-label="Toggle Fullscreen" id="btn-fullscreen">
      <i class="bi bi-arrows-fullscreen"></i>
    </button>

    <% if (isConnected) { %>
      <div class="dropdown ms-2">
        <button class="navbar-profile-btn dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false" id="profile-dropdown">
          <img src="/monapp/assets/images/avatar.png" alt="Profile Image" class="navbar-profile-img">
          <span class="navbar-profile-name"><%= userNom %></span>
        </button>

        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profile-dropdown">
          <li class="px-3 py-2 border-bottom">
            <div class="small text-muted">Connecté</div>
            <div class="fw-semibold"><%= userNom %></div>
            <% if (userEmail != null && !userEmail.isBlank()) { %>
              <div class="small text-muted"><%= userEmail %></div>
            <% } %>
          </li>
          <li><a class="dropdown-item" href="/monapp/dashboard"><i class="bi bi-grid"></i> Dashboard</a></li>
          <li><a class="dropdown-item" href="#"><i class="bi bi-person"></i> Profil</a></li>
          <li><a class="dropdown-item" href="#"><i class="bi bi-gear"></i> Paramètres</a></li>
          <li><hr class="dropdown-divider"></li>
          <li>
            <form action="/monapp/logout" method="post" class="d-inline w-100">
              <button type="submit" class="dropdown-item text-danger w-100 text-start border-0 bg-transparent">
                <i class="bi bi-box-arrow-right"></i> Déconnexion
              </button>
            </form>
          </li>
        </ul>
      </div>
    <% } else { %>
      <a class="btn btn-outline-light me-2" href="/monapp/connexion.html">Connexion</a>
      <a class="btn btn-primary" href="/monapp/inscription.html">Inscription</a>
    <% } %>
  </div>
</header>
