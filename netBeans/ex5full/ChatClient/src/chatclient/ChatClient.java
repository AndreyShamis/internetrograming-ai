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

    private     String          strToSend   = "";
    private     ChatPanel       chatPanel   = null;
    private     String          UserName    = "";
    private     ClientReader    clReader    = null;
    private     Socket          connection  = null;
    private     BufferedReader  reader      = null;
    private     PrintWriter     writer      = null; 
    private     String          hostname    = "";
    private     int             port        = 0; 
//============================================================================== 
    /**
     * 
     * @param linkChatPane
     * @param newUserName 
     */
    public ChatClient(ChatPanel  linkChatPane,String newUserName,String ServerAdrress,int ServerPort) {
        strToSend   = "";
        UserName    = newUserName;
        hostname    =   ServerAdrress;
        port        =   ServerPort;
        
        try{
            chatPanel = linkChatPane;
        }catch(Exception ex){
            System.out.println("Chat client constructor error:" + ex.getMessage());
        }
    }
    
//==============================================================================  
    /**
     * 
     * @param str 
     */
    public synchronized void setStringTosend(String str){
        strToSend = str;
    }
//==============================================================================
    public void init() {
        
    } 
    
//==============================================================================
    /**
     * 
     */
    public  void run(){
                


        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);

        chatPanel.addFeedbackMsg("Start create socket");


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
                 reader = 
                                new BufferedReader(new InputStreamReader(connection.getInputStream()));
                 writer = 
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
    
//==============================================================================
    /**
     * 
     */
    public void stop(){
        clReader.stop();
        try {
            reader.close();
            writer.close();
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}