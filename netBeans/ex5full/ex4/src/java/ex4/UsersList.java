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
 * This clas provide tools for site to know wich user are loged in 
 */
public class UsersList extends HttpServlet {
        private ArrayList<String>  userss = new ArrayList<String>();
//=============================================================================
        /**
         * Function wich return list of users are logened
         * @return 
         */
        public String[] AllUsersList(){
            //  Temp variable be retuned
            String[] users  =  new String[userss.size()];
            for(int i=0;i<users.length;i++){
                users[i] = userss.get(i);   //  get each user from data
            }
            return users;                   //  return array
        }
//=============================================================================
        /**
         * Function wich add user by name into array wich loged in now
         * @param userName  Login User name
         */
        public void AddUserToList(String userName){
            RemoveUserFromList(userName); 
            userss.add(userName);           //  adding
        }
//=============================================================================
        /**
         * Remove user from array by name wich do logout now
         * @param userName Login User name
         */
        public void RemoveUserFromList(String userName){
            //  Start looking for user in array
            for(int i=0;i<userss.size();i++){
                //  Check if find the user
                if(userss.get(i).equals(userName)){
                    userss.remove(i);           //  remove
                    break;                      //  breake
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
