package fr.univlyon1.m1if.m1if03.classes.model;

public class BallotsUserIdDTO {
    public BallotsUserIdDTO(final String url, final String id ){
        url.concat("/ballots/");
        url.concat(id);
    }
}
