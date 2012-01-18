<%-- 
    Document   : usersList.jsp
    Created on : Jan 12, 2012, 11:41:13 PM
    Author     : EX4 Andrey Shamis AND Ilia Gaysinsky
--%>
<jsp:useBean id="Users" class="ex4.UsersList"  scope="application"/> 
<%
  String [] UsersList=Users.AllUsersList();
 
for(int i=0;i<UsersList.length;i++){%>
<strong><a class="userNameinList" href="#"><%=UsersList[i]%></a></strong><br/>
    <%}%>