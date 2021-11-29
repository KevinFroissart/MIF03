package fr.univlyon1.m1if.m1if03.classes.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import fr.univlyon1.m1if.m1if03.utils.APIResponseUtils;

public class FiltreNegociationContenu extends HttpFilter {

	@Override
	protected  void doFilter (HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);

		Object dto = request.getAttribute("DTO");

		if (dto != null) {
			String accept = request.getHeader("Accept");
			String content = request.getHeader("Content-type");
			String serialization = null;
			if ((content != null && content.contains("application/json")) || accept.contains("application/json") || accept.contains("*/*")) {
				serialization = APIResponseUtils.buildJson(response, dto);
			} else if (accept.contains("text/html")) {
				// rediriger vers jsp / servlet
				serialization = APIResponseUtils.buildJson(response, dto);
			} else {
				response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			}
			if(serialization != null) {
				PrintWriter out = response.getWriter();
				out.print(serialization);
				out.flush();
			}
		}
	}
}
