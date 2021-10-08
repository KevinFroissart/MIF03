package fr.univlyon1.m1if.m1if03.classes;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Vote", value = "/castVote")
public class Vote extends HttpServlet {

	Map<String, Ballot> ballots = null;
	List<Bulletin> bulletins = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = config.getServletContext();
		ballots = (Map<String, Ballot>) context.getAttribute("ballots");
		bulletins = (List<Bulletin>) context.getAttribute("bulletins");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		User utilisateur = (User) session.getAttribute("user");
		if(utilisateur != null) {
			request.setCharacterEncoding("UTF-8");
			Map<String, Candidat> candidats = (Map<String, Candidat>) request.getServletContext().getAttribute("candidats");
			String nomCandidat = request.getParameter("candidat");

			Candidat candidat = candidats.get(nomCandidat);
			Bulletin bulletin = new Bulletin(candidat);
			bulletins.add(bulletin);
			Ballot ballot = new Ballot(bulletin);
			ballots.put(utilisateur.getLogin(), ballot);

			request.setAttribute("bulletins", bulletins);
			request.setAttribute("ballots", ballots);
			request.getRequestDispatcher("ballot.jsp").forward(request, response);
		} else {
			response.sendRedirect("index.html");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect("index.html");
	}
}
