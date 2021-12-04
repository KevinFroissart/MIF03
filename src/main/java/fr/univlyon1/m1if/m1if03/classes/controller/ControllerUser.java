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
import java.util.stream.Collectors;

import fr.univlyon1.m1if.m1if03.classes.dto.UserDTO;
import fr.univlyon1.m1if.m1if03.classes.dto.UsersDTO;
import fr.univlyon1.m1if.m1if03.classes.model.Ballot;
import fr.univlyon1.m1if.m1if03.classes.model.User;
import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;
import fr.univlyon1.m1if.m1if03.utils.ElectionM1if03JwtHelper;

@WebServlet(name = "ControllerUser", value = {})
public class ControllerUser extends HttpServlet {

    private List<String> expiredTokens = null;

    private Map<String, User> users = null;
    private Map<User, Integer> usersId = null;
    private Map<String, Ballot> ballots = null;
    private List<String> uri;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
        this.usersId = (Map<User, Integer>) config.getServletContext().getAttribute("usersId");
        this.ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
        this.expiredTokens = (List<String>) config.getServletContext().getAttribute("expiredTokens");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        /** /users **/
        if (uri.size() == 1) {
            UsersDTO usersDTO = new UsersDTO(users.values().stream().collect(Collectors.toList()), request.getRequestURL().toString());
            request.setAttribute("DTO", usersDTO);
            request.setAttribute("statusCode", HttpServletResponse.SC_OK);
        }

        /** /users/{userId} **/
        else if (uri.size() == 2) {

            String login = uri.get(1).replaceAll("%20", " ");
            String token = (String) request.getAttribute("token");

            if (!ElectionM1if03JwtHelper.verifyToken(token, request).equals(login) && !ElectionM1if03JwtHelper.verifyAdmin(token)) {
                request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorMessage", "Utilisateur non administrateur ou pas celui qui est logué.");
                return;
            }

            User user = users.get(login);

            if (user == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Utilisateur non trouvé.");
            } else {
                UserDTO userDTO = new UserDTO(user);
                request.setAttribute("DTO", userDTO);
            }

        } else if (uri.size() == 3) {

            /** /users/{userId}/vote **/
            if (uri.get(2).equals("vote")) {
                String login = uri.get(1).replaceAll("%20", " ");
                String token = (String) request.getAttribute("token");
                User user = users.get(login);

                if (!ElectionM1if03JwtHelper.verifyToken(token, request).equals(login) && !ElectionM1if03JwtHelper.verifyAdmin(token)) {
                    request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                    request.setAttribute("errorMessage", "Utilisateur non administrateur ou non propriétaire du vote.");
                    return;
                }

                if (ballots.get(login) == null || user == null) {
                    request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                    request.setAttribute("errorMessage", "Vote ou utilisateur non trouvé.");
                    return;
                }

                Integer id = usersId.get(user);

                response.sendRedirect(request.getRequestURL().toString().replaceAll("/users/" + uri.get(1) + "/vote", "/election/vote/" + id));
            }

            /** /users/{userId}/ballot **/
            else if (uri.get(2).equals("ballot")) {
                String login = uri.get(1).replaceAll("%20", " ");
                String token = (String) request.getAttribute("token");
                User user = users.get(login);

                if (!ElectionM1if03JwtHelper.verifyToken(token, request).equals(login) && !ElectionM1if03JwtHelper.verifyAdmin(token)) {
                    request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                    request.setAttribute("errorMessage", "Utilisateur non administrateur ou non propriétaire du ballot.");
                    return;
                }

                if (ballots.get(login) == null || user == null) {
                    request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                    request.setAttribute("errorMessage", "Vote ou utilisateur non trouvé.");
                    return;
                }

                Integer id = usersId.get(user);

                response.sendRedirect(request.getRequestURL().toString().replaceAll("/users/" + uri.get(1) + "/ballot", "/election/ballots/" + id));
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        /** /users/login **/
        if (uri.get(1).equals("login")) {
            if (request.getParameter("login") == null || request.getParameter("nom") == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
                request.setAttribute("errorMessage", "Paramètres de la requête non acceptables.");
                return;
            }

            String login = request.getParameter("login");
            String nom = request.getParameter("nom");
            boolean admin = request.getParameter("admin") == null ? false : request.getParameter("admin").equals(true);

            User user = new User(login, nom, admin);

            String token = ElectionM1if03JwtHelper.generateToken(user.getLogin(), user.isAdmin(), request);
            request.setAttribute("Authorization", "Bearer " + token);
            request.setAttribute("statusCode", HttpServletResponse.SC_NO_CONTENT);
            request.setAttribute("token", token);
            request.setAttribute("user", user);
            request.setAttribute("vue", "ballot.jsp");
            users.put(login, user);
        }

        /** /users/logout **/
        else if (uri.get(1).equals("logout")) {
            String token = request.getHeader("Authorization");

            if (token == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_UNAUTHORIZED);
                request.setAttribute("errorMessage", "Utilisateur non authentifié.");
                return;
            }

            try {
                ElectionM1if03JwtHelper.verifyToken(token, request);
            } catch (Exception e) {
                request.setAttribute("errorCode", HttpServletResponse.SC_UNAUTHORIZED);
                request.setAttribute("errorMessage", "Utilisateur non authentifié.");
                return;
            }

            expiredTokens.add(token);
            request.setAttribute("expiredTokens", expiredTokens);
            response.sendRedirect("../index.html");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        uri = APIResponseUtils.splitUri(request.getRequestURI());

        /** /users/{usersId}/nom **/
        if (uri.get(2).equals("nom")) {
            String login = uri.get(1).replaceAll("%20", " ");
            String token = (String) request.getAttribute("token");

            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

            String nom = br.readLine();

            if (nom == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
                request.setAttribute("errorMessage", "Paramètres de la requête non acceptables.");
                return;
            }

            nom = nom.replaceAll("nom=", "").replaceAll("%20", " ");

            if (!ElectionM1if03JwtHelper.verifyToken(token, request).equals(login) && !ElectionM1if03JwtHelper.verifyAdmin(token)) {
                request.setAttribute("errorCode", HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorMessage", "Utilisateur non administrateur ou pas celui qui est logué.");
                return;
            }

            User user = users.get(login);

            if (user == null) {
                request.setAttribute("errorCode", HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "Utilisateur non trouvé.");
                return;
            }

            user.setNom(nom);
            request.setAttribute("statusCode", HttpServletResponse.SC_NO_CONTENT);
            request.setAttribute("successMessage", "Utilisateur correctement modifié.");
        } else {
            request.setAttribute("errorCode", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("errorMessage", "Requête non reconnue.");
            return;
        }
    }
}
