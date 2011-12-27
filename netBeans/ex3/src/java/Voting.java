/*
 * Voting class. This work home exercise 3.
 * 
 */
//==============================================================================
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
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
        
        responseHTML +="<html>\n"
                + "<head>\n"
                + "<title>Servlet Voting</title>\n"
                + "<link rel='stylesheet' "
                + "type='text/css' href='voting.css' />\n"
                + "</head>\n\n<body>\n"
                + "<h1>  EX3 :: Voting servlet</h1>\n"
                + "<table width='100%'>"
                + " <tr>"
                + "    <td></td>"
                + "    <td style='width: 80%;'><h1>  EX3 :: Voting servlet</h1></td>"
                + "    <td></td>"
                + "  </tr>";       

        if(request.getContentType() != null ){
            responseHTML +="<tr><td></td><td>"+PrintVotingProccess( request, response)+"</td><td></td></tr> ";
        }
        else{
            responseHTML +="<tr><td></td><td>"+PrintVotingForm()+"</td><td></td></tr> ";
            responseHTML +="<tr><td></td><td>"+PrintVotingResults() +"</td><td></td></tr> ";    
        }
        
        responseHTML +="</table></body></html>";
        
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
        retHTML +="<div class=\"votingSection\"><table width=\"100%\">";
        Iterator<URLclass> itr = URLs.iterator();
        URLclass  tempURL;
        while(itr.hasNext())
        {
            tempURL = itr.next();
            String urlName = URLDecoder.decode(tempURL.GetURLName());
            retHTML +="<tr>\n"
              + " <td>\n"
              + "   <div><strong>"+ urlName+"</strong></div>\n"
              + "</td>\n"
              + "<td style=\"width:80%;\">\n"
              + "   <div class=\"bar_wrap\">\n"
              + "       <div class=\"bar\" style=\"width:" + tempURL.getPoints()*10  + "%\">\n"
              + "           <div class=\"right\">" + tempURL.getPoints()  + "</div>\n"
              + "       </div>\n"
              + "   </div>\n"
              + " </td>\n"
              + "</tr>";        
        } 

        retHTML +="</table></div>";
        
        return(retHTML);
  }
//==============================================================================
    private String PrintVotingProccess(HttpServletRequest request,HttpServletResponse response)
    {
        String retHTML = "";
        
        String [] resultOfVoting = request.getParameterValues("grade_value"); 
        String [] resultOfVotingURL = request.getParameterValues("url"); 
            
        if(resultOfVoting == null || resultOfVotingURL == null){
            return("<strong class=\"error\">Some of entry of post incorect<strong>");
        }
        for(int i = 0;i<resultOfVoting.length;i++){
            boolean setCookies = false;
            int points=-1 ;
            String url = URLDecoder.decode(resultOfVotingURL[i]);

            try{
                points = Integer.parseInt(resultOfVoting[i]) ;
                if(points < 0 || points > 10){
                    retHTML += "<strong>Bad value for "
                            + url
                            + ".The value must be "
                            + "between 0 to 10</strong><br/>";
                }
                else{
                    setCookies = true;
                }
            }
            catch(Exception ex){
                retHTML += "<strong>Cannot be empty for "+url+".The value must be "
                        + "between 0 to 10</strong><br/>";
            }

            if(setCookies){
                
                Iterator<URLclass> itr = URLs.iterator();
                URLclass  tempURL;
                
                while(itr.hasNext()){
                    tempURL = itr.next();
                                            
                    if(tempURL.GetURLName().equals(resultOfVotingURL[i])){
                        
                        tempURL.SetPoints(points );
                        Cookie userCookie = new Cookie(resultOfVotingURL[i], "true");
                        userCookie.setMaxAge(20);
                        response.addCookie(userCookie);  
                        break;
                    }
                }
            }
        }
        retHTML +="<br/>\n"
                + "<h2>In few seconds you will be redirected "
                + "to reults page.</h2>\n"
                + "<script type=\"text/javascript\">"
                + "setTimeout('window.location=\"Voting\"',3000);</script>\n";
        return(retHTML);
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
        "                    <tr>\n" +
        "                        <th>URL</th>\n" +
        "                        <th>Set grade</th>\n" +
        "                    </tr>\n" +
        "                </thead>\n" +
        "                <tbody>\n"; 
        Iterator<URLclass> itr = URLs.iterator();
        URLclass  tempURL;
        while(itr.hasNext()){
            
            tempURL = itr.next();
            String urlName = URLDecoder.decode(tempURL.GetURLName());
            String searchString = "";
           
            searchString =getCookieValue(cookies,tempURL.GetURLName()); 
            
       
            if(!searchString.equals("true"))
            {
                retHTML += "<tr><td>"+urlName+"</td>\n";
                retHTML +="<td>\n<input type='text' maxlength='2' name='grade_value' "
                    + "value=\"\" />\n"
                    + "<input type='hidden' name='url' "
                    + "value='"+tempURL.GetURLName()+"' />\n"
                    + "</td>\n</tr>\n";
                ReturnForm = true;  //  have return the form
            }
        } 
        retHTML+="</tbody>\n" +
            "            </table>\n" +           
            "            <input type=\"submit\"  />\n" +
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
        
        super.init();
        
        ReadFile rf = null;             //  input class
        String url ="";                 //  url
                             //  read default file
        try {
            rf = new ReadFile(getServletContext().getRealPath("input.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Voting.class.getName()).log(Level.SEVERE, null, ex);
        }
        URLs = new ArrayList<URLclass>();   //  Set array list to work with URLs
        
        while (true)                // Loop till the end of URL list
        {
            try{
                url = rf.getNextURL(); // get the next URL
                url = URLEncoder.encode(url, "UTF-8");
                URLs.add(new URLclass(url)) ;
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
    private  ArrayList<URLclass> URLs ;
    private Cookie[] cookies;

}
//==============================================================================
//==============================================================================
//==============================================================================