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

@WebFilter(filterName = "/LoginFilter")
public class FiltreAuthentification extends HttpFilter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			request.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession(true);
			String login = request.getParameter("login");
			User utilisateur = (User) session.getAttribute("user");
			String uri = request.getRequestURI().substring(getServletContext().getContextPath().length());

			boolean utilisateurConnecte = utilisateur != null && !utilisateur.getLogin().equals("");
			boolean provientFormulaireAuth = request.getRequestURI() != null && uri.equals("/election/vote");
			boolean ressourceStatic = uri.contains(".css") || uri.equals("/") || uri.endsWith("/index.html");
			boolean ressourceAutorisee = uri.endsWith("/resultats.jsp") || uri.endsWith("/resultats");
			boolean loginNonNull = login != null && !login.equals("");

			if (utilisateurConnecte || ressourceStatic || ressourceAutorisee) {
				System.out.println("autorisé");
				chain.doFilter(request, response);
			} else if(provientFormulaireAuth && loginNonNull){
				session.setAttribute("user", new User(login,
						request.getParameter("nom") != null ? request.getParameter("nom") : "",
						request.getParameter("admin") != null && request.getParameter("admin").equals("on")));
				chain.doFilter(request, response);
			} else {
				response.sendRedirect("index.html");
			}
		} catch (IOException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
