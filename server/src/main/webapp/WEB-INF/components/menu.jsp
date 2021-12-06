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
            <c:when test="${requestScope.login != null}">
                <li><a href="../election/vote">Voter</a></li>
                <li><a href="../election/votes/byUser/${requestScope.login}">Votre vote</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="../index.html">Se connecter</a></li>
            </c:otherwise>
        </c:choose>
        <li><a href="../election/resultats">Résultats</a></li>
        <c:if test="${requestScope.login != null}">
            <li><a href="../users/${requestScope.login}">Votre profil</a></li>
            <li><a href="../users/logout">Déconnexion</a></li>
        </c:if>
    </ul>
</aside>
