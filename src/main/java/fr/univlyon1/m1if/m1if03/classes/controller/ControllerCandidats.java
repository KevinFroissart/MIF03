package fr.univlyon1.m1if.m1if03.classes.controller;

import fr.univlyon1.m1if.m1if03.classes.services.CandidatsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ControllerCandidats", value = {})
public class ControllerCandidats extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();

        if(uri.endsWith("candidats")) CandidatsService.getCandidats(); // /election/candidats
        else if(uri.endsWith("noms")) CandidatsService.getNomCandidats(); // /election/candidats/nom
        else if(uri.contains("/candidats/")) CandidatsService.getCandidat(Integer.parseInt(uri.substring(uri.lastIndexOf('/') + 1))); // /election/candidats/{candidatId}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();

        if(uri.endsWith("update")) CandidatsService.updateCandidats(); // /election/candidats/update
    }
}
