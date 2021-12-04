package fr.univlyon1.m1if.m1if03.classes.servlets;

import fr.univlyon1.m1if.m1if03.classes.model.Ballot;
import fr.univlyon1.m1if.m1if03.classes.model.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.model.Candidat;
import fr.univlyon1.m1if.m1if03.classes.model.User;
import fr.univlyon1.m1if.m1if03.utils.CandidatListGenerator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Init", value = "/init", loadOnStartup = 1)
public class Init extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Map<String, Candidat> candidats;
        Map<String, User> users = new HashMap<>();
        Map<User, Integer> usersId = new HashMap<>();
        Map<String, Ballot> ballots = new HashMap<>();
        Map<String, Ballot> ballotsId = new HashMap<>();
        List<Bulletin> bulletins = new ArrayList<>();
        List<String> expiredTokens = new ArrayList<>();

        int compteur = -1;

        ServletContext context = config.getServletContext();
        context.setAttribute("users", users);
        context.setAttribute("usersId", usersId);
        context.setAttribute("ballots", ballots);
        context.setAttribute("ballotsId", ballotsId);
        context.setAttribute("bulletins", bulletins);
        context.setAttribute("expiredTokens", expiredTokens);
        context.setAttribute("compteur", compteur);

        try {
            candidats = CandidatListGenerator.getCandidatList();
            context.setAttribute("candidats", candidats);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
