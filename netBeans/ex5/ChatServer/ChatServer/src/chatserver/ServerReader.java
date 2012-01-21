/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.net.*;
import java.io.*;
import java.util.Vector; 
import java.util.ArrayList;

public class ServerReader implements Runnable {
    private BufferedReader _reader              = null;
    private volatile  ArrayList<String> _data   = null;
    private volatile String _userName           = null;
    public ServerReader(BufferedReader reader,ArrayList<String>  data, String userName) {
            _reader     = reader;
            _data       = data;
            _userName   = userName;
        
    }    
    public synchronized void run(){
    
        try {

            String buffer = "";
            

            buffer = _reader.readLine();
           
            _userName = buffer;
            
            _data.add("## " + _userName + " ## has entered the chat!!!! ##");
            //System.out.println(_userName + "taskjdkanksnd");
            //buffer = _reader.readLine();
            while(_reader != null){
                buffer = _reader.readLine();
                if(buffer == null){
                   // break;
                }
                else{
                    _data.add(buffer);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    System.out.println("Error sleep ");
                }
            }  

        } catch (IOException ioe) {
            
            System.out.println("Error in Handler : " + ioe.getMessage());
        } 
  
          System.out.println("Thread exit run");
    }
}
