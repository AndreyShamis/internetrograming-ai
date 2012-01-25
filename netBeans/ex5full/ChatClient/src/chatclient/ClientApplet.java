/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

/**
 * ChatApplet.java 
 * to be extended:
 * respond to user events - in the main thread -
 * communication with the server - in the second thread -
 */

import java.applet.*;
import java.awt.*;
//import java.util.*;
//import java.net.*;
//import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/** The applet will connect to a server with default port 5555. The port is specified
 * in the HTML parameters (parameter name is serverPort). You should add the Runnable interface
 * and implement the thread that will handle the connection with your server.
 */
public class ClientApplet extends Applet {

    public  ChatPanel   chatPanel = null;
    public ChatClient chCl = null;

    public void init() {
        setLayout(new BorderLayout());
        //connecting the panel
        chatPanel = new ChatPanel();
        add(chatPanel, BorderLayout.CENTER);
        //connecting events to action function
        chatPanel.sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                action(evt);
            }
        });
        chatPanel.chatLineTxt.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    actionKey(e); 
                }
            }
        });
        try{
            String UserName         = getParameter("username");
            String ServerAdrress    = getParameter("ServerHost");
            int ServerPort          = Integer.parseInt(getParameter("Port")) ;
            if(UserName == null || UserName.length() < 1 ){
                chatPanel.addChatText("Cannot get UserName. \nPlase refresh your browser.\n");
            }else if( ServerPort <=1024 || ServerPort >= 65535 ){
                chatPanel.addChatText("Some error.Please refresh you browser.\n");
            }else{
                chCl = new ChatClient(chatPanel,UserName,ServerAdrress,ServerPort) ;
                new Thread(chCl).start();  
                
            }

        }catch(Exception ex){
            System.out.println("Error : " + ex.getMessage());
            chatPanel.addFeedbackMsg("Error : " + ex.getMessage() );
        }
        chatPanel.chatLineTxt.requestFocusInWindow();   //  TODO :: Don`t work
    }
    
    @Override
    public void destroy(){
        
        chCl.stop();
    }

    private synchronized void action(ActionEvent event) {
    	if(event.getSource() == chatPanel.sendBtn){
            getTextFromTextArea();
    	}
    }
    private synchronized void actionKey(KeyEvent event) {
    	if(event.getSource() == chatPanel.chatLineTxt) {
            getTextFromTextArea();
    	}
    }
    private synchronized void getTextFromTextArea(){
        String request;
        request = chatPanel.getChatLine();
        // you can put debugging trace in this text area  		
        chatPanel.addFeedbackMsg("User is typing: " + request );
        chCl.setStringTosend(request);
        chatPanel.addFeedbackMsg("Set end User is typing:\n" );
    }
}
