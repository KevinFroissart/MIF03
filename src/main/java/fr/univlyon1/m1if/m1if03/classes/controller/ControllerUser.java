package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import fr.univlyon1.m1if.m1if03.classes.services.UserService;

@WebServlet(name = "ControllerUser", value = {})
public class ControllerUser extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String uri = request.getRequestURI();
		String[] split = uri.split("/");

		if(uri.endsWith("users")) UserService.getUsers(); // /users/
		else if(uri.endsWith("ballots")) UserService.getBallotUser(Integer.parseInt(split[split.length - 2])); // /users/{userId}/ballot
		else if(uri.endsWith("vote")) UserService.getVoteUser(Integer.parseInt(split[split.length - 2])); // /users/{userId}/vote
		else if(uri.contains("/users/")) UserService.getUser(Integer.parseInt(split[split.length - 1])); // /users/{userId}
		//this.getServletContext().getRequestDispatcher("/WEB-INF/components/profil.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String uri = request.getRequestURI();
		String[] split = uri.split("/");

		if(uri.endsWith("nom")) UserService.updateNomUser(Integer.parseInt(split[split.length - 2])); // /users/{usersId}/nom
		//this.getServletContext().getNamedDispatcher("Profil").include(request, response);
		//this.getServletContext().getRequestDispatcher("/WEB-INF/components/profil.jsp").forward(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String uri = request.getRequestURI();

		if(uri.endsWith("login")) UserService.login(); // /users/login
		else if(uri.endsWith("logout")) UserService.logout(); // /users/logout
		//this.getServletContext().getNamedDispatcher("Profil").include(request, response);
		//this.getServletContext().getRequestDispatcher("/WEB-INF/components/profil.jsp").forward(request, response);
	}
}
