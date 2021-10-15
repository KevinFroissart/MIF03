package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Controller")
public class Controller extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String url = request.getRequestURL().toString();

		if(url.endsWith("vote")) {
			request.getRequestDispatcher("ControllerVote").forward(request, response);
		} else if (url.endsWith("user")) {
			request.getRequestDispatcher("ControllerUser").forward(request, response);
		} else if (url.endsWith("resultats")) {
			request.getRequestDispatcher("ControllerResultats").forward(request, response);
		} else if (url.endsWith("listBallots")) {
			request.getRequestDispatcher("ControllerListBallots").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String url = request.getRequestURL().toString();

		if(url.endsWith("vote")) {
			request.getRequestDispatcher("ControllerVote").forward(request, response);
		} else if (url.endsWith("user")) {
			request.getRequestDispatcher("ControllerUser").forward(request, response);
		} else if (url.endsWith("resultats")) {
			request.getRequestDispatcher("ControllerResultats").forward(request, response);
		} else if (url.endsWith("listBallots")) {
			request.getRequestDispatcher("ControllerListBallots").forward(request, response);
		}
	}
}
