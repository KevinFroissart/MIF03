package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.dto.VoteByUserDTO;
import fr.univlyon1.m1if.m1if03.classes.model.Ballot;
import fr.univlyon1.m1if.m1if03.classes.model.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.model.Candidat;
import fr.univlyon1.m1if.m1if03.classes.model.User;
import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;
import fr.univlyon1.m1if.m1if03.utils.ElectionM1if03JwtHelper;

@WebServlet(name = "ControllerVote", value = {})
public class ControllerVote extends HttpServlet {

    private List<String> uri;
    private Map<String, User> users;
    private Map<User, Integer> usersId;
    private List<Bulletin> bulletins = null;
    private Map<String, Ballot> ballots = null;
    private Map<Integer, Ballot> ballotsId = null;
    private Map<String, Candidat> candidats = null;
    private int compteur;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
        this.usersId = (Map<User, Integer>) config.getServletContext().getAttribute("usersId");
        this.bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
        this.ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
        this.ballotsId = (Map<Integer, Ballot>) config.getServletContext().getAttribute("ballotsId");
        this.candidats = (Map<String, Candidat>) config.getServletContext().getAttribute("candidats");
        this.compteur = (int) config.getServletContext().getAttribute("compteur");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        /** /election/votes/{userId} **/
        if (uri.size() == 3) {
            Integer id = Integer.parseInt(uri.get(2));
            String token = (String) request.getAttribute("token");
            User user = null;

            for (Map.Entry<User, Integer> userEntry : usersId.entrySet()) {
                if (userEntry.getValue().equals(id)) user = userEntry.getKey();
            }

            if (user == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Utilisateur ou vote non trouvé.");
                return;
            }

            if (!ElectionM1if03JwtHelper.verifyToken(token, request).equals(user.getLogin()) && !ElectionM1if03JwtHelper.verifyAdmin(token)) {
                request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorMessage", "Utilisateur non administrateur ou non propriétaire du vote.");
                return;
            }
            request.setAttribute("DTO", new ObjectMapper().createObjectNode());
        }

        /** /election/votes/byUser/{userId} **/
        else if (uri.size() == 4) {
            String login = uri.get(3).replaceAll("%20", " ");
            String token = (String) request.getAttribute("token");
            User user = users.get(login);

            if (user == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Utilisateur ou vote non trouvé.");
                return;
            }

            if (!ElectionM1if03JwtHelper.verifyToken(token, request).equals(login) && !ElectionM1if03JwtHelper.verifyAdmin(token)) {
                request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorMessage", "Utilisateur non administrateur ou non propriétaire du vote.");
                return;
            }

            Integer id = usersId.get(user);

            if (id == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Utilisateur ou vote non trouvé.");
                return;
            }

            VoteByUserDTO voteByUserDTO = new VoteByUserDTO(usersId.get(user), request.getRequestURL().toString().substring(0, request.getRequestURL().toString().indexOf("/byUser")));
            request.setAttribute("DTO", voteByUserDTO);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        /** /election/votes/byUser/{userId} **/
        if (uri.size() == 4) {
            String login = uri.get(3).replaceAll("%20", " ");
            String token = (String) request.getAttribute("token");

            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

            String nom = br.readLine().replaceAll("nomCandidat=", "").replaceAll("%20", " ");

            if (nom == null || login == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
                request.setAttribute("errorMessage", "Paramètres de la requête non acceptables.");
                return;
            }

            User user = users.get(login);
            Ballot ballot = ballots.get(login);
            Integer id = usersId.get(user);

            if (user == null || ballot == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Utilisateur ou vote non trouvé.");
                return;
            }

            if (candidats.get(nom) == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Candidat non trouvé.");
                return;
            }

            if (!ElectionM1if03JwtHelper.verifyToken(token, request).equals(user.getLogin()) && !ElectionM1if03JwtHelper.verifyAdmin(token)) {
                request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorMessage", "Utilisateur non administrateur ou non propriétaire du vote.");
                return;
            }

            compteur++;

            bulletins.remove(ballot.getBulletin());
            ballots.remove(login);
            ballotsId.remove(id);
            usersId.remove(user);

            Candidat candidat = candidats.get(nom);
            Bulletin bulletin = new Bulletin(candidat);
            bulletins.add(bulletin);
            ballot = new Ballot(bulletin);
            ballots.put(login, ballot);
            ballotsId.put(compteur, ballot);
            usersId.put(user, compteur);

            request.setAttribute("statusCode", HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
