package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ControllerUsers", value = {"/users", "/users/*"})
public class MainControllerUsers extends HttpServlet {

	HashMap<String, String> routes = new HashMap<>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		routes.put("users", "ControllerUser");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String url = request.getRequestURL().toString();

		for(Map.Entry<String, String> entry : routes.entrySet()) {
			if(url.contains(entry.getKey())) this.getServletContext().getNamedDispatcher(entry.getValue()).include(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String url = request.getRequestURL().toString();

		for(Map.Entry<String, String> entry : routes.entrySet()) {
			if(url.contains(entry.getKey())) this.getServletContext().getNamedDispatcher(entry.getValue()).include(request, response);
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String url = request.getRequestURL().toString();

		for(Map.Entry<String, String> entry : routes.entrySet()) {
			if(url.contains(entry.getKey())) this.getServletContext().getNamedDispatcher(entry.getValue()).include(request, response);
		}
	}
}
