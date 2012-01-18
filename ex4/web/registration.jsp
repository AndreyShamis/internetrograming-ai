<%-- 
    Document   : registration
    Created on : Jan 12, 2012, 10:49:10 PM
    Author     : EX4 Andrey Shamis AND Ilia Gaysinsky
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

        
        String FirstName    =   "";
        String LastName     =   "";
        String login        =   "";
        String pass         =   "";
        String pass2        =   "";
        
        if(request.getContentType() != null){
            FirstName    =   request.getParameter("fname");
            LastName     =   request.getParameter("lname");
            login        =   request.getParameter("login");
            pass         =   request.getParameter("pass");
            pass2        =   request.getParameter("pass2");
            if(FirstName.length() < 2 ){
                fNameErr = "You mast Entert at least 2 charectar!";
                isValid = false;
            }else if(FirstName.matches("[a-zA-Z]")){
                fNameErr = "You mast Entert only letters!";
                isValid = false;
            }  
            
            if(LastName.length() < 2 ){
                lNameErr = "You mast Entert at least 2 charectar!";
                isValid = false;
            }else if(LastName.matches("[a-zA-Z]")){
                lNameErr = "You mast Entert only letters!";
                isValid = false;
            }  

            if( pass.length() < 3){
                passwordErr = "Password mast have at least 3 charectar";
                isValid = false;       
            } else if (!pass.equals(pass2)){
                passwordErr = "Password you enter not equals to password retype!";
                isValid = false;             
            } 
            
            
            DB.connect();

            if(login.length()<3){
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
        <link rel="stylesheet" href="ex4_ai.css" type="text/css" />
        <title>Register Page</title>
    </head>
    <body>
        <%@include file="userMenu.jsp" %>
        <form method="post" action="registration.jsp">
        <table class="registrationTable">
            <tr>
                <td class="registriontdfree"></td>
                <td colspan="2" class="register_title"><h1>Please enter data for resgistration</h1><%=loginErr%></td>
                <td class="registriontdfree"></td>
            </tr>
            <tr>
                <td class="registriontdfree"></td>
                <td class="registriontd">Please provide Login</td>
                <td class="registriontd"><input type="text" name="login" value="<%=login%>" /><br/><%=loginErr%></td>
                <td class="registriontdfree"></td>
            </tr>
            <tr>
                <td class="registriontdfree"></td>
                <td class="registriontd">Please provide password</td>
                <td class="registriontd"><input type="password" name="pass" value=""/><br/><%=passwordErr%></td>
                <td class="registriontdfree"></td>
            </tr>
            <tr>
                <td class="registriontdfree"></td>
                <td class="registriontd">Please retype password</td>
                <td class="registriontd"><input type="password" name="pass2" value=""/><br/></td>
                <td class="registriontdfree"></td>
            </tr>
            <tr>
                <td class="registriontdfree"></td>
                <td class="registriontd">Please provide First Name</td>
                <td class="registriontd"><input type="text" name="fname" value="<%=FirstName%>" /><br/><%=fNameErr%></td>
                <td class="registriontdfree"></td>
            </tr>
            <tr>
                <td class="registriontdfree"></td>
                <td class="registriontd">Please provide Last Name</td>
                <td class="registriontd"><input type="text" name="lname" value="<%=LastName%>" /><br/><%=lNameErr%></td>
                <td class="registriontdfree"></td>
            </tr>
            <tr>
                <td class="registriontdfree"></td>
                <td class="registriontd"></td>
                <td class="registriontd"><input type="submit" value="Register" /></td>
                <td class="registriontdfree"></td>
            </tr>
        </table>
        </form>
    </body>
</html>
