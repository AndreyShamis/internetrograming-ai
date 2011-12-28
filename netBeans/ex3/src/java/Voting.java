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
 *  Voting class. This work home exercise 3.
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
        String responseHTML = "";
        // Header manipulation
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        if(request.getContentType() != null ){
            VotingProccess( request, response);
            response.setStatus(301);
            response.setHeader("Location","/ex3/Voting");
            response.setHeader( "Connection", "close" );
        } 
        else{
            responseHTML +="<html>\n"
                    + "<head>\n"
                    + "<title>Servlet Voting</title>\n"
                    + "<link rel='stylesheet' "
                    + "type='text/css' href='voting.css' />\n"
                    + "</head>\n\n<body>\n"
                    + "<table width='100%'>"
                    + " <tr>"
                    + "    <td></td>"
                    + "    <td class='votingSection'style='width:80%;'><h1>  EX3 :: Voting servlet</h1></td>"
                    + "    <td></td>"
                    + "  </tr>";       
            responseHTML +="<tr><td></td><td>"+PrintVotingForm()+"</td><td></td></tr> ";
            responseHTML +="<tr><td></td><td>"+PrintVotingResults() +"</td><td></td></tr> ";    
        
            responseHTML +="</table></body></html>";
        }
        out.println(responseHTML);
        out.close();
        
    }
//==============================================================================
  /**
   * 
   * @param out 
   */
    private  String PrintVotingResults(){
        
        String retHTML = "";
        retHTML +="<h3>Total number of votes: "+VoteCounter+" person .<em> This"
                + " actualy also if somebody has voted only for one or more URLs. </em></h3>"
                + ""
                + "<div class=\"votingSection\">"
                + "<table width=\"100%\">";
        Set s = URLs.entrySet();
        Iterator itr = s.iterator();
        URLclass  tempURL;
        while(itr.hasNext())
        {
            Map.Entry  tempMapEntry =(Map.Entry) itr.next();
            tempURL = (URLclass)tempMapEntry.getValue();
            String urlName = URLDecoder.decode(tempURL.GetURLName());
            retHTML +="<tr>\n"
              + " <td>\n"
              + "   <div class='votingFormUrlLinkBox'><strong>"+ urlName+"</strong><br/> *"
                    + "<label class='urlDesc'>Votes : _. Points : 7 / 7.544 (Int / Double)</label></div>\n"
              + "</td>\n"
              + "<td style=\"width:80%;\">\n"
              + "   <div class=\"bar_wrap\">\n"
              + "       <div class=\"bar\" style=\"width:" + tempURL.getPoints()*10  + "%\">\n";
              if(tempURL.getPoints() > 1){
                  
                  retHTML+="           <div class=\"right\">" + tempURL.getPoints()  + "</div>\n";
              }
              retHTML+= "       </div>\n"
              + "   </div>\n"
              + " </td>\n"
              + "</tr>";        
        } 

        retHTML +="</table></div>";
        
        return(retHTML);
  }
//==============================================================================
    private void VotingProccess(HttpServletRequest request,HttpServletResponse response)
    {
        boolean CookiesFlag = false;    //  Variable for know if ned set cookies
        String [] resultOfVoting = request.getParameterValues("grade_value"); 
        String [] resultOfVotingURL = request.getParameterValues("url"); 
            
        if(resultOfVoting == null || resultOfVotingURL == null){
            return;
        }
        
        String searchString =getCookieValue(cookies,"HaveVoted"); 
        if(!searchString.equals("true")){
            Cookie votedCookie = new Cookie("HaveVoted", "true");
            response.addCookie(votedCookie); 
            VoteCounter++;
        }
        for(int i = 0;i<resultOfVoting.length;i++){
            int points=-1 ;
            CookiesFlag = false;
            String ErrorCode = "";
            String url = URLDecoder.decode(resultOfVotingURL[i]);

            try{
                points = Integer.parseInt(resultOfVoting[i]) ;
                CookiesFlag = true;
            }
            catch(Exception ex){
                ErrorCode = "1";
            }

            if(CookiesFlag){
                URLclass  tempURL=URLs.get(resultOfVotingURL[i]);
                if(tempURL != null){
                    try{
                        tempURL.SetPoints(points );
                        Cookie userCookie = new Cookie(resultOfVotingURL[i], "true");
                        userCookie.setMaxAge(60);       //  TODO :: Change
                        response.addCookie(userCookie); 
                    }catch(Exception ex){
                        ErrorCode = "2";
                        CookiesFlag = false;
                    }
                }
            }
            if(!CookiesFlag){
                Cookie userCookie = new Cookie(resultOfVotingURL[i], ErrorCode);
                userCookie.setMaxAge(8);
                response.addCookie(userCookie);   
            }
        }
    }
 
//==============================================================================        
  private String PrintVotingForm(){
      
      String retHTML = "";
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
        Set s = URLs.entrySet();
        Iterator itr = s.iterator();
        URLclass  tempURL;
        boolean flagOdd=false;
        
        
        while(itr.hasNext()){
            Map.Entry  tempMapEntry =(Map.Entry) itr.next();
            tempURL = (URLclass)tempMapEntry.getValue();
            String urlName = URLDecoder.decode(tempURL.GetURLName());
            String searchString = "";
           
            searchString =getCookieValue(cookies,tempURL.GetURLName()); 
            
       
            if(!searchString.equals("true"))
            {
                String lineColor="";
                
                if(flagOdd){
                 lineColor = "cssLineOdd";
                 flagOdd =false;
                }
                else{
                   lineColor = "cssLineNotOdd";
                   flagOdd = true;
                }
                retHTML += "<tr><td class='"+lineColor+"'><span class='VotingFormUrlsNames'>"+urlName+"</span></td>\n";
                retHTML +="<td class='VotingFormPoints "+lineColor+"'>\n<input class='PointsBox' type='text' maxlength='2' name='grade_value' "
                    + "value=\"\" />\n"
                    + "<input  type='hidden' name='url' "
                    + "value='"+tempURL.GetURLName()+"' />\n";
                if(searchString.equals("1")){
                    retHTML += "<em class='informationMessage'>"
                            + "Please enter value</em><br/>";
                }
                if(searchString.equals("2")){
                    retHTML += "<strong class='error'>Bad value"
                            + ".The value must be "
                            + "between 0 to 10</strong><br/>";
                }
                retHTML +=   "</td>\n</tr>\n";
                ReturnForm = true;  //  have return the form
            }
        } 
        retHTML+="</tbody>\n" +
            "            </table>\n" +           
            "           <div class='votingSection' style='width:100%'> <input style='width:200px;' type='submit' value=' Press to Vote '  /></div>\n" +
            "        </form>\n";
        
        if(ReturnForm){
            return (retHTML);
        }else{
            return("");
        }
  }
//==============================================================================
  /**
   * 
   * @throws ServletException 
   */
    @Override
    public void init() throws ServletException {
        String FilePath = "";
        FilePath = getServletContext().getRealPath("") 
                + File.separatorChar 
                + getServletConfig().getInitParameter("urlsFile").toString();
        
        ReadFile rf = null;             //  input class
        String url ="";                 //  url
                             //  read default file
        try { 
            rf = new ReadFile(FilePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Voting.class.getName()).log(Level.SEVERE, null, ex);
        }
        URLs = new HashMap<String,URLclass>();   //  Set array list to work with URLs
        VoteCounter = 0;
        while (true)                // Loop till the end of URL list
        {
            try{
                url = rf.getNextURL(); // get the next URL
                url = URLEncoder.encode(url, "UTF-8");
                URLs.put(url,new URLclass(url)) ;
            }catch(IOException e) {                  // IO Exception
                System.out.println(e.getMessage());
                break;
            }catch(Exception endFile){
                break;
            }
        }
        try{
            rf.close();                         // close input reder file.
        }catch(IOException cl){
            System.out.println(cl.getMessage());// IO Exception
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
        
        if(cookies == null){
            return("");    
        }
        for(int i=0; i<cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName())){
                return(cookie.getValue());
            }
        }
        return("");
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
    private  Map<String,URLclass> URLs ;
    private Cookie[] cookies;
    private int VoteCounter;

}
//==============================================================================
//==============================================================================
//==============================================================================