/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;


//==============================================================================
//==============================================================================
//==============================================================================
//``````````````````````````````````````````````````````````````````````````````

//                          SERVER  SERVER  SERVER

//``````````````````````````````````````````````````````````````````````````````
import java.net.*;
import java.io.*;
import java.util.ArrayList;

// Lab session: here are the things you should do
// 
// 1. catch command line error (missing  or wrong argument) and exit with an error message
// 2. write a cleanUp method that close streams and sockets no matter what! put calls to this method in the rigt place
// 3. have the server hold a list of connected clients (ConnectionHandler)
// 4. add at least one public method removeClient (ConnectionHandler client) that will be used in ConnectionHandler



//==============================================================================
     
public class ChatServer {
    //private static ArrayList<ConnectionHandler>  handlers = new ArrayList<ConnectionHandler>();
    private static ArrayList<String>  _data = new ArrayList<String>();
//==============================================================================
     

    public static void main(String[] args) {
        
        int port            = Integer.parseInt("2232");
        ServerSocket server = null;
        
        try {
            server          = new ServerSocket(port);
        }catch (IOException ioe) {
            System.err.println("Couldn't run server on port " + port);
            return;
        } 
        while(true) {
            try {
                System.out.println("Waiting for connection...");


                ConnectionHandler handler   =   new ConnectionHandler(server.accept(),_data);
                //handlers.add(handler);
                System.out.println("Creating User Handler Thread");
                try{
                    new Thread(handler).start();
                }
                catch (Exception ex){
                    System.out.println("Exception new Start is:" + ex.getMessage() );       
                }
            } catch (IOException ioe1) {
                System.out.println("Exception :" + ioe1.getMessage() );        
            }
        }
    }
/*
    private static void RunGetMessage() {

                     while ( true ) { 
                         for(int i =0; i < handlers.size();i++)
                         {
                             try{
                        String message = (handlers.get(i)).getMessage(); 
                        System.out.println("Got message: " + message); 
                            //sleep( 2000 );   
                             }catch( Exception ex){
                                 System.out.println(ex.getMessage()); 
                             }
                             
                         }

                    }
    }
          */             
     
   
}     

//==============================================================================


//==============================================================================


//==============================================================================
//==============================================================================
//==============================================================================