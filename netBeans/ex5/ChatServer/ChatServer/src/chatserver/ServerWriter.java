/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.net.*;
import java.io.*;
import java.util.Vector; 
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ilia
 */
public class ServerWriter implements Runnable{
    private PrintWriter _writer                 = null;
    private int lastSend                        = 0;  
    private   ArrayList<String> _data   = null;
    
    
    public ServerWriter(PrintWriter writer, ArrayList<String> data) {
        _writer     = writer;
        _data       = data;
    }     
    
    public void run(){

        while(true){
               getDataFromUser();
        }
    }
    
    private synchronized void getDataFromUser(){
               // System.out.println("Send message to:" + connection.getRemoteSocketAddress().toString());
               // System.out.println("Start send to user :" + _data.size() + " messages.");
            for(int i = lastSend;i<_data.size();i++) {
                _writer.println(_data.get(i));
                _writer.flush();                 
                lastSend++;
                System.out.println("Start send to user :" +  (i+1) + "/" +( _data.size()) + " messages.");
            }
            this.notifyAll();
//        try {
//            //System.out.println("Stop send messages.");
//             //writer.println("w");
//             //_writer.flush();
//            this.wait(10);
//             //try {
//             //       Thread.sleep(500);
//             //} catch (InterruptedException ie) {
//             //   System.out.println("Error sleep ");
//             //}    
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ServerWriter.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    public void stop(){
        
        System.out.println("Thread ServerWriter exit Stop");
    }
}
