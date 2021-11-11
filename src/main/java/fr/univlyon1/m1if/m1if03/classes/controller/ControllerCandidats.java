package fr.univlyon1.m1if.m1if03.classes.controller;

import fr.univlyon1.m1if.m1if03.classes.model.Candidat;
import fr.univlyon1.m1if.m1if03.classes.model.User;
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

        if(uri.size() == 2) {
            // /election/candidats
            List<Candidat> candidatsList = new ArrayList<>(candidats.values());
            APIResponseUtils.buildJson(response, candidatsList);
        }

        else if(uri.size() == 3) {

            if(uri.get(2).equals("noms")) {
                // /election/candidats/nom
                List<String> candidatsList = new ArrayList<>();
                for(Candidat candidat : candidats.values()){
                    candidatsList.add(candidat.getNom());
                }
                APIResponseUtils.buildJson(response, candidatsList);
            }

            else if(uri.get(1).equals("candidats")) {
                // /election/candidats/{candidatId}
               // TODO: implémenter
            }

            else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Requête non reconnue");
                return;
            }
        }

        else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Requête non reconnue");
            return;
        }

//        if(uri.endsWith("candidats")) CandidatsService.getCandidats(); // /election/candidats
//        else if(uri.endsWith("noms")) CandidatsService.getNomCandidats(); // /election/candidats/nom
//        else if(uri.contains("/candidats/")) CandidatsService.getCandidat(Integer.parseInt(uri.substring(uri.lastIndexOf('/') + 1))); // /election/candidats/{candidatId}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        // /election/candidats/update
        if(uri.get(2).equals("update")) {
            User utilisateur = (User) request.getSession(false).getAttribute("user");
            if(!utilisateur.isAdmin()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur");
                return;
            }
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
