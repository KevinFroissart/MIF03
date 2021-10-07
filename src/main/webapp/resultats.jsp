<%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 03/10/2021
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="votes" class="java.util.HashMap" scope="request" />
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
    <jsp:param name="titre" value="Résultats actuels de l'élection"/>
</jsp:include>
<main id="contenu" class="wrapper">
    <%@include file="WEB-INF/components/menu.jsp" %>
    <article class="contenu">
        <h2>Voici le résultat courant de l'élection</h2>
        <ul>
            <c:out value="<%= ((Map<String, Integer>) votes).size()%>"/>
            <c:forEach items="<%= ((Map<String, Integer>) votes).keySet()%>" var="nomCandidat">
                <li><c:out value="${nomCandidat}"/> : <%= ((Map<String, Integer>) votes).get((String)pageContext.getAttribute("nomCandidat")) %> vote(s)</li>
            </c:forEach>
        </ul>
    </article>
</main>
</body>
<%@include file="WEB-INF/components/footer.jsp" %>
</html>
