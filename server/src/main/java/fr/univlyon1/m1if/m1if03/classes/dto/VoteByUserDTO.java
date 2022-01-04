package fr.univlyon1.m1if.m1if03.classes.dto;

import java.util.ArrayList;

public class VoteByUserDTO extends ArrayList<String> {
    public VoteByUserDTO(final Integer id, final String url) {
        add(url.concat("/").concat(id.toString()));
    }
}
