<%--
  Created by IntelliJ IDEA.
  User: christ
  Date: 05/10/2021
  Time: 10:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Candidat" %>
<jsp:useBean id="candidats" scope="application" class="java.util.LinkedHashMap"/>
<html>
<head>
    <title>Vote</title>
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
<c:if test="${sessionScope.user == null}">
    <% response.sendError(403, "Vous devez être connecté pour accéder à cette page."); %>
</c:if>
<jsp:include page="WEB-INF/components/header.jsp">
    <jsp:param name="titre" value="Votez pour qui vous voulez"/>
</jsp:include>
<main id="contenu" class="wrapper">
    <%@include file="WEB-INF/components/menu.jsp" %>
    <article class="contenu">
        <c:choose>
            <c:when test="${ballots.get(sessionScope.user.login) != null}">
                <p>Vous avez déjà voté. Vous pouvez consulter votre vote sur <a href="ballot.jsp">cette page</a>. </p>
            </c:when>
            <c:otherwise>
                <form method="post" action="castVote">
                    <label>Sélectionnez un candidat:</label>
                    <select name="candidat" id="selection">
                        <option value="">----</option>
                        <c:forEach items="<%= ((Map<String, Candidat>)candidats).keySet()%>" var="nomCandidat">
                            <option>
                                <c:out value="${nomCandidat}"/>
                            </option>
                        </c:forEach>
                    </select>
                    <p>
                        <input type="submit" name="action" value="Voter">
                    </p>
                </form>
            </c:otherwise>
        </c:choose>
    </article>
</main>
</body>
<%@include file="WEB-INF/components/footer.jsp" %>
</html>
