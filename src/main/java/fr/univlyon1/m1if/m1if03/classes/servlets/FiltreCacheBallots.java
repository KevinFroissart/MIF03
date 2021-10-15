package fr.univlyon1.m1if.m1if03.classes.servlets;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebFilter(filterName = "/CacheBallotsFilter")
public class FiltreCacheBallots extends HttpFilter {
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        Date date = new Date();
    try{
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String method = httpServletRequest.getMethod();
        System.out.println(method);
        String uri =  request.getRequestURI().substring(getServletContext().getContextPath().length());
        System.out.println(uri);
        if (method.equals("POST")){
            chain.doFilter(request,response);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            date = new Date();
            System.out.println("date vote post : "+formatter.format(date));
            getServletContext().setAttribute("date", date.getTime());

            return;

        }
        if (uri.equals("/listBallots.jsp")  && method.equals("GET"))
        {
            String ifModified = request.getHeader("If-Modified-Since");
            System.out.println("ifModified"+ifModified);
            if(date != null){
                System.out.println("date :"+date);
                response.setDateHeader("Last-Modified", date.getTime());
                if(ifModified != null) {
                    // check = (ifModified.equals(response.getHeader("Last-Modified")));
                    long lastModifiedFromBrowser = request.getDateHeader("If-Modified-Since");
                    long lastModifiedFromServer = (long) getServletContext().getAttribute("date");
                    System.out.println("lastModifiedFromBrowser"+lastModifiedFromBrowser);
                    System.out.println("lastModifiedFromServer" +lastModifiedFromServer);

                    if (lastModifiedFromBrowser != -1 &&
                            lastModifiedFromServer < lastModifiedFromBrowser) {
                        //setting 304 and returning with empty body
                        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                        return;
                    }
                }

            }

            System.out.println("la date dans getheader:" +ifModified);
        }
        chain.doFilter(request, response);

    } catch (IOException | ServletException e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
     }
    }
}
