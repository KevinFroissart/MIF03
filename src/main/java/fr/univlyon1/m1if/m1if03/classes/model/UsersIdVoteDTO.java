package fr.univlyon1.m1if.m1if03.classes.model;

public class UsersIdVoteDTO {
    public UsersIdVoteDTO(final String url, final String id){
        url.concat("/users/");// A revoir
        url.concat(id);
        url.concat("/vote");
    }
}
