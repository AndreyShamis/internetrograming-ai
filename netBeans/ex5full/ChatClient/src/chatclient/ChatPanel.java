/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

/**
 * ChatPanel.java 
 * this class is the gui implementation of the applet
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class ChatPanel extends Panel
{
	
	public TextField chatLineTxt = null;
	private TextArea chatTxt      = null;
	private TextArea feedbackTxt  = null;
	public Button    sendBtn     = null;
	
	private static final int numFeedbackRows = 6;
	private static final int numFeedbackCols = 40;
    public void init() {
        
    } 
	/** the ctor of the chatPanel create the 
	 *  a chat area and a feedback area below it
	 */

	public ChatPanel(){
		setLayout(new BorderLayout());
        //chat area panel
        Panel chatArea    = createChatAreaPanel();
        Panel feedbackPnl = createFeedbackPanel();
	     add(chatArea,BorderLayout.CENTER);
 	     add(feedbackPnl,BorderLayout.SOUTH);
	}
	
	private Panel createChatLinePanel(){
		Panel     chatLinePnl     = new Panel();
		chatLinePnl.setLayout(new FlowLayout());
		chatLineTxt     = new TextField(60);
		sendBtn         = new Button("Send");
		chatLinePnl.add(chatLineTxt);
		chatLinePnl.add(sendBtn);
		return chatLinePnl;
	}

	private Panel createChatAreaPanel(){
		
	    //creating sub panels
	    Panel chatLinePnl = createChatLinePanel();
        
	    //creating chat area panel
	    Panel chatArea = new Panel();
	    chatArea.setLayout(new BorderLayout());
        		
	    //chat text
	    chatTxt = new TextArea();
	    chatTxt.setEditable(false);
            
	
	    //adding all componet on the chatArea panel
	    chatArea.add(chatTxt,BorderLayout.CENTER);
	    chatArea.add(chatLinePnl,BorderLayout.SOUTH);
		
	    return chatArea;

	}
	
	/** the function crate a feedback panel
	 *  e.g. a label of feedback and a text area for feedbacks
	 */
	 
	private Panel createFeedbackPanel(){
		Panel feedbackPnl           = new Panel();
		feedbackPnl.setLayout(new BorderLayout());
		
		Label serverMsgLbl            = new Label("Feedback/debugging:");
		serverMsgLbl.setForeground(Color.white);
		serverMsgLbl.setBackground(Color.gray);
		feedbackTxt     = new TextArea(numFeedbackRows,numFeedbackCols);
		feedbackTxt.setEditable(false);
		feedbackPnl.add(serverMsgLbl,BorderLayout.NORTH);
		feedbackPnl.add(feedbackTxt ,BorderLayout.CENTER);
		feedbackPnl.setSize(50,50);
		return feedbackPnl;
	}
	
	
	/** toggle the state of chat line panel
	 *  e.g. - set chat line panel to be enabled / disable and 
	 *  @param flag is the state ot the chat line panel
	 */

	public void enableChatLinePanel(boolean flag){
		chatLineTxt.setEnabled(flag);
		sendBtn.setEnabled(flag);
	}
	
	/** the function will return the String in the chat line text field
	 */
	 public String getChatLine(){
	 	String retValue =  new String( chatLineTxt.getText() );
	 	retValue = retValue + "\n";
	 	chatLineTxt.setText("");
                chatLineTxt.requestFocusInWindow();
	 	return retValue;
	 }
	 
	 /** the function will append a message to the feedback text area
	  *  @param msg the message to be appended
	  */
	 public void addFeedbackMsg(String msg){
	 	feedbackTxt.append(msg);
	 }
	 
	 /** the function will set the text in the chat area
	  *  @param txt the text to be set in chat area
	  */
  	 public void setChatText(String txt){
	 	chatTxt.setText(txt);
	 }
	 
 	 /** the function will append a txt to the chat text area
	  *  @param txt the text to be appended
	  */

	 
  	 public void addChatText(String txt){
	 	chatTxt.append(txt);
	 }

	 
	 /** the function will clean all text in all text area's/fields
	  */
	  public void cleanAllTextArea()
	  {
	  	chatLineTxt.setText("");
	  	chatTxt.setText("");
	  	feedbackTxt.setText("");
	  }
  
};




