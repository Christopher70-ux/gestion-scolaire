<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String activePage = (String) request.getAttribute("activePage");
    if (activePage == null) {
        activePage = "";
    }
%>

<div class="sidebar-wrapper" id="sidebar">
  <a href="/monapp/dashboard" class="sidebar-brand">
    <i class="bi bi-asterisk"></i>
    <span>Spark Admin</span>
  </a>

  <div class="flex-grow-1 overflow-y-auto">
    <div class="sidebar-menu-section">
      <div class="sidebar-menu-title">Menu</div>
      <ul class="sidebar-menu-list">
        <li class="sidebar-menu-item">
          <a href="/monapp/dashboard" class="sidebar-menu-link <%= "/dashboard".equals(activePage) ? "active" : "" %>" id="menu-overview">
            <i class="bi bi-grid-fill"></i>
            <span>Dashboard</span>
          </a>
        </li>
      </ul>
    </div>

    <div class="sidebar-menu-section">
      <div class="sidebar-menu-title">Gestion</div>
      <ul class="sidebar-menu-list">
        <li class="sidebar-menu-item">
          <a href="/monapp/tables-etudiants" class="sidebar-menu-link <%= "/tables-etudiants".equals(activePage) ? "active" : "" %>" id="menu-etudiants">
            <i class="bi bi-people-fill"></i>
            <span>Étudiants</span>
          </a>
        </li>
        <li class="sidebar-menu-item">
          <a href="/monapp/tables-cours" class="sidebar-menu-link <%= "/tables-cours".equals(activePage) ? "active" : "" %>" id="menu-cours">
            <i class="bi bi-book-fill"></i>
            <span>Cours</span>
          </a>
        </li>
        <li class="sidebar-menu-item">
          <a href="/monapp/tables-notes" class="sidebar-menu-link <%= "/tables-notes".equals(activePage) ? "active" : "" %>" id="menu-notes">
            <i class="bi bi-clipboard-data-fill"></i>
            <span>Notes</span>
          </a>
        </li>
        <li class="sidebar-menu-item">
          <a href="/monapp/tables-bulletins" class="sidebar-menu-link <%= "/tables-bulletins".equals(activePage) ? "active" : "" %>" id="menu-bulletins">
            <i class="bi bi-file-earmark-text-fill"></i>
            <span>Bulletins</span>
          </a>
        </li>
      </ul>
    </div>

  </div>

  <div class="sidebar-profile">
    <img src="/monapp/assets/images/avatar.png" alt="Administrator" class="sidebar-profile-img"
      onerror="this.src='https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=256&auto=format&fit=crop'">
    <div class="sidebar-profile-info">
      <div class="sidebar-profile-name">Administrator</div>
      <div class="sidebar-profile-email">admin@email.com</div>
    </div>
  </div>
</div>
