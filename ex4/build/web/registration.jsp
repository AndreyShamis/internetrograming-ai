<%-- 
    Document   : registration
    Created on : Jan 12, 2012, 10:49:10 PM
    Author     : ilia
--%>
<%
        String LoginName =  (String)session.getAttribute("Login");
       
        if(LoginName!=null)
        {
                    response.setStatus(301);
                    response.setHeader("location", "index.jsp");       
        }
            String FirstName    =   request.getParameter("fname");
            String LastName     =   request.getParameter("lname");
            String login        =   request.getParameter("login");
            String pass         =   request.getParameter("pass");

            String ErrorMess = "";
            
            if(login == null || pass == null || login.length()<3 || pass.length() < 1){
                ErrorMess = "Please enter password and login name.";
            }else{
                                                   session.setAttribute("Login", login);
            }
%>            
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@include file="userMenu.jsp" %>
        <h1>Please enter data for resgistration</h1>
        <form method="post" action="registration.jsp">
            Please provide First Name<input type="text" name="fname" value="<%=FirstName%>" /><br/>
            Please provide Last Name<input type="text" name="lname" value="<%=LastName%>" /><br/>
            Please provide Login<input type="text" name="login" value="<%=login%>" /><br/>
            Please provide password<input type="password" name="pass" value="<%=pass%>"/><br/>
            <input type="submit" value="Register" />
            <%=ErrorMess%>
        </form>
    </body>
</html>
