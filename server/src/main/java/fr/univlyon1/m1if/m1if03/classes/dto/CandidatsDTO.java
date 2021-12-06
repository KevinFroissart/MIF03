package fr.univlyon1.m1if.m1if03.classes.dto;


import fr.univlyon1.m1if.m1if03.classes.model.Candidat;

import java.util.ArrayList;
import java.util.Collection;

public class CandidatsDTO extends ArrayList<String> {
    public CandidatsDTO(final Collection<Candidat> candidats, final String url) {
        for (Candidat candidat : candidats) {
            add(url.concat("/" + candidat.getNom().replaceAll(" ", "%20")));
        }
    }
}
