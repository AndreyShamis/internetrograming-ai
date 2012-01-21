/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.net.*;
import java.io.*;

public class ClientReader implements Runnable {
    private BufferedReader _reader = null;
    public volatile  ChatPanel   chatPanel = null;
    public ClientReader(BufferedReader reader,ChatPanel   GchatPanel){
        _reader = reader;
        chatPanel=GchatPanel;
    }
    public synchronized void run(){
        try {
            while(true){
                String reply = _reader.readLine();
                while (!reply.equals("") && reply != null){
                    chatPanel.addChatText(reply + "\n");
                    System.out.println(reply);
                    reply = _reader.readLine();            
                }
                Thread.sleep(50);
            }
        } catch (IOException ioe1) {
            chatPanel.addChatText("Error:" + ioe1.getMessage());
        } catch (InterruptedException ie) {
            chatPanel.addChatText("Error:" + ie.getMessage());
            System.out.println("Error ie ");
        }
    }
}
