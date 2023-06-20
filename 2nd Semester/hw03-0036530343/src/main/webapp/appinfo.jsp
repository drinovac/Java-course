<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AppInfo</title>
</head>
<body>

<% long duration = System.currentTimeMillis() - (long) application.getAttribute("initTime");
long miliseconds = duration % 1000;
long seconds = duration / 1000;
long minutes = seconds / 60;
long hours = minutes / 60;
long days = hours / 24;
hours = hours % 24;
minutes = minutes % 60;
seconds = seconds % 60; %>

<p>Time: <%=days%> days, <%=hours%> hours, <%=minutes%> minutes, <%=seconds%> seconds and <%=miliseconds%> miliseconds</p>

</body>
</html>
