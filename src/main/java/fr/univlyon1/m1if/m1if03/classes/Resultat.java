package fr.univlyon1.m1if.m1if03.classes;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Resultat", value = "/resultat")
public class Resultat extends HttpServlet {

    Map<String, Integer> votes = null;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<String, Candidat> candidats = (Map<String, Candidat>) request.getServletContext().getAttribute("candidats");
            if(votes == null) {
                votes = new HashMap<>();
                for (String nomCandidat : candidats.keySet()) {
                    votes.put(nomCandidat, 0);
                }
                for (Bulletin bulletin : bulletins) {
                    int score = votes.get(bulletin.getCandidat().getNom());
                    votes.put(bulletin.getCandidat().getNom(), ++score);
                }
            }
            System.out.println(votes.toString());
            request.getServletContext().setAttribute("votes", votes);
            request.getRequestDispatcher("resultats.jsp").forward(request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
