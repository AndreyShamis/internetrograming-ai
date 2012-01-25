<%-- 
    Document   : chatWindow
    Created on : 18.01.2012, 16:41:31
    Author     : Andrey Shamis AND Ilia Gaysinsky
--%>
<%
    String chatName =  (String)session.getAttribute("Login");
    if(chatName == null){
        chatName = "";
    }
%>
<APPLET CODE="chatclient.ClientApplet" class="right_window" archive="ChatClient.jar">
<PARAM NAME="ServerHost" VALUE="<%=request.getLocalAddr()%>">
<PARAM NAME="Port" VALUE="5555">
<PARAM NAME="username" VALUE="<%=chatName%>">
</APPLET>