package ex2;
import java.net.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//============================================================================
/**
 *  
 * This program reads from an input file a list of URL, and for each URL read 
 * the html and insert into a database statistics about the number of images in 
 * each page. Finally the program reads and summarize the inserted data in the 
 * form of:
 *          "http://www.cnn.com: 20"
 *          "http://www.google.com: 1"
 *
 * @author Andrey Shamis AND Ilia Gaysinksy
 * @version 7.7.7
 */
public class Ex2{
    
    /**
     *  Main functionn of ex2 exercise
     * @param args is the name of text file that content URLs list.
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
        int numOfIMG = 0;               // number of images counter
        boolean result = false;         //  db query result var
        ReadFile rf = null;             //  input class
        String [] sql = new String[2];  //  db query var
        String htmlStr = "";            //  Variable for data from web
        String url ="";                 //  url
        DerbyDataBase db    = null;
        
        try{
            if(args.length > 0){                //  read file by geven file name 
                rf = new ReadFile(args[0]);  
            }else{                              //  read default file
                rf = new ReadFile("input.txt");  
            }              
        }catch(FileNotFoundException fnfex){    // file not found
            System.out.println(fnfex.getMessage());
            System.exit(1);
        }
        try{                            //  Get ready the data base
            db = new DerbyDataBase("STATISTICS");
            db.connect();   //  connect to data base
        }catch(Exception dbex){
            System.out.println(dbex.getMessage());
            System.exit(3); 
        }
      
        while (true)               // Loop till the end of URL list
        {
            try{
                url = rf.getNextURL(); // get the next URL
            }catch(IOException e) {                  // IO Exception
                System.out.println(e.getMessage());
                break;
            }catch(Exception endFile){
                break;
            }
            // Get html file as string from current URL
            try{
                htmlStr = getHtmlAsStringFromURL(url);   
            }catch(Exception ex){
                System.out.println(ex.getMessage());
                continue;
            }
            // If success to get HTML 
            // Count number of images at current HTML
            numOfIMG = howManyIMGsAtGivenHTMLstr(htmlStr);  
            sql[0] = url;                       //  put url string
            sql[1] = Integer.toString(numOfIMG);//  put number of links
            result = db.saveRecord(sql);        //  save line at data base
        }
        try{
            rf.close();                         // close input reder file.
        }catch(IOException cl){
            System.out.println(cl.getMessage());// IO Exception
        }
        SendToSTDOUT stdout = new SendToSTDOUT(); 
        try{
            stdout.send(db.getListEntries());   //  send db data to print
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            System.exit(4); 
        }
        db.disconnect();                    //  close db connection
    }  // end of main
	
//============================================================================
    /**
     * Function for download web page content located on interntet
     * and can be finded by URL link given in paramter.
     * The function download the content put into string end return the string.
     * @param url of Internet site(WEB page).
     * @return whole HTML page in string form
     * @exception IOException On BufferedReader error.
     * @exception MalformedURLException On URL error.
     * @return return HTML file content as string from given URL address 
     */
    public static String getHtmlAsStringFromURL(String url) throws Exception
    {
        // check corection of begine of current URL
	if(!(url.startsWith("http") || url.startsWith("HTTP"))){
            throw new IOException("Attention: Incorect or broken url: " + url);
        }
        String  inputLine="",                   // hold lines of html file
                htmlStr = "";                   // content whole html file 
        BufferedReader br = null;               // bufferd reader
        boolean needClosebr = false;            // if buufer was opened

        try {
            URL workString = new URL(url);      //  Set URL object
            URLConnection uc = workString.openConnection(); //  Get data
            br = new BufferedReader(new 
                                    InputStreamReader(uc.getInputStream()));
            needClosebr = true;             //  Set for finally case
            while((inputLine = br.readLine()) != null){
                    htmlStr += inputLine;
            }
        }catch (MalformedURLException mue) {
            //  In case wich URL cannot be exist
            throw new MalformedURLException("Attention: Malformed URL "
                    + "detected: " + url);            
        }catch (IOException ioe) {
            //  In case wich cannot get content of given URL
            throw new IOException("Attention: Incorect or broken url: " + url);
        }finally{
            try{//  Check if must close buffer reader
                //  wich work only in case if url was success
                if(needClosebr){
                    br.close();                 //  close buffer reader
                }        
            } catch (IOException ex) {
            }
        }
        return htmlStr;                         //  return html text
    }

//============================================================================
    /**
     * Count number of images in given HTML string
     * @param htmlStr is string that content HTML file.
     * @return Number of images founded in given HTML string
     */
    public static int howManyIMGsAtGivenHTMLstr(String htmlStr){
        
        int counter = 0;        // images counter

        Pattern strMatch = Pattern.compile("<img.*?src=\"(.+?)\".*?>", 
                           Pattern.CASE_INSENSITIVE);	// Set pattern
        Matcher m = strMatch.matcher( htmlStr );        // Set matcher

        while (m.find()){       // Loop till the end of matches
                counter++;      // Promout images counter
        }
        return counter;         // Return images counter
    }
}
//============================================================================
//============================================================================
//============================================================================