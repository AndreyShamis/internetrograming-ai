
import java.io.*;
import java.lang.Object.*;
/**
 * That interface for Class that Read some input.
 * @author Ilia Gaysinksy AND Andrey Shamis
 */
public interface ReadInput {
   /**
    * recive next line from file as string
    * @return line from file as string
    * @throws FileNotFoundException this exception happen when same parameters
    * no correct,like file not exist
    * @throws IOException this exception happen when same error ocured in
    * reading file
      @throws Exception when the file ends - it is usful exception for 
    * indication to main that the file is ended.
    */
    public String getNextURL() throws IOException,Exception;
    
    /**
     * close bafer reader and file reader
     * @throws IOException when fail to close bafer reader or file reader
     */
    public void close() throws IOException;

}
//=============================================================================
//=============================================================================
//=============================================================================