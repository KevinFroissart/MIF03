package fr.univlyon1.m1if.m1if03.classes.filter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import fr.univlyon1.m1if.m1if03.utils.ElectionM1if03JwtHelper;

@WebFilter(filterName = "FiltreAuthentification", urlPatterns = "/*")
public class FiltreAuthentification extends HttpFilter {

    private final String[] authorizedURIs = {
            "/index.html",
            "/static/votes.css",
            "/election/resultats",
            "/election/candidats",
            "/election/candidats/noms",
            "/users/login",
            "/users/logout"
    };

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String currentUri = req.getRequestURI().replace(req.getContextPath(), "");

        // Traitement de l'URL racine
        if (currentUri.equals("/")) {
            res.sendRedirect("index.html");
            return;
        }

        // Traitement des autres URLs autorisées sans authentification
        for (String authorizedUri : authorizedURIs) {
            if (currentUri.endsWith(authorizedUri)) {
                super.doFilter(req, res, chain);
                return;
            }
        }

        if (req.getHeader("Authorization") != null) {
            String token = req.getHeader("Authorization");

            List<String> expiredTokens = (List<String>) req.getServletContext().getAttribute("expiredTokens");

            if (expiredTokens != null && expiredTokens.contains(token)) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez être authentifié pour accéder à cette page.");
                return;
            }

            try {
                ElectionM1if03JwtHelper.verifyToken(token, req);
                req.setAttribute("token", token);
                super.doFilter(req, res, chain);
                return;
            } catch (Exception e) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez être authentifié pour accéder à cette page.");
                return;
            }
        }
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez être authentifié pour accéder à cette page.");
    }
}
