<%-- 
    Document   : usersList.jsp
    Created on : Jan 12, 2012, 11:41:13 PM
    Author     : ilia
--%>
<jsp:useBean id="Users" class="ex4.UsersList"  scope="application"/> 
<%
  String [] UsersList=Users.AllUsersList();
 
for(int i=0;i<UsersList.length;i++){
    %>
    User:<%=UsersList[i]%><br/>
    <%
}
%>
Size is <%=UsersList.length%>