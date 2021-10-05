<%--
  Created by IntelliJ IDEA.
  User: christ
  Date: 05/10/2021
  Time: 10:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Bulletin" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Candidat" %>
<%@ page import="java.util.*" %>
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
        <label>Sélectionné un candidat:</label>
        <%
            Collection<Candidat> values = ((Map<String, Candidat>) application.getAttribute("candidats")).values();
            ArrayList<Candidat> candidats = new ArrayList<>(values);
            ArrayList<String> candidatsName = new ArrayList<>();
            for (Candidat candidat : candidats) {
                candidatsName.add(candidat.getNom());
            }
        %>
        <select name="candidat" id="selection">
            <option value="">----</option>
            <c:forEach items="${candidatsName}"  var="nomCandidat">
                <option><c:out value="${nomCandidat}"/>  ${nomCandidat} </option>
            </c:forEach>
            <option value="Dog">Dog</option>
        </select>
    </article>
</main>
</body>
</html>
