package fr.univlyon1.m1if.m1if03.classes.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class BallotsDTO extends ArrayList<String> {
    public BallotsDTO(final String url, Map<String, Ballot> ballotsId) {
        for (String key : ballotsId.keySet()) {
            add(url.concat("/ballots/").concat(key));
        }
    }
}
