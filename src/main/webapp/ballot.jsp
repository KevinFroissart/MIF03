<%--
  Created by IntelliJ IDEA.
  User: kevin
  Date: 06/10/2021
  Time: 21:22
  To change this template use File | Settings | File Templates.
  <jsp:useBean id="ballots" scope="application" class="java.util.LinkedHashMap"/>
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Vote</title>
    <link rel="stylesheet" type="text/css" href="../static/vote.css">
</head>
<body>
<c:if test="${sessionScope.user == null}">
    <% response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous devez être connecté pour accéder à cette page."); %>
</c:if>
<jsp:include page="WEB-INF/components/header.jsp">
    <jsp:param name="titre" value="Votre preuve de vote"/>
</jsp:include>
<main id="contenu" class="wrapper">
    <%@include file="WEB-INF/components/menu.jsp" %>
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
                        <input type="submit" name="action" value="supprimer">
                    </p>
                </form>
            </c:otherwise>
        </c:choose>
    </article>
</main>
</body>
<%@include file="WEB-INF/components/footer.html" %>
</html>
