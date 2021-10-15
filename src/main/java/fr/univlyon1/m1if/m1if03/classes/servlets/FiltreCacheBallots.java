package fr.univlyon1.m1if.m1if03.classes.servlets;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "/CacheBallotsFilter")
public class FiltreCacheBallots extends HttpFilter {

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {

        Date date = new Date();

        try {
            String method = request.getMethod();
            String uri = request.getRequestURI().substring(getServletContext().getContextPath().length());

            if (method.equals("POST")){
                chain.doFilter(request,response);
                getServletContext().setAttribute("date", date.getTime());
                return;
            }
            if (uri.endsWith("listBallots") && method.equals("GET")) {
                String ifModified = request.getHeader("If-Modified-Since");
                response.setDateHeader("Last-Modified", date.getTime());
                if (ifModified != null) {
                    long lastModifiedFromBrowser = request.getDateHeader("If-Modified-Since");
                    long lastModifiedFromServer = (long) getServletContext().getAttribute("date");
                    if (lastModifiedFromBrowser != -1 && lastModifiedFromServer < lastModifiedFromBrowser) {
                        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                        return;
                    }
                }
            }
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
