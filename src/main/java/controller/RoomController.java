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
        if (action == null) {
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
                request.setAttribute("roomDetail", roomDetail);
                request.getRequestDispatcher("roomDetail.jsp").forward(request, response);
                break;
            case "create":
                response.sendRedirect("createRoom.jsp");
                break;
            case "delete":
                String idDelString = request.getParameter("id");
                int idDel = Integer.parseInt(idDelString);
                Room roomDel = rdao.getRoomByID(idDel);
                request.setAttribute("roomDel", roomDel);
                request.getRequestDispatcher("deleteRoom.jsp").forward(request, response);
            case "update":
                String idUpdateString = request.getParameter("id");
                int idUpdate = Integer.parseInt(idUpdateString);
                Room roomUpdate = rdao.getRoomByID(idUpdate);
                request.setAttribute("roomUpdate", roomUpdate);
                request.getRequestDispatcher("updateRoom.jsp").forward(request, response);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RoomDAO rdao = new RoomDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "all";
        }
        switch (action) {
            case "all":
                response.sendRedirect("room");
                break;
            case "create":
                String name = request.getParameter("name");
                String capacityString = request.getParameter("capacity");
                String type = request.getParameter("type");
                int capacity = Integer.parseInt(capacityString);
                int changes = rdao.createRoom(name, capacity, type);
                if (changes != -1) {
                    System.out.println("OK");
                } else {
                    System.out.println("Error");
                }
                response.sendRedirect("room");
                break;
            case "delete":
                String idDelString = request.getParameter("id");
                int idDel = Integer.parseInt(idDelString);
                int deleted = rdao.deleteRoombyID(idDel);
                if (deleted > 0) {
                    System.out.println("Room deleted successfully");
                } else {
                    System.out.println("Failed to delete room");
                }
                response.sendRedirect("room");
                break;
            case "update":
                String idUpdateString = request.getParameter("id");
                String nameUpdateString = request.getParameter("name");
                String capacityUpdateString = request.getParameter("capacity");
                String typeUpdateString = request.getParameter("type");
                int idUpdate = Integer.parseInt(idUpdateString);
                int capacityUpdate = Integer.parseInt(capacityUpdateString);
                int updated = rdao.updateRoom(idUpdate, nameUpdateString, capacityUpdate, typeUpdateString);
                if (updated > 0) {
                    System.out.println("Room updated successfully");
                } else {
                    System.out.println("Failed to update room");
                }
                response.sendRedirect("room");
                break;

        }
    }
}
