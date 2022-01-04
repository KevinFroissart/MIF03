<%--
  Created by IntelliJ IDEA.
  User: kfroissart
  Date: 08/10/2021
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp?header=Profil&titre=Mettre à jour mon profil"/>
<main id="contenu" class="wrapper">
    <%@include file="menu.jsp" %>
    <article class="contenu">
        <h2>Nom actuel : ${requestScope.login}</h2>
        <br/>
        <form method="put" action="/users/${requestScope.login}/nom">
            <h2>Modifier mon nom:</h2>
            <p>
                <input type="text" id="name" name="name" required>
                <input type="submit" name="action" value="Modifier">
            </p>
        </form>
    </article>
    <%@include file="footer.html" %>
