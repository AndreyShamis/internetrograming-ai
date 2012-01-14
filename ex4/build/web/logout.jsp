<%-- 
    Document   : loguot
    Created on : Jan 12, 2012, 10:39:54 PM
    Author     : ilia
--%>
<%

    session.removeAttribute("Login");
    response.setStatus(301);
    response.setHeader("location", "index.jsp");
%>