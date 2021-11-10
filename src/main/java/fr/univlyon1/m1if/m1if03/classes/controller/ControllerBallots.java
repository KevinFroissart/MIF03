package fr.univlyon1.m1if.m1if03.classes.controller;

import fr.univlyon1.m1if.m1if03.classes.services.BallotsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ControllerBallots", value = {})
public class ControllerBallots extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if(uri.endsWith("ballots")) BallotsService.getBallots(); //election/ballots
        else if(uri.contains("/byUser")) {
            //election/ballots/byUser/{userId}
            BallotsService.getUserBallot(Integer.parseInt(uri.substring(uri.lastIndexOf('/') + 1)));
        }
         //election/ballots/{ballotId}
        else BallotsService.getBallot(Integer.parseInt(uri.substring(uri.lastIndexOf('/') + 1)));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String uri = request.getRequestURI();
        if(uri.endsWith("ballots")) BallotsService.createNewBallot();//election/ballots
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (uri.contains("/ballots/")) BallotsService.deleteBallot(); //election/ballots/{ballotId}
    }
}
