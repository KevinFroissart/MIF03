package fr.univlyon1.m1if.m1if03.classes;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DeleteVote", value = "/DeleteVote")
public class DeleteVote extends HttpServlet {
    Map<String, Ballot> ballots = null;
    List<Bulletin> bulletins = null;
    Map<String, Integer> votes = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();

        votes = (Map<String, Integer>) context.getAttribute("votes");

        ballots = (Map<String, Ballot>) context.getAttribute("ballots");
        bulletins = (List<Bulletin>) context.getAttribute("bulletins");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        User utilisateur = (User) session.getAttribute("user");
        Ballot ballot = ballots.get(utilisateur.getLogin());
        Bulletin bulletin = ballot.getBulletin();
        bulletins.remove(bulletin);
        ballot.setBulletin(null);
        ballots.remove(utilisateur.getLogin());
        request.getRequestDispatcher("ballot.jsp").forward(request, response);
    }
}
