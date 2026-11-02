package org.example.servlet;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({
        "/dashboard",
        "/tables-etudiants",
        "/tables-cours",
        "/tables-notes",
        "/tables-bulletins",
        "/ui-forms",
        "/ui-buttons",
        "/page-blank",
        "/page-404"
})
public class DashboardServlet extends HttpServlet {

    private static final Map<String, String> PAGES = Map.of(
            "/dashboard", "/WEB-INF/dashboard.jsp",
            "/tables-etudiants", "/WEB-INF/tables-etudiants.jsp",
            "/tables-cours", "/WEB-INF/tables-cours.jsp",
            "/tables-notes", "/WEB-INF/tables-notes.jsp",
            "/tables-bulletins", "/WEB-INF/tables-bulletins.jsp",
            "/ui-forms", "/WEB-INF/ui-forms.jsp",
            "/ui-buttons", "/WEB-INF/ui-buttons.jsp",
            "/page-blank", "/WEB-INF/page-blank.jsp",
            "/page-404", "/WEB-INF/page-404.jsp");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/connexion.html");
            return;
        }

        String path = req.getServletPath();
        String target = PAGES.getOrDefault(path, "/WEB-INF/dashboard.jsp");
        req.setAttribute("activePage", path);
        req.getRequestDispatcher(target).forward(req, resp);
    }
}