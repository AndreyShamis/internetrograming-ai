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
//==============================================================================
//==============================================================================
public class ConnectionHandler  implements Runnable{

    // The connection with the client
    private Socket _connection;
    private PrintWriter _writer                 = null ;
    private BufferedReader _reader              = null;
    private volatile ArrayList<String> _data    = null;
    private static String _userName             = new String();
   
//==============================================================================    
    /**
     * Constructs a new ConnectionHandler.
     */
    public ConnectionHandler(Socket connection,ArrayList<String> data) {
        this._connection    = connection;
        _data               = data;
        _userName           = "";
        
    } 
      
//==============================================================================
    public void run() {
        
        StartChat();
        System.out.println("Exit RUN");
    }
    
    private synchronized void StartChat(){
        try {
		
            System.out.println("Connect-\n");
            
            
            _reader = new BufferedReader(new InputStreamReader(_connection.getInputStream()));
            _writer = new PrintWriter(new OutputStreamWriter(_connection.getOutputStream())); 
            new Thread(new ServerReader(_reader,_data,_userName)).start();
            new Thread(new ServerWriter(_writer,_data,_userName)).start(); 
            
/*            String buffer = "";

            buffer = _reader.readLine();
            while(!buffer.equals("")){
                buffer = _reader.readLine();
                if(buffer == null){
                    break;
                }
                else{
                    _data.add(buffer);
                }
            }  
            
             */
            System.out.println("Start exit run");
          //  _writer.close();
          //  _reader.close();
            //connection.close();

        } catch (IOException ioe) {
            System.out.println("Error in Handler : " + ioe.getMessage());
        } 
        

    }
    public void Stop(){
        try {
        _writer.close();
        _reader.close();
                } catch (IOException ioe) {
            System.out.println("Error in Handler : " + ioe.getMessage());
        } 
        System.out.println("Thread Finish");
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