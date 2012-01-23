/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;
// Handles a _connection of a client to an HelloServer.
// Encapsulates the task of talking with the client in 
// the 'hello' protocol

// Lab session: here are the things you should do
// 1. write a cleanUp method that close streams and sockets no matter what! put calls to this method in the rigt place
// 2. turn the _connection handler into an infinite loop that always sends an answer to incoming client string
// 3. take care of removing "this" from the list of clients in the server when it terminates (normally or not)
// (you need to pass the Server reference somehow)

import java.net.*;
import java.io.*;
import java.util.Vector; 
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
//==============================================================================
//==============================================================================
public class ConnectionHandler  implements Runnable{

    // The connection with the client
    private Socket _connection;
    private PrintWriter _writer                 = null ;
    private BufferedReader _reader              = null;
    private volatile ArrayList<String> _data    = null;
    private ServerReader _sr                     = null;
    private ServerWriter _sw                     = null;
    private Map<String,Integer> _usersNames      = null;
   
//==============================================================================    
    /**
     * Constructs a new ConnectionHandler.
     */
    public ConnectionHandler(Socket connection,ArrayList<String> data, Map<String,Integer> usersNames) {
        this._connection    = connection;
        _data               = data;
        _usersNames         = usersNames;
        
    } 
      
//==============================================================================
    public void run() {
        
        StartChat();
        System.out.println("Exit RUN");
    }
    
    private  void StartChat(){
        try {
		
            System.out.println("Connect-\n");  
            _reader = new BufferedReader(new InputStreamReader(_connection.getInputStream()));
            _writer = new PrintWriter(new OutputStreamWriter(_connection.getOutputStream())); 
            new Thread(_sr = new ServerReader(_reader, _data)).start(); 

            new Thread(_sw = new ServerWriter(_writer, _data)).start();    
                
            //ReadImportantData();
            System.out.println("Start write username\n");
            while(_sr.getUserName().length()<1){
                try {

                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Integer temp;
                if(_usersNames.containsKey(_sr.getUserName())){
                    temp = _usersNames.get(_sr.getUserName());
                    temp +=1;
                    _usersNames.put(_sr.getUserName(),temp);        
                }
                else {
                    temp = 1;
                    _usersNames.put(_sr.getUserName(),temp);
                }
            while(_reader.read() != -1){
                try {

                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            System.out.println("Start exit run");
            this.Stop();   
            
            
        } catch (IOException ioe) {
            System.out.println("Error in Handler : " + ioe.getMessage());
        } 
        

    }
    public void Stop(){
        try {
            _writer.close();
            _reader.close();
            _connection.close();
        
        } catch (IOException ioe) {
            System.out.println("IMPORTANT!!!.Error in Handler : " + ioe.getMessage());
        } 
        
        Integer temp;
        temp = _usersNames.get(_sr.getUserName());
        temp -=1;

        _usersNames.put(_sr.getUserName(),temp);   
        if(temp == 0){
            _usersNames.remove(_sr.getUserName());
            if(_usersNames.size() == 0){
                _data.clear();
            }
        }

        
        
        
        _sr.stop();
        _sw.stop();
        
        System.out.println("Thread ConnectionHandler Finish");
    }

    private synchronized void ReadImportantData(){
        this.notifyAll();
        try {
            this.wait(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            while(_sr.getUserName() != null &&  _sr.getUserName().length() < 1 ){
                try {
                    Thread.sleep(100);
                    System.out.println("Sleep\n");
                } catch (InterruptedException ex) {
                    System.out.println("huyna\n");

                }
                this.notifyAll();
            }   
    }
//==============================================================================
    
    // Called by Main Server 
  /*  public synchronized String getMessage() 
        throws InterruptedException { 
        notify(); 
        while ( mess.length() < 1 ) 
            wait(); 
        String message =mess;// (String)messages.firstElement(); 
        
        messages.removeElement( message ); 
        mess = "";
        return message; 
    }  */
//==============================================================================


}
//==============================================================================