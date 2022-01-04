package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.model.Candidat;

import java.util.ArrayList;
import java.util.List;

public class CandidatsNomsDTO extends ArrayList {
    public CandidatsNomsDTO(List<Candidat> candidats) {
        for (Candidat candidat : candidats) {
            add(candidat.getNom());
        }
    }
}
