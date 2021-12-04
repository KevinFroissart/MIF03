package fr.univlyon1.m1if.m1if03.classes.model;

public class UsersUserIdDTO {
    private String login;
    private String nom;
    private boolean admin;

    public UsersUserIdDTO(String login, String nom, boolean admin){
        this.login = login;
        this.nom = nom;
        this.admin = admin;
    }
}
