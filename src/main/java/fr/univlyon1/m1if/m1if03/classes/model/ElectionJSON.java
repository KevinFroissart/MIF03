package fr.univlyon1.m1if.m1if03.classes.model;

import java.util.List;

public class ElectionJSON {

    public List<VotesJSON> elections;

    public ElectionJSON(List<VotesJSON> elections) {
        this.elections = elections;
    }

}
