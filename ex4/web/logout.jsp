<%-- 
    Document   : loguot
    Created on : Jan 12, 2012, 10:39:54 PM
    Author     : EX4 Andrey Shamis AND Ilia Gaysinsky
--%>
<jsp:useBean id="Users" class="ex4.UsersList" scope="application" /> 
<%
    String LoginName =  (String)session.getAttribute("Login");
    Users.RemoveUserFromList(LoginName);
    session.removeAttribute("Login");
    session.invalidate();
    response.setStatus(301);
    response.setHeader("location", "login.jsp");
%>