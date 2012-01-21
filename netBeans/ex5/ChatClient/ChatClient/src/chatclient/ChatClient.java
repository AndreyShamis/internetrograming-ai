/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;


import java.net.*;
import java.io.*;
import java.awt.*;
import java.applet.*;
// Lab session: here is a list of things to be fixed in this client:
// 1. catch command line error (missing  or wrong arguments) and exit with an error message
// 2. write a cleanUp method that close streams and sockets no matter what! put calls to this methid in the rigt place
// 3. add a loop so that the client sends 10 times the requests to the server, put a sleep(1000) between each call.

// A client of an HelloServer
public class ChatClient implements Runnable{

    private  String strToSend = "";
    public volatile ChatPanel   chatPanel= null;
    public ChatClient(ChatPanel   GchatPanel) {
        chatPanel = GchatPanel;
    }
    
    public void setStringTosend(String str){
        strToSend = str;
    }
    public void init() {
        
    } 
    public synchronized void run(){
                
        String hostname     = "localhost";//args[0];
        int port            = 2232; //Integer.parseInt(args[1]);
        String UserName     = "";
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);
        
        System.out.print("Enter your name:");
        chatPanel.addFeedbackMsg("Enter your name:");
        //try{
       //     UserName = in.readLine();
       // } catch (IOException ioe1) {
    	//}
        chatPanel.addFeedbackMsg("Start create socket");
        Socket connection = null;
      //  try {
            try{
                connection = new Socket(hostname, port);
            }catch (Exception ex){
                chatPanel.addFeedbackMsg("Socket failed\n" + ex.getMessage() + "\n");
            }
            chatPanel.addFeedbackMsg("Socket created\n");
       // } catch (IOException ioe) {
            
       //     chatPanel.addFeedbackMsg("Connection failed\n" + ioe.getMessage() + "\n");
       //     System.err.println("Connection failed"); 
       //     return;
      //  }
            
            
        //UserName =  getParameter("username");
        if(UserName.length() < 1 || UserName == null){
            UserName = "Test";
            chatPanel.addFeedbackMsg("Your name is:" + UserName);
        }

		//=========================
        chatPanel.addFeedbackMsg("Start chat");
        try {
            BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter writer = 
                            new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            //writer.println("Loged In USER"); // client nameargs[2]
            writer.println(UserName); //############################################################################
            writer.flush();
            new Thread(new ClientReader(reader,chatPanel)).start();


            while(!strToSend.equals("/exit")){
                
                if(strToSend != null && strToSend.length() > 0){
                    chatPanel.addFeedbackMsg("Send " + UserName + ":" +strToSend);
                    writer.println(strToSend); // client nameargs[2]//
                    writer.flush();  
                    strToSend = "";
                }
                try {	
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    chatPanel.addFeedbackMsg("Error sleep");
                    System.out.println("Error sleep " );
                    ie.printStackTrace();
                }       
            }
  /*          for(int i=0;i<20;i++){

                String input = "";
                input = in.readLine();
                writer.println(UserName + ":" +input); // client nameargs[2]
                writer.flush();

                try {
                //System.out.println("Start sleep " + i );	
                    Thread.sleep(200);
                //    System.out.println("Stop sleep " + i );
                } catch (InterruptedException ie) {
                    System.out.println("Error sleep " + i );
                    ie.printStackTrace();
                }            
            }*/
            reader.close();
            writer.close();
             chatPanel.addFeedbackMsg("Exit chat!!!\n");
    	} catch (IOException ioe1) {   
            // NADA 4ETA SDELAT  
    	}
    	finally {
            try { 
                connection.close();
            } catch (IOException ioe) {}
    	} 
    }
}