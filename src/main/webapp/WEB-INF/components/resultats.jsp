<%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 03/10/2021
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp?header=Résultats&titre=Résultats actuels de l'élection"/>
<main id="contenu" class="wrapper">
    <%@include file="menu.jsp" %>
    <article class="contenu">
        <h2>Voici le résultat courant de l'élection</h2>
        <ul>
            <c:forEach items="${votes.keySet()}" var="nomCandidat">
                <li><c:out value="${nomCandidat}"/> : ${votes.get(pageContext.getAttribute("nomCandidat"))} vote(s)</li>
            </c:forEach>
        </ul>
    </article>
    <%@include file="footer.html" %>
