package fr.univlyon1.m1if.m1if03.classes.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import fr.univlyon1.m1if.m1if03.classes.model.User;

@WebServlet(name = "Profil", value = {})
public class Profil extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		User utilisateur = (User) session.getAttribute("user");
		if(utilisateur != null) {
			String name = request.getParameter("name");
			utilisateur.setNom(name);
			response.sendRedirect("profil.jsp");
		} else {
			response.sendRedirect("index.html");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect("index.html");
	}
}
