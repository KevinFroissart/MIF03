package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/election/ControllerVote")
public class ControllerVote extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.getRequestDispatcher("../WEB-INF/components/vote.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		boolean candidatNull = request.getParameter("candidat") == null
				|| request.getParameter("candidat").equals("")
				|| request.getParameter("candidat").equals("null");
		if(candidatNull){
			request.getRequestDispatcher("../WEB-INF/components/vote.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/castVote").include(request, response);
			request.getRequestDispatcher("../WEB-INF/components/ballot.jsp").forward(request, response);
		}
	}
}
