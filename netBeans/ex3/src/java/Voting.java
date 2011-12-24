/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head>"
                + "<title>Servlet Voting</title>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"voting.css\" />"
                + "</head><body>");       
        Cookie[] cookies = request.getCookies();
        counter++;
        String searchString = getCookieValue(cookies,"user"); 
        PrintVoitingForm(out);
        PrintVotingResults(out);
        out.println(searchString);
        out.print(":-6-" + request.getParameterValues("grade_value") + ":");
        try {
            Cookie userCookie = new Cookie("user", request.getRemoteHost());
            response.addCookie(userCookie); 
            
            
//            String[] str = getParameterValues("inputFile");

            out.println("<a href='/ex3'>To Main : "+counter+"</a>");
            out.println("<h1>Ёбання хуйня at " + request.getContextPath () + "</h1>");
            out.println(request.getParameter("t"));
            
        } finally {            
            out.close();
        }
        out.println("</body></html>");
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
    
  private static String getCookieValue(Cookie[] cookies,
                                      String cookieName) {
    for(int i=0; i<cookies.length; i++) {
      Cookie cookie = cookies[i];
      if (cookieName.equals(cookie.getName()))
        return(cookie.getValue());
    }
    return("");
  }
  private static void PrintVotingResults(PrintWriter out)
  {
      out.println("<div class=\"votingSection\">");
      for(int i=0;i<4;i++)
      {
      out.println("<div class=\"bar_wrap\">"+
"    <div class=\"bar\" style=\"width:" + (i+1)*20 + "%\">"
+"    <div class=\"left\">"+i +" Voted</div>"+
"    <div class=\"right\">" + (i+1)*20 + "%</div>"
              + "</div>"+
"</div>");
      }
      out.println("</div>");
  }
  private static void PrintVoitingForm(PrintWriter out) 
  {
      out.println("<h1>EX3</h1>" +
"        <form method=\"post\" action=\"Voting\">" +
"            <input type=\"text\" name=\"t\" />" +
"            <table border=\"1\">" +
"                <thead>" +
"                    <tr>" +
"                        <th>URL</th>" +
"                        <th>Set grade</th>" +
"                    </tr>" +
"                </thead>" +
"                <tbody>");
            for(int i=0;i<3;i++)
           {
               out.println("<tr><td>"+i+"</td>");
               out.println("<td><input type=\"text\" name=\"grade_value\" id=\"grade_value\" value=\"Enter your value\" /></td></tr>");
           }

        out.println("</tbody>" +
"            </table>" +           
"            <input type=\"submit\"  />" +
"        </form>");
  }
  public static int counter;
}
