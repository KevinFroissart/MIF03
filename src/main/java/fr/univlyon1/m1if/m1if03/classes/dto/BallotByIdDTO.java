package fr.univlyon1.m1if.m1if03.classes.dto;

import java.util.ArrayList;

public class BallotByIdDTO extends ArrayList<String> {
    public BallotByIdDTO(final Integer ballotsId, final String url) {
        add(url.concat("/votes/").concat(ballotsId.toString()));
    }
}
