<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Korisnik
  Date: 14.4.2023.
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <title>Trigonometric</title>
</head>
<body>
<table>
    <tr>
        <th>x</th>
        <th>sin(x)</th>
        <th>cos(x)</th>
    </tr>

    <c:forEach var="x" items="${values}">
        <tr>
            <th>${x.x}</th>
            <th>${x.sinX}</th>
            <th>${x.cosX}</th>
        </tr>
    </c:forEach>

</table>
</body>
</html>
