package fr.univlyon1.m1if.m1if03.classes.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import fr.univlyon1.m1if.m1if03.classes.model.Ballot;
import fr.univlyon1.m1if.m1if03.classes.model.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.model.User;

@WebServlet(name = "DeleteVote", value = "/election/deleteVote")
public class DeleteVote extends HttpServlet {

    Map<String, Ballot> ballots = null;
    List<Bulletin> bulletins = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        ballots = (Map<String, Ballot>) context.getAttribute("ballots");
        bulletins = (List<Bulletin>) context.getAttribute("bulletins");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("user");
        Ballot ballot = ballots.get(login);
        Bulletin bulletin = ballot.getBulletin();
        bulletins.remove(bulletin);
        ballots.remove(login);

        HttpSession session = request.getSession(true);
        User utilisateur = (User) session.getAttribute("user");
        request.getRequestDispatcher(
                utilisateur.isAdmin()
                ? "../WEB-INF/components/listBallots.jsp"
                : "../WEB-INF/components/ballot.jsp")
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("../index.html");
    }
}
