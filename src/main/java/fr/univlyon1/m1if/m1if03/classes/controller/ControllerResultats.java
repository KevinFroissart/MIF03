package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
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

import fr.univlyon1.m1if.m1if03.classes.model.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.model.Candidat;
import fr.univlyon1.m1if.m1if03.classes.dto.VotesDTO;
import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;

@WebServlet(name = "ControllerResultats", value = {})
public class ControllerResultats extends HttpServlet {

    List<Bulletin> bulletins = null;
    Map<String, Candidat> candidats = null;

    private List<String> uri;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
        this.candidats = (Map<String, Candidat>) config.getServletContext().getAttribute("candidats");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        Map<String, Integer> votes = new LinkedHashMap<>();
        for (String nomCandidat : candidats.keySet()) {
            votes.put(nomCandidat, 0);
        }
        for (Bulletin bulletin : bulletins) {
            int score = votes.get(bulletin.getCandidat().getNom());
            votes.put(bulletin.getCandidat().getNom(), ++score);
        }

        List<VotesDTO> election = new ArrayList<>();

        for (Map.Entry<String, Integer> vote : votes.entrySet()) {
            election.add(new VotesDTO(vote.getKey(), vote.getValue()));
        }

        request.setAttribute("DTO", election);
    }

}
