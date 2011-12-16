package ex2;
import java.util.ArrayList;

/**
 * This class is interface for database
 * @author Andrey Shamis AND Ilia Gaysinksy
 */
public interface DataBase {
    /**
     * Function wich provide connection to data base
     * @return true if connect success
     * @throws Exception ocured when the clas not ready to work with database
     * can happen when the problem in qery template
     */
    public boolean connect() throws Exception ;
//=============================================================================    
    /**
     * Function wich close the connection created by
     * funvtion connect. 
     */
    public void disconnect();
//=============================================================================
    /**
     * Function for save information to data base
     * @param records array of url and number of images
     * @return true if query executing success
     */
    public boolean saveRecord(String [] records);
//=============================================================================
    /**
     * Function wich execute sql query for all entryes in table
     * @return list of lists of strings
     */
    public ArrayList<ArrayList<String>> getListEntries()  throws Exception;
//============================================================================= 
    /**
     * Function wich rovide otions to update the data base
     * Not used in version ex2. Still in this class for next exercises
     * @param records array of url and number of images
     * @return true if success
     */
    public boolean editRecord(String [] records);
    
}
//=============================================================================
//=============================================================================
//=============================================================================
    
    
