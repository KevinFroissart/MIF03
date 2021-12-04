package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.model.User;

import java.util.ArrayList;
import java.util.Collection;

public class UsersDTO extends ArrayList<String> {
    public UsersDTO(final Collection<User> users, final String url) {
        for (User user : users) {
            add(url.concat("/").concat(user.getLogin().replaceAll(" ", "%20")));
        }
    }
}
