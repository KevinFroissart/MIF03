package fr.univlyon1.m1if.m1if03.classes.model;

import java.util.ArrayList;
import java.util.List;

public class CandidatsNomsDTO extends ArrayList {
    public List<String> nomsCandidats;
    public CandidatsNomsDTO(List<Candidat> candidats){
        for (Candidat candidat:candidats){
            nomsCandidats.add(candidat.getNom());
        }
    }
}
