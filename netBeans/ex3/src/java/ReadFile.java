
import java.io.*;
import java.lang.Object.*;

/**
 * Read text file that contain URLs.
 * @author Ilia Gaysinksy AND Andrey Shamis
 */
public class ReadFile  implements ReadInput {

    private BufferedReader br = null;               // bufferd reader
    private FileReader fr = null;
    
    /**
     * constructor
     * @throws FileNotFoundException when file not found
     */
    public ReadFile(String fileName) 
            throws FileNotFoundException {
    
        //  Try open file
	try {
            System.out.println(fileName);
            fr = new FileReader(fileName);      // file reader
            
            br = new BufferedReader(fr);                // bafer reader
        } catch(FileNotFoundException fN) {     // file not funded exception
            throw new FileNotFoundException("ERROR: Missing or corrupted "
                    + "input file:" + fN.getMessage()); 
	} 
    }
   /**
    * recive next line from file as string
    * @return Next line in text file of URLs
    * @throws FileNotFoundException this exception happen when same parameters
    * no correct,like file not exist
    * @throws IOException this exception happen when same error ocured in
    * reading file
      @throws Exception when the file ends - it is usful exception for 
    * indication to main that the file is ended.
    */  
    public String getNextURL() throws IOException,Exception {
        
        try{
            String line = br.readLine();
            if(line == null){
                throw new Exception("end of file"); // indication of end of file
            }
            return (line);
        }
        catch(IOException e) {                  // IO Exception
            throw new IOException("ERROR: Failed to read next line " + 
                            "Missing or corrupted input file!" + 
                            "Will continue to update data base till that poit"); 
	}
    }
    /**
     * close bafer reader and file reader
     * @throws IOException when fail to close bafer reader or file reader
     */
    public void close() throws IOException{
        try {
                    br.close();
                    fr.close();
        } catch (IOException ioe) {             // IO Exception              
            throw new IOException("ERROR: An IOException happened while"
                                                             + " closing.");
        }
    }
    
    
    
    
    
    
} // end of class definition
//=============================================================================
//=============================================================================
//=============================================================================