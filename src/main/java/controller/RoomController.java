/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RoomDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Room;

/**
 *
 * @author Legion
 */
@WebServlet(name = "RoomController", urlPatterns = {"/room"})
public class RoomController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RoomDAO rdao = new RoomDAO();
        String action = request.getParameter("action");
        if(action == null){
            action = "all";
        }
        switch (action) {
            case "all":
               List<Room> allRoom = rdao.getAllRoom();
               request.setAttribute("allRooms", allRoom);
               request.getRequestDispatcher("room.jsp").forward(request, response);
                break;
            case "detail":
                String idString = request.getParameter("id").trim();
                int id = Integer.parseInt(idString);
                Room roomDetail = rdao.getRoomByID(id);
                request.setAttribute("roomDetail",roomDetail);
                request.getRequestDispatcher("roomDetail.jsp").forward(request, response);
                break;
        }
        
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
