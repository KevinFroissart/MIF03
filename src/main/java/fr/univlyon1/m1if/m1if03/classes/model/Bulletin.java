package fr.univlyon1.m1if.m1if03.classes.model;

public class Bulletin {
    final Candidat candidat;

    public Bulletin(Candidat candidat) {
        this.candidat = candidat;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    @Override
    public String toString() {
        return "Bulletin{" +
                "candidat=" + candidat +
                '}';
    }
}
