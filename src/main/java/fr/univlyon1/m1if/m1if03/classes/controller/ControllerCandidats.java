package fr.univlyon1.m1if.m1if03.classes.controller;

import fr.univlyon1.m1if.m1if03.classes.model.Candidat;
import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;
import fr.univlyon1.m1if.m1if03.utils.CandidatListGenerator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ControllerCandidats", value = {})
public class ControllerCandidats extends HttpServlet {

    private Map<String, Candidat> candidats;
    private List<String> uri;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.candidats = (Map<String, Candidat>) config.getServletContext().getAttribute("candidats");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        // /election/candidats
        if (uri.size() == 2) {
            List<String> urls = new ArrayList<>();
            for (int i = 0; i < candidats.size(); i++) {
                urls.add(request.getRequestURL() + "/" + i);
            }
            APIResponseUtils.buildJson(response, urls);
        } else if (uri.size() == 3) {

            // /election/candidats/nom
            if (uri.get(2).equals("noms")) {
                List<String> candidatsList = new ArrayList<>();
                for (Candidat candidat : candidats.values()) {
                    candidatsList.add(candidat.getNom());
                }
                request.setAttribute("DTO", candidatsList);
//                APIResponseUtils.buildJson(response, candidatsList);
            }

            // /election/candidats/{candidatId}
            else if (uri.get(1).equals("candidats")) {
                String nom = uri.get(2).replaceAll("%20", " ");

                Candidat candidat = candidats.get(nom);

                if (candidat == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Candidat non trouvé.");
                    return;
                }
                APIResponseUtils.buildJson(response, Arrays.asList(candidat.getPrenom() + " " + candidat.getNom()));
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Requête non reconnue");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Requête non reconnue");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        // /election/candidats/update
        if (uri.get(2).equals("update")) {
            try {
                candidats = CandidatListGenerator.getCandidatList();
                this.getServletContext().setAttribute("candidats", candidats);
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "Liste mise à jour");
                return;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors du chargement de la liste");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Requête non reconnue");
            return;
        }
    }
}
