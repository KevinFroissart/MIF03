package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.model.User;

public class UserDTO {
    
    public String login;
    public String nom;
    public boolean admin;

    public UserDTO(User user) {
        this.login = user.getLogin();
        this.nom = user.getNom();
        this.admin = user.isAdmin();
    }
}
