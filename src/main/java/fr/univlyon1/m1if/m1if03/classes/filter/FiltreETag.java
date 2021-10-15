package fr.univlyon1.m1if.m1if03.classes.filter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import fr.univlyon1.m1if.m1if03.classes.model.Ballot;
import fr.univlyon1.m1if.m1if03.classes.model.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.model.User;

@WebFilter(filterName = "EtagFilter")
public class FiltreETag extends HttpFilter {

	String ballotString = "";
	int votes = 0;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void doFilter (HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = request.getSession(true);
		User utilisateur = (User) session.getAttribute("user");
		Map<String, Ballot> ballots = (Map<String, Ballot>) request.getServletContext().getAttribute("ballots");
		List<Bulletin> bulletins = (List<Bulletin>) request.getServletContext().getAttribute("bulletins");

		votes = bulletins.size();
		if(utilisateur != null && ballots.get(utilisateur.getLogin()) != null) {
			ballotString = ballots.get(utilisateur.getLogin()).getBulletin().getCandidat().getNom();
		}

		String eTagFromBrowser = request.getHeader("If-None-Match");

		if(request.getMethod().equals("GET")){
			if(eTagFromBrowser != null){
				if (eTagFromBrowser.equals(getTag())){
					response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				} else {
					response.addHeader("Etag",getTag());
				}
			} else {
				response.setHeader("Etag",getTag());
			}
		}
		chain.doFilter(request, response);
	}

	private String getTag() {
		return ballotString + ":" + votes;
	}
}