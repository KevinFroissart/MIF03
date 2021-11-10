package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import fr.univlyon1.m1if.m1if03.classes.services.ResultatsService;

@WebServlet(name = "ControllerResultats", value = {})
public class ControllerResultats extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String uri = request.getRequestURI();

		if(uri.endsWith("resultats")) ResultatsService.getResultats();
//		this.getServletContext().getNamedDispatcher("Resultats").include(request, response);
//		this.getServletContext().getRequestDispatcher("/WEB-INF/components/resultats.jsp").forward(request, response);
	}

//	@Override
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//		this.getServletContext().getRequestDispatcher("/WEB-INF/components/resultats.jsp").forward(request, response);
//	}
}
