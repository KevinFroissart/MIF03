package fr.univlyon1.m1if.m1if03.classes.filter;

import fr.univlyon1.m1if.m1if03.classes.model.User;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "FiltreAutorisation", urlPatterns = "/election/listBallots")
public class FiltreAutorisation extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(((User) req.getSession().getAttribute("user")).isAdmin()) {
            super.doFilter(req, res, chain);
        } else {
            this.getServletContext().getRequestDispatcher("/WEB-INF/components/ballot.jsp").forward(req, res);
//            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous devez être administrateur pour accéder à cette page.");
        }
    }
}
