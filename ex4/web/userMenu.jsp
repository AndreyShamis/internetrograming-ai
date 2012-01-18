<%-- 
    Document   : userMenu
    Created on : Jan 12, 2012, 10:40:48 PM
    Author     : Andrey Shamis AND Ilia Gaysinsky
--%>
<%
    String LogName =  (String)session.getAttribute("Login");
    if(LogName == null){
        LogName = "";
    }
%>
<table class="menu">
    <tr>
        <td class="tdUserMenuLeft"> 
        <strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <%if(LogName.length() < 1){%>
                <%if(page.toString().matches(".*registration.*")){ %>
                    <a class="menu_link" href="login.jsp" >LogIn</a>
                <%}else{%>
                    <a class="menu_link" href="registration.jsp">Registration</a>
                <%}%>
            <%}else{%>
            <a class ="menu_link" href="logout.jsp">LogOut</a> 
            <%} %>  
        </strong>    
        </td>
        <td class="tdUserMenuRight"><strong> 
<%
                if(LogName.length() < 1){
        %>Please login<%
                }else{
        %>Welcome <%=LogName%><%
                }

%>                             
        </strong></td>
    </tr>
</table>
