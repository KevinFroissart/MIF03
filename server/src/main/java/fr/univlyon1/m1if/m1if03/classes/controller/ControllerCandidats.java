package fr.univlyon1.m1if.m1if03.classes.controller;

import fr.univlyon1.m1if.m1if03.classes.dto.CandidatDTO;
import fr.univlyon1.m1if.m1if03.classes.dto.CandidatsDTO;
import fr.univlyon1.m1if.m1if03.classes.dto.CandidatsNomsDTO;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        /** /election/candidats **/
        if (uri.size() == 2) {
            CandidatsDTO candidatsDTO = new CandidatsDTO(candidats.values(), request.getRequestURL().toString());
            request.setAttribute("DTO", candidatsDTO);
            request.setAttribute("statusCode", HttpServletResponse.SC_OK);
        } else if (uri.size() == 3) {

            /** /election/candidats/nom **/
            if (uri.get(2).equals("noms")) {
                CandidatsNomsDTO candidatsNomsDTO = new CandidatsNomsDTO(candidats.values().stream().collect(Collectors.toList()));
                request.setAttribute("DTO", candidatsNomsDTO);
                request.setAttribute("statusCode", HttpServletResponse.SC_OK);
            }

            /** /election/candidats/{candidatId} **/
            else if (uri.get(1).equals("candidats")) {
                String nom = uri.get(2).replaceAll("%20", " ");

                Candidat candidat = candidats.get(nom);

                if (candidat == null) {
                    request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                    request.setAttribute("errorMessage", "Candidat non trouvé.");
                    return;
                }

                CandidatDTO candidatDTO = new CandidatDTO(candidat);
                request.setAttribute("DTO", candidatDTO);
                request.setAttribute("statusCode", HttpServletResponse.SC_OK);
            } else {
                request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
                request.setAttribute("errorMessage", "Requête non reconnue.");
                return;
            }
        } else {
            request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("errorMessage", "Requête non reconnue.");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        /** /election/candidats/update **/
        if (uri.get(2).equals("update")) {
            try {
                candidats = CandidatListGenerator.getCandidatList();
                this.getServletContext().setAttribute("candidats", candidats);
                request.setAttribute("statucCode", HttpServletResponse.SC_NO_CONTENT);
                request.setAttribute("successMessage", "Liste mise à jour.");
                return;
            } catch (Exception e) {
                request.setAttribute("errorCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                request.setAttribute("errorMessage", "Erreur lors du chargement de la liste.");
                return;
            }
        } else {
            request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("errorMessage", "Requête non reconnue.");
            return;
        }
    }
}
