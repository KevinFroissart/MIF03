package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.univlyon1.m1if.m1if03.classes.model.Ballot;
import fr.univlyon1.m1if.m1if03.classes.model.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.model.Candidat;
import fr.univlyon1.m1if.m1if03.classes.model.User;

@WebServlet(name = "Controller")
public class Controller extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String url = request.getRequestURL().toString();
		System.out.println("get : " + url);

		if(url.endsWith("vote")) {
			request.getRequestDispatcher("../vote.jsp").forward(request, response); //OK
		} else if (url.endsWith("user")) {
			request.getRequestDispatcher("../profile.jsp").forward(request, response); // OK
		} else if (url.endsWith("resultats")) {
			Map<String, Candidat> candidats = (Map<String, Candidat>) request.getServletContext().getAttribute("candidats");
			//Map<String, Ballot> ballots = (Map<String, Ballot>) request.getServletContext().getAttribute("ballots");
			List<Bulletin> bulletins = (List<Bulletin>) request.getServletContext().getAttribute("bulletins");

			Map<String, Integer> votes = new LinkedHashMap<>();
			for (String nomCandidat : candidats.keySet()) {
				votes.put(nomCandidat, 0);
			}
			for (Bulletin bulletin : bulletins) {
				int score = votes.get(bulletin.getCandidat().getNom());
				votes.put(bulletin.getCandidat().getNom(), ++score);
			}
			request.setAttribute("votes", votes);
			request.getRequestDispatcher("../resultats.jsp").forward(request, response); // OK
		} else if (url.endsWith("listBallots")) { // OK
			System.out.println("get list ballots");
			request.getRequestDispatcher("../listBallots.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String url = request.getRequestURL().toString();
		System.out.println("post : " + url);

		if(url.endsWith("vote")) { // OK
			boolean candidatNull = request.getParameter("candidat") == null
					|| request.getParameter("candidat").equals("")
					|| request.getParameter("candidat").equals("null");

			if(candidatNull){
				request.getRequestDispatcher("../vote.jsp").forward(request, response);
			} else {
				Map<String, Candidat> candidats = (Map<String, Candidat>) request.getServletContext().getAttribute("candidats");
				Map<String, Ballot> ballots = (Map<String, Ballot>) request.getServletContext().getAttribute("ballots");
				List<Bulletin> bulletins = (List<Bulletin>) request.getServletContext().getAttribute("bulletins");
				HttpSession session = request.getSession(true);
				User utilisateur = (User) session.getAttribute("user");

				String nomCandidat = request.getParameter("candidat");
				Candidat candidat = candidats.get(nomCandidat);
				Bulletin bulletin = new Bulletin(candidat);
				bulletins.add(bulletin);
				Ballot ballot = new Ballot(bulletin);
				ballots.put(utilisateur.getLogin(), ballot);

				request.setAttribute("bulletins", bulletins);
				request.setAttribute("ballots", ballots);
				request.getRequestDispatcher("../ballot.jsp").forward(request, response);
			}
		} else if (url.endsWith("user")) { // OK
			HttpSession session = request.getSession(true);
			User utilisateur = (User) session.getAttribute("user");
			String name = request.getParameter("name");
			utilisateur.setNom(name);
			request.getRequestDispatcher("../profile.jsp").forward(request, response);
		} else if (url.endsWith("resultats")) { // OK
			request.getRequestDispatcher("../resultats").forward(request, response);
		} else if (url.endsWith("listBallots")) { // OK
			request.getRequestDispatcher("../listBallots.jsp").forward(request, response);
		}
	}
}
