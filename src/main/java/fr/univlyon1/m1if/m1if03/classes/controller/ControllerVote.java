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
import java.util.UUID;

import fr.univlyon1.m1if.m1if03.classes.model.Ballot;
import fr.univlyon1.m1if.m1if03.classes.model.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.model.Candidat;
import fr.univlyon1.m1if.m1if03.classes.model.User;
import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;
import fr.univlyon1.m1if.m1if03.utils.ElectionM1if03JwtHelper;

@WebServlet(name = "ControllerVote", value = {})
public class ControllerVote extends HttpServlet {

    Map<String, User> users;
    Map<String, User> usersId;
    List<Bulletin> bulletins = null;
    Map<String, Ballot> ballots = null;
    Map<String, Ballot> ballotsId = null;
    Map<String, Candidat> candidats = null;
    List<String> uri;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
        this.usersId = (Map<String, User>) config.getServletContext().getAttribute("usersId");
        this.bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
        this.ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
        this.ballotsId = (Map<String, Ballot>) config.getServletContext().getAttribute("ballotsId");
        this.candidats = (Map<String, Candidat>) config.getServletContext().getAttribute("candidats");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        // /election/votes/{userId}
        if (uri.size() == 3) {
            String uuid = uri.get(2);
            User user = usersId.get(uuid);
            String token = (String) request.getAttribute("token");

            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur ou vote non trouvé.");
                return;
            }

            if (!ElectionM1if03JwtHelper.verifyToken(token, request).equals(user.getLogin()) && !ElectionM1if03JwtHelper.verifyAdmin(token)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou non propriétaire du vote.");
                return;
            }
            request.getRequestDispatcher("/election/ballots/" + uuid).forward(request, response);
        }
        // /election/votes/byUser/{userId}
        else if (uri.size() == 4) {
            String login = uri.get(3).replaceAll("%20", " ");
            String token = (String) request.getAttribute("token");
            User user = users.get(login);

            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur ou vote non trouvé.");
                return;
            }

            if (!ElectionM1if03JwtHelper.verifyToken(token, request).equals(login) && !ElectionM1if03JwtHelper.verifyAdmin(token)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou non propriétaire du vote.");
                return;
            }
            request.getRequestDispatcher("/election/ballots/byUser/" + login).forward(request, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        // /election/votes/byUser/{userId}
        if (uri.size() == 4) {
            String login = uri.get(3).replaceAll("%20", " ");
            String uuid = UUID.nameUUIDFromBytes(login.getBytes()).toString();
            String token = (String) request.getAttribute("token");
            User user = users.get(login);
            Ballot ballot = ballots.get(login);

            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

            String nom = br.readLine().replaceAll("nom=", "").replaceAll("%20", " ");

            if (nom == null || login == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requête non acceptables.");
                return;
            }

            if (user == null || ballot == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Utilisateur ou vote non trouvé.");
                return;
            }

            if (candidats.get(nom) == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Candidat non trouvé.");
                return;
            }

            if (!ElectionM1if03JwtHelper.verifyToken(token, request).equals(user.getLogin()) && !ElectionM1if03JwtHelper.verifyAdmin(token)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou non propriétaire du vote.");
                return;
            }

            bulletins.remove(ballot.getBulletin());
            ballots.remove(login);
            ballotsId.remove(uuid);

            Candidat candidat = candidats.get(nom);
            Bulletin bulletin = new Bulletin(candidat);
            bulletins.add(bulletin);
            ballot = new Ballot(bulletin);
            ballots.put(login, ballot);
            ballotsId.put(uuid, ballot);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
