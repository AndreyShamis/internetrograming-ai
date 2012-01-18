<%-- 
    Document   : userMenu
    Created on : Jan 12, 2012, 10:40:48 PM
    Author     : ilia
--%>
<table width="100%">
    <tr class="menu">
        <td> 
        <strong>
                <a class ="menu_link" href="login.jsp" >LogIn</a> | 
                <a class ="menu_link" href="logout.jsp">LogOut</a> | 
                <a class ="menu_link" href="registration.jsp">Registration</a>    | 
                <a class ="menu_link" href="index.jsp">Index</a>  |
        </strong>    
        </td>
        <td> <strong>Welcome <%=(String)session.getAttribute("Login")%></strong></td>
    </tr>
</table>
