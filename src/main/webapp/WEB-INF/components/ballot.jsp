<%--
  Created by IntelliJ IDEA.
  User: kevin
  Date: 06/10/2021
  Time: 21:22
  To change this template use File | Settings | File Templates.
  <jsp:useBean id="ballots" scope="application" class="java.util.LinkedHashMap"/>
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp?header=Ballot&titre=Votre preuve de vote"/>
<%  response.setIntHeader("Refresh", 5); %>
<main id="contenu" class="wrapper">
    <%@include file="menu.jsp" %>
    <article class="contenu">
        <c:choose>
            <c:when test="${sessionScope.user != null && ballots.get(sessionScope.user.login) == null}">
                <p>Vous n'avez pas encore voté. Dirigez vous sur <a href="vote">cette page</a> pour voter.</p>
            </c:when>
            <c:otherwise>
                <form method="post" action="deleteVote">
                    <p>
                        Votre vote:
                        <b>${ballots.get(sessionScope.user.login).getBulletin().getCandidat().getPrenom()}
                                ${ballots.get(sessionScope.user.login).getBulletin().getCandidat().getNom()}</b>
                    </p>
                    <p>
                        Nombre total de votes: ${ballots.size()}
                    </p>
                    <p>
                        <input type="hidden" name="user" value="${sessionScope.user.login}">
                        <input type="submit" name="action" value="supprimer">
                    </p>
                </form>
            </c:otherwise>
        </c:choose>
    </article>
<%@include file="footer.html" %>
