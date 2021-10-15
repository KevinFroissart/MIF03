package fr.univlyon1.m1if.m1if03.classes.filter;

import fr.univlyon1.m1if.m1if03.classes.model.User;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "/AutorisationFilter")
public class FiltreAutorisation extends HttpFilter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        User utilisateur = (User) session.getAttribute("user");
        if(utilisateur.isAdmin()){
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("../WEB-INF/components/ballot.jsp").forward(request, response);
        }
    }
}