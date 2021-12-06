<%--
  Created by IntelliJ IDEA.
  User: kfroissart
  Date: 07/10/2021
  Time: 18:44
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${param.header}</title>
    <link rel="stylesheet" type="text/css" href="../static/vote.css">
</head>
<body>
<header>
    <c:if test="${requestScope.login != null}">
        <p class="header-user"> Bonjour ${requestScope.login}</p>
    </c:if>
    <h1 class="header-titre">${param.titre}</h1>
</header>
