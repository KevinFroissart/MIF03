package fr.univlyon1.m1if.m1if03.classes.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "/AutorisationFilter")
public class FiltreAutorisation extends HttpFilter {
    public void init(FilterConfig config) throws ServletException {
    }


    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");

        if (session.getAttribute("user") == null){
            chain.doFilter(request, response);
        } else if(user.isAdmin()){
            request.getRequestDispatcher("listBallots.jsp").forward(request,response);

        } else {
            request.getRequestDispatcher("ballot.jsp").forward(request, response);

        }
    }
}
