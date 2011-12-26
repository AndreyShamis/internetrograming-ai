/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
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

/**
 *
 * @author ilia
 */
@WebServlet(name = "Voting", urlPatterns = {"/Voting"})
public class Voting extends HttpServlet {

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
        //  Get response object
        
        String responseHTML = "";
        responseHTML +="<html><head>"
                + "<title>Servlet Voting</title>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"voting.css\" />"
                + "</head><body>"
                + "<h1>  EX3 :: Voting servlet</h1>";       
        //  Print voting form
        
        PrintWriter out = response.getWriter();
        //out.println(searchString);
        if(request.getContentType() != null ){
            responseHTML +=PrintVotingProccess( request, response);
        }
        else{
            responseHTML +=PrintVotingForm();
            responseHTML +=PrintVotingResults();    
        }
        
        
        out.println(responseHTML);

        out.println("</body></html>");
        out.close();
        
    }
  private  String getCookieValue(Cookie[] cookies,
                                      String cookieName) {
    for(int i=0; i<cookies.length; i++) {
      Cookie cookie = cookies[i];
      if (cookieName.equals(cookie.getName()))
        return(cookie.getValue());
    }
    return("");
  }
  
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
              + "   <div>"+ urlName+"</div>\n"
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
  
    private String PrintVotingProccess(HttpServletRequest request,HttpServletResponse response)
    {
        String retHTML = "";
        
           String [] resultOfVoting = request.getParameterValues("grade_value"); 
           String [] resultOfVotingURL = request.getParameterValues("url"); 
            for(int i = 0;i<resultOfVoting.length;i++){
                boolean setCookies = false;
                int points=0 ;
                if(resultOfVoting[i].equals(""))
                {
                       retHTML += "<strong>Bad value.The value must be "
                               + "between 0 to 10</strong><br/>";
                }else{
                    points = Integer.parseInt(resultOfVoting[i]) ;
                    if(points < 0 || points > 10){
                        retHTML += "<strong>Bad value.The value must be "
                                + "between 0 to 10</strong><br/>";
                    }
                    else{
                        setCookies = true;
                    }
                        
                }
                if(setCookies){
                  Iterator<URLclass> itr = URLs.iterator();
                  URLclass  tempURL;
                  while(itr.hasNext())
                  {
                    tempURL = itr.next();
                    if(tempURL.GetURLName().equals(resultOfVotingURL[i]))
                    {
                        tempURL.SetPoints(points);
                        Cookie userCookie = new Cookie(resultOfVotingURL[i], "true");
                        //request.getRemoteHost()
                        response.addCookie(userCookie);  
                        userCookie.setMaxAge(20);
                        break;
                    }
                  }
                }
            }
            retHTML +="<br/>"
                    + "<h2>In few seconds you will be redirected to reults page.</h2>";
        return(retHTML);
    }
 
            
  private String PrintVotingForm(){
      
      String retHTML = "";
      retHTML = "" +
        "        <form method=\"post\" action=\"Voting\">" +
        "            <table>" +
        "                <thead>" +
        "                    <tr>" +
        "                        <th>URL</th>" +
        "                        <th>Set grade</th>" +
        "                    </tr>" +
        "                </thead>" +
        "                <tbody>"; 
        Iterator<URLclass> itr = URLs.iterator();
        URLclass  tempURL;
        while(itr.hasNext()){
            
            tempURL = itr.next();
            String urlName = URLDecoder.decode(tempURL.GetURLName());
            String searchString = getCookieValue(cookies,tempURL.GetURLName()); 
            if(!searchString.equals("true"))
            {
                retHTML += "<tr><td>"+urlName+"</td>";
                retHTML +="<td><input type=\"text\" name=\"grade_value\" "
                    + "value=\"\" />"
                    + "<input type=\"hidden\" name=\"url\" "
                    + "value=\""+tempURL.GetURLName()+"\" />"
                    + "</td></tr>";
            }
        } 
        retHTML+="</tbody>" +
            "            </table>" +           
            "            <input type=\"submit\"  />" +
            "        </form>";
        
        return (retHTML);
  }
  
  /**
   * 
   * @throws ServletException 
   */
    @Override
    public void init() throws ServletException {
        //theparam = getServletConfig().getInitParameter("");
        ReadFile rf = null;             //  input class
        String url ="";                 //  url
                             //  read default file
        try {
            rf = new ReadFile(getServletContext().getRealPath("input.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Voting.class.getName()).log(Level.SEVERE, null, ex);
        }
        URLs = new ArrayList<URLclass>();
        while (true)               // Loop till the end of URL list
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
    

    private  ArrayList<URLclass> URLs ;
    private Cookie[] cookies;

}
