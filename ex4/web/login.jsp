<%-- 
    Document   : login
    Created on : Jan 12, 2012, 10:39:23 PM
    Author     : ilia
--%>
<%@page isThreadSafe="false" %>
<jsp:useBean id="DB" class="ex4.DerbyDataBase"  scope="page" /> 
<jsp:useBean id="Users" class="ex4.UsersList" scope="application" /> 
<%

        String LoginName =  (String)session.getAttribute("Login");
       
        if(LoginName!=null){  
            response.setStatus(301);
            response.setHeader("location", "index.jsp");       
        }
        String login        =   "";
        String pass         =   "";
        String ErrorMess    =   "";
            
        if(request.getContentType() != null){
            login   = request.getParameter("login");
            pass    = request.getParameter("pass");
                    
            if(login == null || pass == null || login.length()<3 || pass.length() < 1){
                ErrorMess = "Please enter password and login name.";
            }else{
                DB.connect();
                if(DB.isItAxistAtBD(login, pass)){
                    Users.AddUserToList(login);
                    session.setAttribute("Login", login);
                    response.setStatus(301);
                    response.setHeader("location", "index.jsp");
                }else{
                    ErrorMess = "Login or password incorect!";
                }
                DB.disconnect(); 
            }
        }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="ex4_ai.css" type="text/css" />
        <title>Login</title>
    </head>
    <body>
        <%@include file="userMenu.jsp" %>
        <h1>Please login</h1>
        <form method="post" action="login.jsp">
            <input type="text" name="login" value="<%=login%>" />
            <input type="password" name="pass" value=""/>
            <input type="submit" value="Login ..." />
            <%=ErrorMess%>
        </form>
    </body>
</html>
