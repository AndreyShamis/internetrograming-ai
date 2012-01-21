/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.net.*;
import java.io.*;
import java.util.Vector; 
import java.util.ArrayList;
/**
 *
 * @author ilia
 */
public class ServerWriter implements Runnable{
    private PrintWriter _writer                 = null;
    private int lastSend                        = 0;  
    private volatile  ArrayList<String> _data   = null;
    private volatile String _userName           = null;
    
    
    public ServerWriter(PrintWriter writer, ArrayList<String> data, String userName) {
        _writer     = writer;
        _data       = data;
        _userName   = userName;
    }     
    
    public synchronized void run(){

        while(true){
               // System.out.println("Send message to:" + connection.getRemoteSocketAddress().toString());
               // System.out.println("Start send to user :" + _data.size() + " messages.");
            for(int i = lastSend;i<_data.size();i++) {
                _writer.println(_data.get(i));
                _writer.flush();                 
                lastSend++;
                System.out.println("Start send to user <" + _userName + ">:" +  i + " of " + _data.size() + " messages.");
            }
            //System.out.println("Stop send messages.");
             //writer.println("w");
             _writer.flush();
             try {
                    Thread.sleep(1000);
             } catch (InterruptedException ie) {
                System.out.println("Error sleep ");
             }
        }
    }
}
