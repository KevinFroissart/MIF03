package fr.univlyon1.m1if.m1if03.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class APIResponseUtils {

    public static List<String>  splitUri(String url){
        List<String> uri = new LinkedList<>(Arrays.asList(url.split("/")));
        uri.remove(0);
        uri.remove(0);
        return uri;
    }

    public static void buildJson(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String res = objectMapper.writeValueAsString(object);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
    }


}
