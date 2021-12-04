package fr.univlyon1.m1if.m1if03.classes.model;

import java.util.Collection;
import java.util.Map;

public class BallotID {
    public BallotID(final String url, final String id ){
        url.concat("/candidats/");
        url.concat(id);
    }
}
