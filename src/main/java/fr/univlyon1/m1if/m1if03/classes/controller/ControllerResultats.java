package fr.univlyon1.m1if.m1if03.classes.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;

@WebServlet(name = "ControllerResultats", value = {})
public class ControllerResultats extends HttpServlet {

	private List<String> uri;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		uri = APIResponseUtils.splitUri(request.getRequestURI());

		this.getServletContext().getNamedDispatcher("Resultats").include(request, response);
		this.getServletContext().getRequestDispatcher("/WEB-INF/components/resultats.jsp").forward(request, response);
	}

}
