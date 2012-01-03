/*
 * Voting class. This work home exercise 3.
 * 
 */
//==============================================================================
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

//==============================================================================
//==============================================================================
//==============================================================================
/**
 * Voting class. This home work exercise 3.
 * @author Andey Shamis & Ilia Gaysinsky
 */
@WebServlet(name = "Voting", urlPatterns = {"/Voting"})

public class Voting extends HttpServlet {

//==============================================================================
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cookies manipulation
        cookies = request.getCookies();

        // Header manipulation
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); //  get response out object
        
        if(request.getContentType() != null ){                                      //HASER TEUD
            VotingProccess( request, response);
            response.setStatus(301);
            response.setHeader("Location",ServletPATH);
            response.setHeader( "Connection", "close" );
        }else{
            out.println(PrepareHTMLbody());
        }
        
        out.close();
    }
//==============================================================================
/**
     * Function wich print HTML out of main page include all sections
     * @return 
     */
    private String PrepareHTMLbody()
    {
        String retHTML = "";            //  Return variable
        //  Buld the page
        retHTML +="<html>\n<head>\n"
                + "<title>Servlet Voting</title>\n"
                + "<link rel='stylesheet' "
                + "type='text/css' href='voting.css' />\n"
                + "</head>\n\n<body>\n"
                + "<table width='100%'><tr><td></td>"
                + "    <td class='votingSection'style='width:80%;'>"
                + "<h1>  EX3 :: Voting servlet</h1></td>"
                + "    <td></td></tr>";       
        //  Printout the forms of Voting Form and Result form
        retHTML +="<tr><td></td><td>"+PrintVotingForm()+"</td><td></td></tr> ";
        retHTML +="<tr><td></td><td>"+PrintVotingResults() +"</td><td></td></tr>";    
        retHTML +="</table></body></html>"; //  Close body and HTML    
        return retHTML;                 //  return value
    }
//==============================================================================
  /**
   * Function wich prepare voting results for out into the browser screen.
   * But to know the HTML will be retunded to script wich call to this function
   * @return The HTML builded for voting results
   */
    private  String PrintVotingResults(){
        String retHTML  = "";               //  HML will be returned
        String userInfo = "";               //  Information for user
        Set s           = URLs.entrySet();  //  Set of map used for DS
        Iterator itr    = s.iterator();     //  Iterator used in print
        URLclass tempURL= null;             //  temp object for each URLs
        
        // If input file is missing - print error
        if(fileNotFound){
            userInfo +="<h3>ERROR: Input file of URLs is Missing</h3>";  
        }
        
        // If input file is empty - print error
        else if(fileIsEmpty){
            userInfo +="<h3>ERROR: Input file of URLs is Empty</h3>";
        }
        //  Prepare out for table of results
        else{
            userInfo +="<h3>Total number of persons that have votes is: "+VoteCounter+" person .<em> Note:"
                     + " It counts Persons that have vote (NOT VOTS it self!!!!) </em></h3>";
        }
        //  Prepare out for table of results
        retHTML +=userInfo
                + ""
                + "<div class='votingSection'>"
                + "<table width='100%'>";

        //  For each entry in data structure
        while(itr.hasNext()){
            //  Get map entry
            Map.Entry  tempMapEntry =(Map.Entry) itr.next();
            //  Convert map entry to normal class
            tempURL = (URLclass)tempMapEntry.getValue();
            //  Convert encoded URL name
            String urlName = URLDecoder.decode(tempURL.GetURLName());
            //  Start build HTML
            retHTML +="<tr>\n<td>\n"
            + "   <div class='votingFormUrlLinkBox'><strong>"+ urlName+"</strong><br/> *"
            + " <label class='urlDesc'>Votes : _. Points : "+tempURL.getPoints()+""
            + " / "+(int)tempURL.getPoints()+" (Int / Double)</label></div>\n"        //  TODO :: Check this KETA code <int>
            + "</td>\n<td style=\"width:80%;\">\n"
            + "   <div class=\"bar_wrap\">\n"
            + "       <div class=\"bar\" style=\"width:" + tempURL.getPoints()*10  + "%\">\n";
            //  If points value more than 0.5 print it into bar
            if(tempURL.getPoints() >= 0.5){
                retHTML+="<div class=\"right\">" +tempURL.getPoints()+ "</div>\n";
            }
            //  Close HTML for this entry
            retHTML+= "</div>\n</div>\n</td>\n</tr>";        
        } 
        retHTML +="</table></div>";     //  Close HTML for results
        
        
        return(retHTML);                //  Return builded HTML
  }
//==============================================================================
/**
     * This function do the process of voting. Calculate the data posted by user
     * @param request   variable contain posted data
     * @param response  variable will contain new cookies wich be returned to user
     */
    private void VotingProccess(HttpServletRequest request,
            HttpServletResponse response){
        boolean CookiesFlag = false;    //  Variable for know if ned set cookies
        //  String arrays for posted data
        String [] resultOfVoting    = request.getParameterValues("grade_value"); 
        String [] resultOfVotingURL = request.getParameterValues("url"); 
            
        //  Check if posted arrays contain something
        if(resultOfVoting == null || resultOfVotingURL == null){
            return;                     //  exit from function
        }
        //  Search for cookies wich tall as if user was voted one or more
        String searchString =getCookieValue(cookies,"HaveVoted"); 
        if(!searchString.equals("true")){
            //  If not found create 
            Cookie votedCookie = new Cookie("HaveVoted", "true");
            response.addCookie(votedCookie); 
            VoteCounter++;              //  Increase the counter
        }
        //  For each entry in post of URLs array
        for(int i = 0;i<resultOfVoting.length;i++){
            int points=-1 ;             //  temp variable
            CookiesFlag = false;        //  cookies flag
            String ErrorCode = "";      //  Error code for cookies
            //  Decode encoded URLs
            String url = URLDecoder.decode(resultOfVotingURL[i]);

            try{
                //  Convert points value into int from string
                points = Integer.parseInt(resultOfVoting[i]) ;
                CookiesFlag = true;     //  if success set this flag
            }catch(Exception ex){       //  else set error code 1
                ErrorCode = "1";
            }
            //  If success convert the string into int
            if(CookiesFlag){
                //  Get class of some URLs from map by UL name
                URLclass  tempURL=URLs.get(resultOfVotingURL[i]);
                //  Check if the object not null
                if(tempURL != null){
                    try{
                        //  Set new points
                        tempURL.SetPoints(points );
                        Cookie userCookie = new Cookie(resultOfVotingURL[i], "true");
                        userCookie.setMaxAge(CookiesTime);  //  time of cookies
                        response.addCookie(userCookie);     //  add cookies
                    }catch(Exception ex){
                        //  This section can happend if the value not in the range
                        ErrorCode = "2";                    //  set error code
                        CookiesFlag = false;                //  set flag
                    }
                }
            }
            //  This section happen if user was vote for one or more url 
            //  only once for eacvh user
            if(!CookiesFlag){
                //  Create cookies
                Cookie userCookie = new Cookie(resultOfVotingURL[i], ErrorCode);
                userCookie.setMaxAge(8);        //  Time to live
                response.addCookie(userCookie); //  Add cookie
            }
        }
    }
 
//==============================================================================   
/**
     * Functiob wich print the voting form 
     * @return The html bulded voting form
     */
  private String PrintVotingForm(){
      
      String    retHTML     = "";   //  Return value
      URLclass  tempURL;            //  temp variable used in print each url
      boolean   flagOdd     = false;//  odd variable used for each lines
      Set s                 = URLs.entrySet();  //  data structure
      Iterator itr          = s.iterator();     //  iterator for data structure
      boolean ReturnForm = false;   //  will be set to true if have some entry 
      //    for wich the user must vote. If no, the form not be retuned
      
      retHTML = "" +
        "        <form method='post' action='Voting' class='VotingForm' name='VotingForm'>\n" +
        "            <table width='100%'>\n" +
        "                <thead>\n" +
        "                    <tr class='votingSection'>\n" +
        "                        <th>URL</th>\n" +
        "                        <th>Set grade . The value must be between 0 to 10.</th>\n" +
        "                    </tr>\n" +
        "                </thead>\n" +
        "                <tbody>\n"; 


        while(itr.hasNext()){
            String searchString = "";   //  value in cookes ret
            //  Get next entry in data structure
            Map.Entry  tempMapEntry =(Map.Entry) itr.next();
            //  Get value in next entry
            tempURL = (URLclass)tempMapEntry.getValue();
            //  Decode the encoded URLs
            String urlName = URLDecoder.decode(tempURL.GetURLName());
            //  Search vookies for this URL entry
            searchString =getCookieValue(cookies,tempURL.GetURLName()); 

            if(!searchString.equals("true")){
                String lineColor="";            //  css in next line
                // Odd lines manipulations
                if(flagOdd){
                    lineColor = "cssLineOdd";   //  Set css for odd line
                    flagOdd =false;
                }else{
                    lineColor = "cssLineNotOdd";//  Set css for not odd line
                    flagOdd = true;
                }
                //  Print into variable html text
                retHTML += "<tr><td class='"+lineColor+"'>"
                        + "<span class='VotingFormUrlsNames'>"
                        + urlName+"</span></td>\n";
                retHTML +="<td class='VotingFormPoints "+lineColor+"'>\n"
                        + "<input class='PointsBox' type='text' "
                        + "maxlength='2' name='grade_value' "
                        + "value=\"\" />\n"
                        + "<input  type='hidden' name='url' "
                        + "value='"+tempURL.GetURLName()+"' />\n";
                //  Check if the prev value not was a some number
                if(searchString.equals("1")){
                    retHTML += "<em class='informationMessage'>"
                            + "Please enter value</em><br/>";
                }
                //  Check if the previous value was in bad range
                if(searchString.equals("2")){
                    retHTML += "<strong class='error'>Bad value"
                            + ".The value must be "
                            + "between 0 to 10</strong><br/>";
                }
                retHTML +=   "</td>\n</tr>\n";  //  Close table tr
                ReturnForm = true;  //  have return the form
            }
        } 
        //  Close table
        retHTML+="</tbody>\n" +
            "            </table>"
                + "<div class='votingSection' style='width:100%'>"
                + "<input style='width:200px;' type='submit' "
                + "value=' Press to Vote '  /></div>"
                + "</form>\n";
        
        if(ReturnForm){         //  If have same entry to vote return the table
            return (retHTML);   //  return builded html text
        }else{                  //  else if no any URLs to vote
            return("");         //  Not return the table
        }
  }
//==============================================================================
  /**
   * 
   * @throws ServletException 
   */
    @Override
    public void init() throws ServletException {
        int counter = 0;      // count num of URLs have read from input file
        String FilePath = "";
        FilePath = getServletContext().getRealPath("") // get input file fath
                + File.separatorChar 
                + getServletConfig().getInitParameter("urlsFile").toString();
        
        ReadFile rf = null;             //  input class
        String url ="";                 //  url
                             //  read default file
        try { 
            rf = new ReadFile(FilePath);    // create file reader
        } catch (FileNotFoundException ex) {
            fileNotFound = true;
            Logger.getLogger(Voting.class.getName()).log(Level.SEVERE, null, ex);
        }
        URLs = new HashMap<String,URLclass>();  // Set Hash Map to work with URLs
        VoteCounter = 0;
        while (true)                            // Loop till the end of URL Hash Map
        {
            try{
                url = rf.getNextURL();          // get the next URL
                url = URLEncoder.encode(url, "UTF-8");
                URLs.put(url,new URLclass(url)) ;
            }catch(IOException e) {             // IO Exception
                System.out.println(e.getMessage());
                break;
            }catch(Exception endFile){
                break;
            }
            counter++;                          // count num of urls
        }
        try{
            rf.close();                         // close input reder file.
        }catch(IOException cl){
            System.out.println(cl.getMessage());// IO Exception
        }
        if(counter == 0){                       // check if file is empty
            fileIsEmpty = true;
        }
    }
//==============================================================================
    /**
     * Function which searching inside to cookies for some specific cookies name
     * @param cookies       cookies object
     * @param cookieName    the name of cookies
     * @return              the name of empty on faild to find
     */
    private  String getCookieValue(Cookie[] cookies,String cookieName){
        
        if(cookies == null){    //  Check if object in not null
            return("");         //  return nothing
        }
        //  Start search in cookie array for entry wich equal to given value
        for(int i=0; i<cookies.length; i++) {
            Cookie cookie = cookies[i];                 //  get this cookie
            if (cookieName.equals(cookie.getName())){   //  compare names
                return(cookie.getValue());              //  if eq, ret the value
            }
        }
        return("");             //  return nothing becouse not found any thing
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
//==============================================================================
    private  Map<String,URLclass> URLs ;                //  Map of URLs classes
    private Cookie[] cookies;                           // cookies
    private int VoteCounter;                            // person vote counter
    private String ServletPATH      =   "/ex3/Voting";  // path to servlet
    private int CookiesTime         =   60*60*24*30;    // cooies timer
    private boolean fileNotFound    =   false;          // file not find flag
    private boolean fileIsEmpty     =   false;          // File empty flag

}
//==============================================================================
//==============================================================================
//==============================================================================