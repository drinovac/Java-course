<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Glasanje</title>
</head>
<body>

<h1>Glasanje za omiljeni bend:</h1>
<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!</p>
<ol>
    <c:forEach var="band" items="${bands}">
        <li><p><a href="glasanje-glasaj?id=${band.ID}">Glasaj za: ${band.name}</a><br><a href="${band.songLink}">Link na pjesmu</a></p></li>
    </c:forEach>
</ol>

</body>
</html>
