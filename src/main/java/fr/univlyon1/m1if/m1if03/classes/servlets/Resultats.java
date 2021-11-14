package fr.univlyon1.m1if.m1if03.classes.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.univlyon1.m1if.m1if03.classes.model.Ballot;
import fr.univlyon1.m1if.m1if03.classes.model.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.model.Candidat;
import fr.univlyon1.m1if.m1if03.classes.model.ElectionJSON;
import fr.univlyon1.m1if.m1if03.classes.model.VotesJSON;
import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;

@WebServlet(name = "Resultats", value = {})
public class Resultats extends HttpServlet {

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
        Map<String, Candidat> candidats = (Map<String, Candidat>) request.getServletContext().getAttribute("candidats");
        Map<String, Integer> votes = new LinkedHashMap<>();
        for (String nomCandidat : candidats.keySet()) {
            votes.put(nomCandidat, 0);
        }
        for (Bulletin bulletin : bulletins) {
            int score = votes.get(bulletin.getCandidat().getNom());
            votes.put(bulletin.getCandidat().getNom(), ++score);
        }
        request.setAttribute("votes", votes);

        List<VotesJSON> election = new ArrayList<>();

        // /election/resultats
        for (Map.Entry<String, Integer> vote : votes.entrySet()) {
            election.add(new VotesJSON(vote.getKey(), vote.getValue()));
        }

        APIResponseUtils.buildJson(response, new ElectionJSON(election));
    }
}
