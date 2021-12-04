package fr.univlyon1.m1if.m1if03.classes.model;

public class VotesUserIdDTO {
    public VotesUserIdDTO(final String url, final String id){
        url.concat("/votes/");
        url.concat(id);
    }
}
