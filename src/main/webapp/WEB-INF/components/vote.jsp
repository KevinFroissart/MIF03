<%--
  Created by IntelliJ IDEA.
  User: christ
  Date: 05/10/2021
  Time: 10:14
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.model.Candidat" %>
<jsp:useBean id="candidats" scope="application" class="java.util.LinkedHashMap"/>
<jsp:include page="header.jsp?header=Vote&titre=Votez pour qui vous voulez"/>
<main id="contenu" class="wrapper">
    <%@include file="menu.jsp" %>
    <article class="contenu">
        <c:choose>
            <c:when test="${ballots.get(sessionScope.user.login) != null}">
                <p>Vous avez déjà voté. Vous pouvez consulter votre vote sur <a href="listBallots">cette page</a>. </p>
            </c:when>
            <c:otherwise>
                <form method="post" action="vote">
                    <label>Sélectionnez un candidat:</label>
                    <select name="candidat" id="selection">
                        <option value="">----</option>
                        <c:forEach items="<%= ((Map<String, Candidat>)candidats).keySet()%>" var="nomCandidat">
                            <option>
                                <c:out value="${nomCandidat}"/>
                            </option>
                        </c:forEach>
                    </select>
                    <p><input type="submit" name="action" value="Voter"></p>
                </form>
            </c:otherwise>
        </c:choose>
    </article>
<%@include file="footer.html" %>
