package fr.univlyon1.m1if.m1if03.classes.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.utils.CandidatListGenerator;

@WebServlet(name = "login", value = "/login", loadOnStartup = 1)
public class AuthFilter extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String login = request.getParameter("login");
			if (login != null && !login.equals("")) {
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
