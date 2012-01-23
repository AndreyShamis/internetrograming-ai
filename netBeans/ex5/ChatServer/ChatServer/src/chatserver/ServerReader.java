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
import java.util.HashMap;
import java.util.Map;

public class ServerReader implements Runnable {
    private volatile BufferedReader _reader              = null;
    private  ArrayList<String> _data   = null;
    private  String _userName           = "";
    //private Map<String,Integer> _usersNames     = null;
    public ServerReader(BufferedReader reader,ArrayList<String>  data) {
            _reader     = reader;
            _data       = data;
        //_usersNames =   usersNames;
        
    }    
    public  void run(){
        try {
            System.out.println("BEFOR read");
            String buffer      =  _reader.readLine();
            _userName   = buffer;
            System.out.println("After read");
            _data.add("#" + _userName + " # has entered the chat!");
            
            System.out.println("BEFOR WHILE");
            while(_reader != null){
                buffer = _reader.readLine(); 
                if(buffer != null && buffer.length() > 0){
                    AddNewMessageToDB(_userName + ": "  + buffer);
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
        stop();
    }
    private synchronized void AddNewMessageToDB(String data){
        try {
            this.wait(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        _data.add(data);
        
        this.notifyAll();  
    }
    public synchronized String getUserName(){  
        return _userName;
    }
    
    public   String readUserName(){  
        String buffer = "";
        try {
            buffer      = _reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(ServerReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        _userName   = buffer;
        return(buffer);
    }
        
    public void stop(){
        _data.add("#" + _userName + "#: has leave the chat.");
        System.out.println("Thread ServerReader exit Stop:" + _userName);
    }
}
