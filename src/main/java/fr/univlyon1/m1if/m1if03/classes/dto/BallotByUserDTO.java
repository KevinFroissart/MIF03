package fr.univlyon1.m1if.m1if03.classes.dto;

import java.util.ArrayList;

public class BallotByUserDTO extends ArrayList<String> {
    public BallotByUserDTO(final Integer ballotsId, final String url) {
        add(url.concat("/").concat(ballotsId.toString()));
    }
}
