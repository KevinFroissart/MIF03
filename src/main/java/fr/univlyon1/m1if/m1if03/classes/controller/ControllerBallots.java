package fr.univlyon1.m1if.m1if03.classes.controller;

import fr.univlyon1.m1if.m1if03.classes.model.Ballot;
import fr.univlyon1.m1if.m1if03.classes.model.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.model.Candidat;
import fr.univlyon1.m1if.m1if03.classes.model.User;
import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;
import fr.univlyon1.m1if.m1if03.utils.ElectionM1if03JwtHelper;

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
import java.util.UUID;

@WebServlet(name = "ControllerBallots", value = {})
public class ControllerBallots extends HttpServlet {

    private List<String> uri;
    List<Bulletin> bulletins = null;
    Map<String, Ballot> ballots = null;
    Map<String, Ballot> ballotsId = null;
    Map<String, Candidat> candidats = null;
    Map<String, User> usersId = null;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
        this.ballotsId = (Map<String, Ballot>) config.getServletContext().getAttribute("ballotsId");
        this.bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
        this.candidats = (Map<String, Candidat>) config.getServletContext().getAttribute("candidats");
        this.usersId = (Map<String, User>) config.getServletContext().getAttribute("usersId");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        // /election/ballots
        if (uri.size() == 2) {
            String token = request.getHeader("Authorization");
            if (!ElectionM1if03JwtHelper.verifyAdmin(token)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur.");
                return;
            }

            List<String> urls = new ArrayList<>();
            for (int i = 0; i < ballots.size(); i++) {
                urls.add(request.getRequestURL().toString() + "/" + i);
            }
            APIResponseUtils.buildJson(response, urls);
        }
        // /election/ballots/{ballotId}
        else if (uri.size() == 3) {
            String uuid = uri.get(2);
            User user = usersId.get(uuid);
            String token = request.getHeader("Authorization");

            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ballot non trouvé.");
                return;
            }

            if (!ElectionM1if03JwtHelper.verifyAdmin(token) && !ElectionM1if03JwtHelper.verifyToken(token, request).equals(user.getLogin())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou non propriétaire du ballot.");
                return;
            }

            if (ballotsId.get(uuid) == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ballot non trouvé.");
                return;
            }

            int i = 0;
            for (String utilisateur : ballots.keySet()) {
                if (utilisateur.equals(user.getLogin())) {
                    String url = request.getRequestURL().toString().replace("ballots/" + uri.get(2), "vote/" + i);
                    APIResponseUtils.buildJson(response, Arrays.asList(url));
                    return;
                }
                i++;
            }
        }
        // /election/ballots/byUser/{userId}
        else if (uri.size() == 4) {
            String login = uri.get(3).replaceAll("%20", " ");

            String token = request.getHeader("Authorization");
            if (!ElectionM1if03JwtHelper.verifyAdmin(token) && !ElectionM1if03JwtHelper.verifyToken(token, request).equals(login)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou non propriétaire du ballot.");
                return;
            }

            if (ballots.get(login) == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ballot non trouvé.");
                return;
            }

            int i = 0;
            for (String user : ballots.keySet()) {
                if (user.equals(login)) {
                    String url = request.getRequestURL().toString().replace("ballots/byUser/" + uri.get(3), "vote/" + i);
                    APIResponseUtils.buildJson(response, Arrays.asList(url));
                    return;
                }
                i++;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        //election/ballots
        if (uri.size() == 2) {
            Candidat candidat = candidats.get(request.getParameter("nomCandidat"));

            if (candidat == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requête non acceptables");
                return;
            }

            String token = request.getHeader("Authorization");
            String login = ElectionM1if03JwtHelper.verifyToken(token, request);
            String uuid = UUID.nameUUIDFromBytes(login.getBytes()).toString();

            Bulletin bulletin = new Bulletin(candidat);
            bulletins.add(bulletin);
            Ballot ballot = new Ballot(bulletin);
            ballots.put(login, ballot);
            ballotsId.put(uuid, ballot);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Requête non reconnue");
            return;
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        //election/ballots/{ballotId}
        if (uri.size() == 3) {
            String uuid = uri.get(2);
            User user = usersId.get(uuid);
            String token = request.getHeader("Authorization");

            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ballot non trouvé.");
                return;
            }

            if (!ElectionM1if03JwtHelper.verifyAdmin(token) && !ElectionM1if03JwtHelper.verifyToken(token, request).equals(user.getLogin())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou non propriétaire du ballot.");
                return;
            }

            if (ballotsId.get(uuid) == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ballot non trouvé.");
                return;
            }

            ballots.remove(user.getLogin());
            ballotsId.remove(uuid);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Requête non reconnue");
            return;
        }
    }
}
