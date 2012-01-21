/**
 * ChatApplet.java 
 * to be extended:
 * respond to user events - in the main thread -
 * communication with the server - in the second thread -
 */

import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.event.ActionEvent;

/** The applet will connect to a server with default port 5555. The port is specified
 * in the HTML parameters (parameter name is serverPort). You should add the Runnable interface
 * and implement the thread that will handle the connection with your server.
 */
public class ChatApplet extends Applet {

    private  ChatPanel   chatPanel = null;

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
    }
    
    private synchronized void action(ActionEvent event) {
    	String request;
    	
    	if(event.getSource() == chatPanel.sendBtn) //send button - send a chat line
    	{
    		request = chatPanel.getChatLine();
		// you can put debugging trace in this text area
   		chatPanel.addFeedbackMsg("User is typing: " + request);
		//...
    	}
    }
}


