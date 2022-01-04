package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.model.Candidat;

public class CandidatDTO {

    public String prenom;
    public String nom;

    public CandidatDTO(Candidat candidat) {
        this.prenom = candidat.getPrenom();
        this.nom = candidat.getNom();
    }

}
