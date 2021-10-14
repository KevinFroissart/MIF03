<%--
  Created by IntelliJ IDEA.
  User: kfroissart
  Date: 07/10/2021
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aside class="menu">
    <h2>Menu</h2>
    <ul>
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <li><a href="vote">Voter</a></li>
                <li><a href="listBallots">Votre vote</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="index.html">Se connecter</a></li>
            </c:otherwise>
        </c:choose>
        <li><a href="resultats">Résultats</a></li>
        <c:if test="${sessionScope.user != null}">
            <li><a href="user">Votre profil</a></li>
            <li><a href="deco">Déconnexion</a></li>
        </c:if>
    </ul>
</aside>
