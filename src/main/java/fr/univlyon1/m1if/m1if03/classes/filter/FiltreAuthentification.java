package fr.univlyon1.m1if.m1if03.classes.filter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import fr.univlyon1.m1if.m1if03.classes.model.User;

@WebFilter(filterName = "FiltreAuthentification", urlPatterns = "/*")
public class FiltreAuthentification extends HttpFilter {
	private final String[] authorizedURIs = {"/index.html", "/static", "/election/resultats"}; // Manque "/", traité plus bas...
	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		String currentUri = req.getRequestURI().replace(req.getContextPath(), "");

		// Traitement de l'URL racine
		if(currentUri.equals("/")) {
			res.sendRedirect("index.html");
			return;
		}

		// Traitement des autres URLs autorisées sans authentification
		for(String authorizedUri: authorizedURIs) {
			if(currentUri.startsWith(authorizedUri)) {
				super.doFilter(req, res, chain);
				return;
			}
		}

		HttpSession session = req.getSession(false); // On récupère la session sans la créer
		if(session != null && session.getAttribute("user") != null) {
			super.doFilter(req, res, chain);
		} else {
			String login = req.getParameter("login");
			if(req.getMethod().equals("POST") && login != null && !login.equals("")) {
				session = req.getSession(true);
				System.out.println(req.getParameter("nom") + " : " + login);
				session.setAttribute("user", new User(login,
						req.getParameter("nom") != null ? req.getParameter("nom") : "",
						req.getParameter("admin") != null && req.getParameter("admin").equals("on")));
				super.doFilter(req, res, chain);
			} else
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez être connecté pour accéder à cette page.");
		}
	}
}
