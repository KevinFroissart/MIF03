package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.model.User;
import fr.univlyon1.m1if.m1if03.classes.services.UserService;
import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;

@WebServlet(name = "ControllerUser", value = {})
public class ControllerUser extends HttpServlet {

	List<String> uri;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		uri = APIResponseUtils.splitUri(request.getRequestURI());

		// /users
		if(uri.size() == 1) {
			// TODO: implémenter
		}

		// /users/{userId}
		else if(uri.size() == 2) {
			// TODO: implémenter
		}

		else if(uri.size() == 3) {

			// /users/{userId}/vote
			if(uri.get(2) == "vote") {
				// TODO: implémenter
			}

			// /users/{userId}/ballot
			else if(uri.get(2) == "ballot") {
				// TODO: implémenter
			}

			else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "requête non reconnue");
				return;
			}

		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "requête non reconnue");
			return;
		}

//		if(uri.endsWith("users")) UserService.getUsers(); // /users/
//		else if(uri.endsWith("ballots")) UserService.getBallotUser(Integer.parseInt(split[split.length - 2])); // /users/{userId}/ballot
//		else if(uri.endsWith("vote")) UserService.getVoteUser(Integer.parseInt(split[split.length - 2])); // /users/{userId}/vote
//		else if(uri.contains("/users/")) UserService.getUser(Integer.parseInt(split[split.length - 1])); // /users/{userId}
		//this.getServletContext().getRequestDispatcher("/WEB-INF/components/profil.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		uri = APIResponseUtils.splitUri(request.getRequestURI());

		// /users/login
		if(uri.get(1).equals("login")) {
			if(request.getParameter("login") == null || request.getParameter("nom") == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requête non acceptables");
				return;
			}
			String login = request.getParameter("login");
			if(!login.equals("")) {
				HttpSession session = request.getSession(true);
				session.setAttribute("user", new User(login,
						request.getParameter("nom"),
						request.getParameter("admin") != null && request.getParameter("admin").equals("on")));
			}
			else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez être connecté pour accéder à cette page.");
				return;
			}
		}

		// /users/logout
		else if(uri.get(1).equals("logout")) {
			HttpSession session = request.getSession(false);
			User utilisateur = (User) session.getAttribute("user");
			if(!utilisateur.isAdmin()) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
				return;
			}
			session.invalidate();
			response.sendError(HttpServletResponse.SC_NO_CONTENT, "successful operation");
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		uri = APIResponseUtils.splitUri(request.getRequestURI());

		// /users/{usersId}/nom
	 	if(uri.get(2).equals("nom")) {
			// TODO: implémenter
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Requête non reconnue");
			return;
		}

		//this.getServletContext().getNamedDispatcher("Profil").include(request, response);
		//this.getServletContext().getRequestDispatcher("/WEB-INF/components/profil.jsp").forward(request, response);
	}
}
