<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <title>Colors</title>
    <style>
        body {
            background-color: <%= session.getAttribute("pickedBgCol") %>;
        }
    </style>
</head>
<body>
<a href="/webapp2">Home</a>
<br>
<a href="setcolor?color=white">WHITE</a><br>
<a href="setcolor?color=red">RED</a><br>
<a href="setcolor?color=green">GREEN</a><br>
<a href="setcolor?color=cyan">CYAN</a><br>
</body>
</html>
