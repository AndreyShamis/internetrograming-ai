/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;


import java.net.*;
import java.io.*;
import java.awt.*;
import java.applet.*;
import java.applet.Applet;
import java.util.logging.Level;
import java.util.logging.Logger;
// Lab session: here is a list of things to be fixed in this client:
// 1. catch command line error (missing  or wrong arguments) and exit with an error message
// 2. write a cleanUp method that close streams and sockets no matter what! put calls to this methid in the rigt place
// 3. add a loop so that the client sends 10 times the requests to the server, put a sleep(1000) between each call.

// A client of an HelloServer
public class ChatClient implements Runnable{

    private  String strToSend = "";
    public volatile ChatPanel   chatPanel= null;
    private String UserName = "";
    public  ChatClient(ChatPanel  linkChatPane,String newUserName) {
        strToSend = "";
        UserName = newUserName;
        try{
            chatPanel = linkChatPane;
        }catch(Exception ex){
            System.out.println("Chat client constructor error:" + ex.getMessage());
        }
    }
    
    
    public synchronized void setStringTosend(String str){
        strToSend = str;
    }
    public void init() {
        
    } 
    public  void run(){
                
        String hostname     = "localhost";//args[0];
        int port            = 5555; //Integer.parseInt(args[1]);
        ClientReader clReader = null;
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);

        chatPanel.addFeedbackMsg("Start create socket");
        Socket connection = null;

        try{
            connection = new Socket(hostname, port);
        }catch (Exception ex){
            chatPanel.addFeedbackMsg("Socket failed\n" + ex.getMessage() + "\n");
        }
        chatPanel.addFeedbackMsg("Socket created\n");
            
        if(connection == null){
            chatPanel.addFeedbackMsg("Cannot connect to " + hostname + "." );
        } 
        else{
            //UserName =  getParameter("username");
            if(UserName.length() < 1 || UserName == null){
                UserName = "Test";
                chatPanel.addFeedbackMsg("Your name is:" + UserName + "\n");
            }

            chatPanel.addFeedbackMsg("Start chat\n");
            try {
                chatPanel.addFeedbackMsg("Start open streams\n");
                BufferedReader reader = 
                                new BufferedReader(new InputStreamReader(connection.getInputStream()));
                PrintWriter writer = 
                                new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
                //writer.println("Loged In USER"); // client nameargs[2]
                chatPanel.addFeedbackMsg("End open streams\nStart send name.\n");

                writer.println("" + UserName);
                writer.flush();
                clReader = new ClientReader(reader,chatPanel);
                new Thread(clReader).start();

                
                while(!strToSend.equals("/exit") ){
                    
                    if(strToSend != null && strToSend.length() > 0){

                        chatPanel.addFeedbackMsg("Start send string to server\n");
                        chatPanel.addFeedbackMsg("Send " + UserName + ":" +strToSend);
                        writer.println(strToSend); // client nameargs[2]//
                        writer.flush();  
                        chatPanel.addFeedbackMsg("END send string to server\n");
                        strToSend = "";
                    }
                    try {	
                        Thread.sleep(200);
                    } catch (InterruptedException ie) {
                        chatPanel.addFeedbackMsg("Error sleep\n");
                        System.out.println("Error sleep " );
                        ie.printStackTrace();
                    }  
                }
                
                reader.close();
                writer.close();
                connection.close();
                chatPanel.addFeedbackMsg("Exit chat!!!\n");
            } catch (IOException ioe1) {   
                chatPanel.addFeedbackMsg("Error :" + ioe1.getMessage() + "\n"); 
            }
            finally {
                try { 
                    connection.close();
                } catch (IOException ioe) {}
            } 
        }
    }
}