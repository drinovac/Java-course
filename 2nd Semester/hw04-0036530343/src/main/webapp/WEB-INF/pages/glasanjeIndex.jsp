<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Glasanje</title>
</head>
<body>

<h1>${poll.title}</h1>
<p>${poll.message}</p>
<ol>
    <c:forEach var="band" items="${bands}">
        <li><p><a href="glasanje-glasaj?id=${band.ID}&pollID=${poll.id}">Glasaj za: ${band.name}</a>
            <a href="glasanje-like?id=${band.ID}&pollID=${poll.id}">Lajkaj</a>
            <a href="glasanje-dislike?id=${band.ID}&pollID=${poll.id}">Dislajkaj</a>
            <br>
            <a href="${band.songLink}">Link na pjesmu</a></p></li>
    </c:forEach>
</ol>

</body>
</html>
