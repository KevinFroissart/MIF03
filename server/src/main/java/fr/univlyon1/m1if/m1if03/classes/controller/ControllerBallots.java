package fr.univlyon1.m1if.m1if03.classes.controller;

import fr.univlyon1.m1if.m1if03.classes.dto.BallotByIdDTO;
import fr.univlyon1.m1if.m1if03.classes.dto.BallotByUserDTO;
import fr.univlyon1.m1if.m1if03.classes.dto.BallotsDTO;
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
import java.util.List;
import java.util.Map;

@WebServlet(name = "ControllerBallots", value = {})
public class ControllerBallots extends HttpServlet {

    private List<String> uri;
    private List<Bulletin> bulletins = null;
    private Map<String, Ballot> ballots = null;
    private Map<Integer, Ballot> ballotsId = null;
    private Map<String, Candidat> candidats = null;
    private Map<User, Integer> usersId;
    private Map<String, User> users;
    private int compteur;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
        this.ballotsId = (Map<Integer, Ballot>) config.getServletContext().getAttribute("ballotsId");
        this.bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
        this.candidats = (Map<String, Candidat>) config.getServletContext().getAttribute("candidats");
        this.usersId = (Map<User, Integer>) config.getServletContext().getAttribute("usersId");
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
        this.compteur = (int) config.getServletContext().getAttribute("compteur");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        /** /election/ballots **/
        if (uri.size() == 2) {
            String token = request.getAttribute("token").toString();
            if (!ElectionM1if03JwtHelper.verifyAdmin(token)) {
                request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorMessage", "Utilisateur non administrateur.");
                return;
            }
            BallotsDTO ballotsDTO = new BallotsDTO(ballotsId, request.getRequestURL().toString());
            request.setAttribute("DTO", ballotsDTO);
            request.setAttribute("vue", "listBallots.jsp");
        }

        /** /election/ballots/{ballotId} **/
        else if (uri.size() == 3) {
            Integer id = Integer.parseInt(uri.get(2));
            String token = request.getAttribute("token").toString();
            User user = null;

            for (Map.Entry<User, Integer> userEntry : usersId.entrySet()) {
                if (userEntry.getValue().equals(id)) user = userEntry.getKey();
            }

            if (user == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Ballot non trouvé.");
                return;
            }

            if (!ElectionM1if03JwtHelper.verifyAdmin(token) && !ElectionM1if03JwtHelper.verifyToken(token, request).equals(user.getLogin())) {
                request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorMessage", "Utilisateur non administrateur ou non propriétaire du ballot.");
                return;
            }

            if (ballotsId.get(id) == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Ballot non trouvé.");
                return;
            }

            BallotByIdDTO ballotByIdDTO = new BallotByIdDTO(id, request.getRequestURL().toString().substring(0, request.getRequestURL().toString().indexOf("/ballots")));
            request.setAttribute("DTO", ballotByIdDTO);
        }

        /** /election/ballots/byUser/{userId} **/
        else if (uri.size() == 4) {
            String login = uri.get(3).replaceAll("%20", " ");
            String token = request.getAttribute("token").toString();
            User user = users.get(login);


            if (!ElectionM1if03JwtHelper.verifyAdmin(token) && !ElectionM1if03JwtHelper.verifyToken(token, request).equals(login)) {
                request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorMessage", "Utilisateur non administrateur ou non propriétaire du ballot.");
                return;
            }

            if (ballots.get(login) == null || user == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Ballot non trouvé.");
                return;
            }

            Integer id = usersId.get(user);

            BallotByUserDTO
                    ballotByUserDTO = new BallotByUserDTO(id, request.getRequestURL().toString().substring(0, request.getRequestURL().toString().indexOf("/byUser")));
            request.setAttribute("DTO", ballotByUserDTO);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        /** /election/ballots **/
        if (uri.size() == 2) {
            Candidat candidat = candidats.get(request.getParameter("nomCandidat"));

            if (candidat == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
                request.setAttribute("errorMessage", "Paramètres de la requête non acceptables.");
                return;
            }

            String token = request.getAttribute("token").toString();
            String login = ElectionM1if03JwtHelper.verifyToken(token, request);

            if (ballots.get(login) != null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
                request.setAttribute("errorMessage", "Un vote existe déjà pour cet utilisateur.");
                return;
            }

            compteur++;

            Bulletin bulletin = new Bulletin(candidat);
            bulletins.add(bulletin);
            Ballot ballot = new Ballot(bulletin);
            ballots.put(login, ballot);
            ballotsId.put(compteur, ballot);
            usersId.put(users.get(login), compteur);

            request.setAttribute("statusCode", HttpServletResponse.SC_CREATED);
        } else {
            request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("errorMessage", "Requête non reconnue.");
            return;
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        /** /election/ballots/{ballotId} **/
        if (uri.size() == 3) {
            Integer id = Integer.parseInt(uri.get(2));
            String token = request.getAttribute("token").toString();

            User user = null;

            for (Map.Entry<User, Integer> userEntry : usersId.entrySet()) {
                if (userEntry.getValue().equals(id)) user = userEntry.getKey();
            }

            if (user == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Ballot non trouvé.");
                return;
            }

            if (!ElectionM1if03JwtHelper.verifyAdmin(token) && !ElectionM1if03JwtHelper.verifyToken(token, request).equals(user.getLogin())) {
                request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorMessage", "Utilisateur non administrateur ou non propriétaire du ballot.");
                return;
            }

            if (ballotsId.get(id) == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Ballot non trouvé.");
                return;
            }

            ballots.remove(user.getLogin());
            ballotsId.remove(id);
            usersId.remove(user);

            request.setAttribute("statusCode", HttpServletResponse.SC_NO_CONTENT);
        } else {
            request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("errorMessage", "Requête non reconnue.");
            return;
        }
    }
}
