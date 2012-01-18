<%-- 
    Document   : index
    Created on : Jan 12, 2012, 8:40:19 PM
    Author     : EX4 Andrey Shamis AND Ilia Gaysinsky
--%>
<%

        String LoginName =  (String)session.getAttribute("Login");
       
        if(LoginName==null)
        {
            response.setStatus(301);
            response.setHeader("location", "login.jsp");
        }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="ex4_ai.css" type="text/css" />
        <title>EX4 Andrey Shamis AND Ilia Gaysinsky</title>
    </head>
    <body>
        <%@include file="userMenu.jsp" %>
        <%@include file="home.jsp" %>
    </body>
</html>
