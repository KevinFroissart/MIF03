package fr.univlyon1.m1if.m1if03.classes.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "/CacheBallotsFilter")
public class FiltreCacheBallots extends HttpFilter {
    Long lastModified = (long) -1;

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        switch(req.getRequestURI().split("/")[req.getRequestURI().split("/").length - 1]) {
            case "listBallots":
                if(req.getMethod().equals("GET")) {
                    if(req.getHeader("If-Modified-Since") != null && req.getDateHeader("If-Modified-Since") > lastModified) {
                        res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    } else {
                        res.setDateHeader("Last-Modified", new Date().getTime());
                        super.doFilter(req, res, chain);
                    }
                }
                break;
            case "vote":
            case "deleteVote":
                if(req.getMethod().equals("POST")) {
                    super.doFilter(req, res, chain);
                    lastModified = new Date().getTime();
                    break;
                }
            default:
                super.doFilter(req, res, chain);
        }
    }
}
