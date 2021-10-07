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
<c:if test="${sessionScope.user == null}">
    <% response.sendRedirect("index.html"); %>
</c:if>
<jsp:include page="WEB-INF/components/header.jsp">
    <jsp:param name="titre" value="Votre preuve de vote"/>
</jsp:include>
<main id="contenu" class="wrapper">
    <%@include file="WEB-INF/components/menu.jsp" %>
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
<%@include file="WEB-INF/components/footer.jsp" %>
</html>
