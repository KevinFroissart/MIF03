<%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 11/10/2021
  Time: 08:20
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp?header=Ballots&titre=Liste des ballots (admin)"/>
<main id="contenu" class="wrapper">
    <jsp:include page="menu.jsp"/>
    <article class="contenu">
        <h2>Voici la liste des <c:out value="${ballots.size()}" /> votants</h2>
        <ul>
            <c:forEach items="${ballots}" var="ballotEntry">
                <li>
                    <form action="deleteVote" method="post">
                        <c:out value="${ballotEntry.key}"/>
                        <input type="hidden" name="user" value="${ballotEntry.key}">
                        <input type="submit" value="supprimer">
                    </form>
                </li>
            </c:forEach>
        </ul>
    </article>
<%@include file="footer.html" %>
