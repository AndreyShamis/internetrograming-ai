/*
 * Authors: EX4 Andrey Shamis AND Ilia Gaysinsky
 */
package ex4;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 *
 * @author admn
 */
public class UsersList extends HttpServlet {
        private ArrayList<String>  userss = new ArrayList<String>();
//=============================================================================
        /**
         * 
         * @return 
         */
        public String[] AllUsersList(){
            String[] users  =  new String[userss.size()];
            for(int i=0;i<users.length;i++){
                users[i] = userss.get(i);
            }
            return users;
        }
//=============================================================================
        /**
         * 
         * @param userName 
         */
        public void AddUserToList(String userName){
            userss.add(userName);
        }
//=============================================================================
        /**
         * 
         * @param userName 
         */
        public void RemoveUserFromList(String userName){
            for(int i=0;i<userss.size();i++){
                if(userss.get(i).equals(userName)){
                    userss.remove(i);
                    break;
                }      
            }
        }
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
        try {
        } finally {            
            out.close();
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
}
