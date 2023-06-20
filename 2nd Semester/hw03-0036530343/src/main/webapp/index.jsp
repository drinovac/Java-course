<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
<head>
    <title>Index</title>
    <style>
        body {
            background-color:  <%= session.getAttribute("pickedBgCol") %>;
        }
    </style>
</head>
<body>
<a href="setcolor">Background color chooser</a>
<hr>
<a href="trigonometric?a=0&b=90">Trigonometric page</a>
<hr>
<form action="trigonometric" method="GET">
    Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
    Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
    <input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
</form>

<hr>

<a href="stories/funny.jsp">See funny story</a>

<hr>

<a href="reportImage">Generate chart</a>

<hr>

<form action="powers" method="GET">
    a: <input type="number" name="a" step="1" value="0"><br>
    b: <input type="number" name="b" step="1" value="0"><br>
    n: <input type="number" name="n" step="1" value="1"><br>
    <input type="submit" value="Generiraj"><input type="reset" value="Reset">
</form>

<a href="powers?a=1&b=100&n=3">Generate excel table with default parameters</a>

<hr>

<a href="appinfo.jsp">App Info Site</a>

<hr>

<a href="glasanje">Link to site for voting</a>
</body>
</html>












