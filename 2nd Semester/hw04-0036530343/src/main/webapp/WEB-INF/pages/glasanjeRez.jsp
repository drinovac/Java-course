<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Rezultati glasanja</title>
    <style type="text/css">
        table.rez td {text-align: center;}
    </style>
</head>
<body>
    <h1>Rezultati glasanja</h1>
    <p>Ovo su rezultati glasanja</p>
    <table border="1" cellspacing="0" class="rez">
        <thead><tr><th>Bend</th><th>Broj glasova</th><th>Broj lajkova</th><th>Broj dislajkova</th><th>Razlika</th></tr></thead>
        <tbody>
        <c:forEach var="band" items="${results}">
            <tr><td>${band.name}</td><td>${band.numberOfVotes}</td><td>${band.numberOfLikes}</td>
                <td>${band.numberOfDislikes}</td></tr>
        </c:forEach>
        </tbody>
    </table>

    <h2>Grafički prikaz rezultata</h2>
    <img alt="Pie-chart" src="${pageContext.request.contextPath}/servleti/glasanje-chart?pollID=${pollID}" width="500" height="350" />

    <h2>Rezultati u XLS formatu</h2>
    <p>Rezultati u XLS formatu dostupni su <a href="${pageContext.request.contextPath}/servleti/glasanje-xls?pollID=${pollID}">ovdje</a></p>

    <h2>Razno</h2>
    <p>Primjeri pjesama pobjedničkih bendova:</p>
    <ul>
        <c:forEach var="band" items="${winners}">
            <li><a href="${band.songLink}" target="_blank">${band.name}</a></li>
        </c:forEach>
    </ul>

    <%

    %>
    <%!
        public int difference(String a, String b) {
            return Integer.parseInt(a) - Integer.parseInt(b);
        }
    %>

</body>
</html>
