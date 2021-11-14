package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.univlyon1.m1if.m1if03.classes.model.Ballot;
import fr.univlyon1.m1if.m1if03.classes.model.User;
import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;
import fr.univlyon1.m1if.m1if03.utils.JWTHelper;

@WebServlet(name = "ControllerUser", value = {})
public class ControllerUser extends HttpServlet {

    Map<String, User> users = null;
    Map<String, User> usersId = null;
    Map<String, Ballot> ballots = null;
    List<String> uri;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
        this.usersId = (Map<String, User>) config.getServletContext().getAttribute("usersId");
        this.ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        // /users
        if (uri.size() == 1) {
            List<String> urls = new ArrayList<>();
            for (User user : users.values()) {
                urls.add(request.getRequestURL() + "/" + user.getNom());
            }
            APIResponseUtils.buildJson(response, urls);
        }

        // /users/{userId}
        else if (uri.size() == 2) {

            String login = uri.get(1).replaceAll("%20", " ");
            String token = (String) request.getAttribute("token");

            if (!JWTHelper.verifyToken(token).equals(login) && !JWTHelper.verifyAdmin(token)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou pas celui qui est logué.");
                return;
            }

            User user = users.get(login);
            if (user == null) response.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur non trouvé.");
            else APIResponseUtils.buildJson(response, user);

        } else if (uri.size() == 3) {

            // /users/{userId}/vote
            if (uri.get(2).equals("vote")) {
                String login = uri.get(1).replaceAll("%20", " ");
                String token = (String) request.getAttribute("token");

                if (!JWTHelper.verifyToken(token).equals(login) && !JWTHelper.verifyAdmin(token)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou non propriétaire du vote.");
                    return;
                }

                if (ballots.get(login) == null || users.get(login) == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Vote ou utilisateur non trouvé.");
                    return;
                }

                int i = 0;
                for (String utilisateur : ballots.keySet()) {
                    if (utilisateur.equals(login)) {
                        response.sendRedirect(request.getRequestURL().toString().replaceAll("/users/" + uri.get(1) + "/vote", "/election/vote/" + i));
                        return;
                    }
                    i++;
                }
            }

            // /users/{userId}/ballot
            else if (uri.get(2).equals("ballot")) {
                String login = uri.get(1).replaceAll("%20", " ");
                String token = (String) request.getAttribute("token");

                if (!JWTHelper.verifyToken(token).equals(login) && !JWTHelper.verifyAdmin(token)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou non propriétaire du ballot.");
                    return;
                }

                if (ballots.get(login) == null || users.get(login) == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Vote ou utilisateur non trouvé.");
                    return;
                }

                int i = 0;
                for (String utilisateur : ballots.keySet()) {
                    if (utilisateur.equals(login)) {
                        response.sendRedirect(request.getRequestURL().toString().replaceAll("/users/" + uri.get(1) + "/ballot", "/election/ballots/" + i));
                        return;
                    }
                    i++;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "requête non reconnue");
                return;
            }

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "requête non reconnue");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        // /users/login
        if (uri.get(1).equals("login")) {
            if (request.getParameter("login") == null || request.getParameter("nom") == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requête non acceptables");
                return;
            }

            String login = request.getParameter("login");
            String nom = request.getParameter("nom");
            boolean admin = request.getParameter("admin").equals("true");

            User user = new User(login, nom, admin);

            String token = JWTHelper.generateToken(user.getLogin(), user.isAdmin());
            response.setHeader("Authorization", "Bearer " + token);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            request.setAttribute("user", user);

            String uuid = UUID.nameUUIDFromBytes(login.getBytes()).toString();

            users.put(login, user);
            usersId.put(uuid, user);
        }

        // /users/logout
        else if (uri.get(1).equals("logout")) {
            HttpSession session = request.getSession(false);
            if (request.getAttribute("user") == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
                return;
            }
            request.removeAttribute("token");
            session.invalidate();
            response.sendRedirect("index.html");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        // /users/{usersId}/nom
        if (uri.get(2).equals("nom")) {

            String login = uri.get(1).replaceAll("%20", " ");
            String token = (String) request.getAttribute("token");

            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

            String nom = br.readLine().replaceAll("nom=", "").replaceAll("%20", " ");

            if (nom == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requête non acceptables.");
                return;
            }

            if (!JWTHelper.verifyToken(token).equals(login) && !JWTHelper.verifyAdmin(token)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou pas celui qui est logué.");
                return;
            }

            User user = users.get(login);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur non trouvé.");
                return;
            }

            user.setNom(nom);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT, "User correctement modifié.");

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Requête non reconnue");
            return;
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/components/profil.jsp").forward(request, response);
    }
}
