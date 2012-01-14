<%-- 
    Document   : usersList.jsp
    Created on : Jan 12, 2012, 11:41:13 PM
    Author     : ilia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%

for(int i=0;i<30;i++){
    %>
    User:<%=i%><br/>
    <%
}
%>