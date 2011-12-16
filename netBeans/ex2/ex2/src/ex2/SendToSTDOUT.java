package ex2;
import java.util.*;

/**
 * Send a given list of lists to STDOUT.
 * @author Ilia Gaysinksy AND Andrey Shamis
 */
public class SendToSTDOUT implements SendOutput { 

    /**
     * Send a given list of lists to STDOUT.
     * @param db is a given data base that need to be print on STDOUT
     */
    public void send (ArrayList<ArrayList<String>> db)  {
	
        //for internal list
        ArrayList<String> dbLine = new ArrayList<>();  
        // for iteration on list of lists
        Iterator<ArrayList<String>> itr = db.iterator();
        
        System.out.println("\nSummary of updated Data Base:"
                + "\n-----------------------------");
        
        while (itr.hasNext()){   // Loop tiil the end of list of lists
            dbLine = itr.next(); // take current list from list of lists
            System.out.println(dbLine.get(0) + ": " + dbLine.get(1));
        }	        
    } // end of class definition
}
//=============================================================================
//=============================================================================
//=============================================================================
    
    
