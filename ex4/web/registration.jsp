<%-- 
    Document   : registration
    Created on : Jan 12, 2012, 10:49:10 PM
    Author     : ilia
--%>
<%@page isThreadSafe="false" %>
<jsp:useBean id="DB" class="ex4.DerbyDataBase"  scope="page" /> 
<%
    String fNameErr = ""; 
    String lNameErr = "";
    String passwordErr="";
    String loginErr = "";
    boolean isValid = true;


    String LoginName =  (String)session.getAttribute("Login");
    if(LoginName!=null){
        response.setStatus(301);
        response.setHeader("location", "index.jsp");       
    }

        
        String FirstName    =   request.getParameter("fname");
        String LastName     =   request.getParameter("lname");
        String login        =   request.getParameter("login");
        String pass         =   request.getParameter("pass");
        
        if(request.getContentType() != null){
            
            if(FirstName == null){
                fNameErr = "You mast Entert at least 1 charectar!";
                isValid = false;
            }else if(FirstName.matches("[a-zA-Z]")){
                fNameErr = "You mast Entert only letters!";
                isValid = false;
            }  
            
            if(LastName == null){
                lNameErr = "You mast Entert at least 1 charectar!";
                isValid = false;
            }else if(LastName.matches("[a-zA-Z]")){
                lNameErr = "You mast Entert only letters!";
                isValid = false;
            }  

            if(pass == null || pass.length() < 6){
                passwordErr = "Password mast have at least 6 charectar";
                isValid = false;       
            }    
            
            DB.connect();

            if(login == null || login.length()<3){
                loginErr = "Login Name mast have at least 3 charectar";
                isValid = false;       
            }else if(DB.isItAxistAtBD(login, pass)){
                loginErr = "Entered Login Name alredy exist, Pleas enter other Login Name";
                isValid = false; 
            }   
%>
<jsp:useBean id="Users" class="ex4.UsersList" scope="application" /> 
<%
            if(isValid){
                String [] records = new String[4];
                records[0] = login;
                records[1] = pass;
                records[2] = FirstName;
                records[3] = LastName;
                DB.saveRecord(records);
                Users.AddUserToList(login);
                session.setAttribute("Login", login);
                response.setStatus(301);
                response.setHeader("location", "index.jsp");
            }
            DB.disconnect();                                                          
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
            Please provide password<input type="password" name="pass" value=""/><br/>
            Please retype password<input type="password" name="pass2" value=""/><br/>
            <input type="submit" value="Register" />
<%=loginErr%>
        </form>
    </body>
</html>
