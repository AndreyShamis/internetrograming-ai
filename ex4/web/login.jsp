<%-- 
    Document   : login
    Created on : Jan 12, 2012, 10:39:23 PM
    Author     : ilia
--%>
<%
        String LoginName =  (String)session.getAttribute("Login");
       
        if(LoginName!=null)
        {
                    response.setStatus(301);
                    response.setHeader("location", "index.jsp");       
        }
            String login    =   request.getParameter("login");
            String pass     =   request.getParameter("pass");

            String ErrorMess = "";
            
            if(login == null || pass == null || login.length()<3 || pass.length() < 1){
                ErrorMess = "Please enter password and login name.";
            }
           else{
                    session.setAttribute("Login", login);
                    response.setStatus(301);
                    response.setHeader("location", "index.jsp");
           }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="ex4_ai.css" type="text/css" />
        <title>Hadassah Book Log In</title>
        <title>Login</title>
    </head>
    <body>
        <%@include file="userMenu.jsp" %>
        <h1>Please login</h1>
        <form method="post" action="login.jsp">
            <input type="text" name="login" value="<%=login%>" />
            <input type="password" name="pass" value="<%=pass%>"/>
            <input type="submit" value="Login ..." />
            <%=ErrorMess%>
        </form>
    </body>
</html>
