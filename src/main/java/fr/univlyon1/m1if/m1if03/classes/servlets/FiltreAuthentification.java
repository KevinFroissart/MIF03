package fr.univlyon1.m1if.m1if03.classes.servlets;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import fr.univlyon1.m1if.m1if03.classes.User;

@WebFilter(filterName = "/login")
public class FiltreAuthentification extends HttpFilter {

	public void init(FilterConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			String login = request.getParameter("login");
			System.out.println(request.getRequestURI());
			System.out.println(request.getPathInfo());
			if (login != null && !login.equals("")) {
				chain.doFilter(request, response);
			} else if(request.getPathInfo().equals("index.html")){
				HttpSession session = request.getSession(true);
				session.setAttribute("user", new User(login,
						request.getParameter("nom") != null ? request.getParameter("nom") : "",
						request.getParameter("admin") != null && request.getParameter("admin").equals("on")));
				request.getRequestDispatcher("vote.jsp").forward(request, response);
			} else {
				response.sendRedirect("index.html");
			}
		} catch (IOException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
