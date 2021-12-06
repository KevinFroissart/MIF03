package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.model.Ballot;

import java.util.ArrayList;
import java.util.Map;

public class BallotsDTO extends ArrayList<String> {
    public BallotsDTO(final Map<Integer, Ballot> ballotsId, final String url) {
        for (Integer id : ballotsId.keySet()) {
            add(url.concat("/").concat(id.toString()));
        }
    }
}
