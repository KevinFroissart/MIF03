<%--
  Created by IntelliJ IDEA.
  User: kfroissart
  Date: 08/10/2021
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Profile</title>
    <link rel="stylesheet" type="text/css" href="../static/vote.css">
</head>
<body>
<c:if test="${sessionScope.user == null}">
    <% response.sendError(403, "Vous devez être connecté pour accéder à cette page."); %>
</c:if>
<jsp:include page="WEB-INF/components/header.jsp">
    <jsp:param name="titre" value="Mettre à jour mon profil"/>
</jsp:include>
<main id="contenu" class="wrapper">
    <%@include file="WEB-INF/components/menu.jsp" %>
    <article class="contenu">
        <h2>Nom actuel : ${sessionScope.user.nom}</h2>
        <br/>
        <form method="post" action="profile">
            <h2>Modifier mon nom:</h2>
            <p>
                <input type="text" id="name" name="name" required>
                <input type="submit" name="action" value="Modifier">
            </p>
        </form>
    </article>
</main>
</body>
<%@include file="WEB-INF/components/footer.html" %>
</html>
