package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import fr.univlyon1.m1if.m1if03.classes.services.CandidatsService;
import fr.univlyon1.m1if.m1if03.classes.services.VoteService;

@WebServlet(name = "ControllerVote", value = {})
public class ControllerVote extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String uri = request.getRequestURI();
		String[] split = uri.split("/");

		if(uri.endsWith("byUser")) VoteService.getVodeByUser(Integer.parseInt(split[split.length - 1])); // /election/votes/byUser/{userId}
		else if(uri.contains("/votes/")) VoteService.getVote(Integer.parseInt(split[split.length - 1])); // /election/votes/{userId}
		//this.getServletContext().getRequestDispatcher("/WEB-INF/components/vote.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String uri = request.getRequestURI();
		String[] split = uri.split("/");

		if(uri.endsWith("byUser")) VoteService.updateVote(Integer.parseInt(split[split.length - 1])); // /election/votes/byUser/{userId}
//		boolean candidatNull = request.getParameter("candidat") == null
//				|| request.getParameter("candidat").equals("")
//				|| request.getParameter("candidat").equals("null");
//		if(request.getRequestURI().endsWith("deleteVote")) {
//			this.getServletContext().getNamedDispatcher("DeleteVote").forward(request, response);
//		} else if(candidatNull){
//			request.getRequestDispatcher("/WEB-INF/components/vote.jsp").forward(request, response);
//		} else {
//			this.getServletContext().getNamedDispatcher("Vote").include(request, response);
//			this.getServletContext().getRequestDispatcher("/WEB-INF/components/ballot.jsp").forward(request, response);
//		}
	}
}
