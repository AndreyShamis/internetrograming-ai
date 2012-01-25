/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.net.*;
import java.io.*;
/**
 * 
 * @author Andrey Shamis and Ilia Gaysinsky
 */
public class ClientReader implements Runnable {
    private     BufferedReader  _reader         = null;
    private     ChatPanel       _chatPanel      = null;
//==============================================================================
    public ClientReader(BufferedReader reader, ChatPanel chatPanel){
        _reader     = reader;
        _chatPanel  = chatPanel;
    }
//==============================================================================
    public void run(){
        try {
            while(true){
                String reply = _reader.readLine();
                while (!reply.equals("") && reply != null){
                    _chatPanel.addChatText(reply + "\n");
                    reply = _reader.readLine();            
                }
            }
        } catch (IOException ioe1) {
            _chatPanel.addChatText("Error (Reader function run):" + ioe1.getMessage());
        
        }
    }
//==============================================================================
    public void stop(){
        System.out.println("Client reader thread stop.");
    }
}
