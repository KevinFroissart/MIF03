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
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse wrapper = new BufferlessHttpServletResponseWrapper(response);
        super.doFilter(request, response, chain);

        Object vue = request.getAttribute("vue");
        Object dto = request.getAttribute("DTO");
        Object errorCode = request.getAttribute("errorCode");
        Object errorMessage = request.getAttribute("errorMessage");
        Object statusCode = request.getAttribute("statusCode");
        Object authorization = request.getAttribute("Authorization");
        Object location = request.getAttribute("Location");

        if (errorCode != null) {
            if (errorMessage != null)
                response.sendError(Integer.parseInt(errorCode.toString()), errorMessage.toString());
            else response.sendError(Integer.parseInt(errorCode.toString()));
            return;
        }

        if (authorization != null) {
            response.setHeader("Authorization", authorization.toString());
            if (statusCode != null) response.setStatus(Integer.parseInt(statusCode.toString()));
        }

        if (dto != null || vue != null) {
            String accept = request.getHeader("Accept");
            String content = request.getHeader("Content-type");
            String serialization = null;

            if ((content != null && content.contains("text/html")) || accept.contains("text/html")) {
                if (vue != null)
                    request.getServletContext().getRequestDispatcher("/WEB-INF/components/" + vue).include(request, response);
            } else if ((content != null && content.contains("application/xml")) || accept.contains("application/xml")) {
                serialization = APIResponseUtils.buildXml(response, dto);
            } else if ((content != null && content.contains("application/json")) || accept.contains("application/json") || accept.contains("*/*")) {
                serialization = APIResponseUtils.buildJson(response, dto);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
            }
            if (statusCode != null) response.setStatus(Integer.parseInt(statusCode.toString()));
            if (serialization != null) {
                PrintWriter out = response.getWriter();
                out.print(serialization);
                out.flush();
            }
        } else {
            if (statusCode != null) {
                if (location != null) response.setHeader("Location", location.toString());
                response.setStatus(Integer.parseInt(statusCode.toString()));
            }
        }
    }
}
