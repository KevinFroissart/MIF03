<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: kevin
  Date: 06/10/2021
  Time: 21:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Vote</title>
    <link rel="stylesheet" type="text/css" href="static/vote.css">
</head>
<body>
<header>
    <c:choose>
        <c:when test="${sessionScope.user == null}">
            <% response.sendRedirect("index.html"); %>
        </c:when>
        <c:otherwise>
            <p class="header-user"> Bonjour ${sessionScope.user.nom}</p>
        </c:otherwise>
    </c:choose>
    <h1 class="header-titre">Votre preuve de vote</h1>
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
        <c:choose>
            <c:when test="${sessionScope.user == null}"> <%-- TODO: remplacer par une methode qui permet de savoir si l'utilisateur a voté --%>
                <p>Vous n'avez pas encore voté. Dirigez vous sur <a href="vote.jsp">cette page</a> pour voter </p>
            </c:when>
            <c:otherwise>
                <p>Votre vote: </p>
                <p>
                    <input type="submit" name="action" value="supprimer">
                </p>
            </c:otherwise>
        </c:choose>
    </article>
</main>
</body>
</html>
