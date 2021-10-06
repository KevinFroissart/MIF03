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
<html>
<head>
    <title>Vote</title>
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
<header>
    <c:if test="${sessionScope.user != null}">
        <p class="header-user"> Bonjour ${sessionScope.user.nom}</p>
    </c:if>
    <h1 class="header-titre">Voter pour qui vous voulez</h1>
</header>
<main id="contenu" class="wrapper">
    <aside class="menu">
        <h2>Menu</h2>
        <ul>
            <li><a href="vote.jsp">Voter</a></li>
            <li><a href="ballot.jsp">Votre vote</a></li>
            <li><a href="resultats.jsp">Résultats</a></li>
            <li><a href="Deco">Déconnexion</a></li>
        </ul>
    </aside>
    <article class="contenu">
        <form method="post" action="castVote">
            <label>Sélectionnez un candidat:</label>
            <%
                Map<String, Candidat> candidats = (Map<String, Candidat>) application.getAttribute("candidats");
            %>
            <select name="candidat" id="selection">
                <option value="">----</option>
                <c:forEach items="<%= candidats.keySet()%>" var="nomCandidat">
                    <option><c:out value="${nomCandidat}"/></option>
                </c:forEach>
            </select>
            <p>
                <input type="submit" name="action" value="Voter">
            </p>
        </form>
    </article>
</main>
</body>
</html>
