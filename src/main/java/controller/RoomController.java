/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RoomDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Room;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "RoomController", urlPatterns = {"/room"})
public class RoomController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            RoomDAO rdao = new RoomDAO();
            String action = request.getParameter("action");
            if (action == null) {
                action = "all";
            }

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute(("user"));

            if (currentUser == null) {
                response.sendRedirect("login");
                return;
            }
            if (currentUser.getRole() == null || !currentUser.getRole().getManageCourse()) {
                session.setAttribute("message", "Access Denied: You do not have permission to manage rooms!");
                session.setAttribute("messageType", "error");
                response.sendRedirect("dashboard");
                return;
            }

            switch (action) {
                case "all":
                    List<Room> allRoom = rdao.getAllRoom();
                    // Check which rooms have classes assigned
                    Map<Integer, Boolean> roomUsageMap = new HashMap<>();
                    for (Room room : allRoom) {
                        boolean isInUse = rdao.isRoomInUse(room.getRoomId());
                        roomUsageMap.put(room.getRoomId(), isInUse);
                    }

                    // Get filter criteria from database
                    List<String> roomTypes = rdao.getDistinctRoomTypes();
                    List<Integer> capacities = rdao.getDistinctRoomCapacities();

                    request.setAttribute("allRooms", allRoom);
                    request.setAttribute("roomUsageMap", roomUsageMap);
                    request.setAttribute("roomTypes", roomTypes);
                    request.setAttribute("capacities", capacities);
                    request.setAttribute("home_view", "/academic/manageRoom.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    break;
                case "manage":
                    // Client-side filtering: load full room list and let JS handle filtering in the JSP
                    String capacity = request.getParameter("capacity");
                    String type = request.getParameter("type");
                    String status = request.getParameter("status");

                    List<Room> allRoomForManage = rdao.getAllRoom();
                    // Check which rooms have classes assigned
                    Map<Integer, Boolean> usageMapForManage = new HashMap<>();
                    for (Room room : allRoomForManage) {
                        boolean isInUse = rdao.isRoomInUse(room.getRoomId());
                        usageMapForManage.put(room.getRoomId(), isInUse);
                    }

                    List<String> allRoomTypes = rdao.getDistinctRoomTypes();
                    List<Integer> allCapacities = rdao.getDistinctRoomCapacities();

                    request.setAttribute("allRooms", allRoomForManage);
                    request.setAttribute("roomUsageMap", usageMapForManage);
                    request.setAttribute("roomTypes", allRoomTypes);
                    request.setAttribute("capacities", allCapacities);
                    // Preserve current filter values so selects can pre-select on page load
                    request.setAttribute("capacity", capacity);
                    request.setAttribute("type", type);
                    request.setAttribute("status", status);
                    request.setAttribute("home_view", "/academic/manageRoom.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    break;
                case "detail":
                    String idString = request.getParameter("id").trim();
                    int id = Integer.parseInt(idString);
                    Room roomDetail = rdao.getRoomByID(id);

                    // Get list of classes using this room
                    List<String[]> classesUsingRoom = rdao.getClassesUsingRoom(id);

                    request.setAttribute("roomDetail", roomDetail);
                    request.setAttribute("classesUsingRoom", classesUsingRoom);
                    request.setAttribute("home_view", "/academic/roomDetail.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    break;
                case "create":
                    request.setAttribute("home_view", "/academic/createRoom.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    break;
                case "delete":
                    String idDelString = request.getParameter("id");
                    int idDel = Integer.parseInt(idDelString);
                    Room roomDel = rdao.getRoomByID(idDel);
                    request.setAttribute("roomDel", roomDel);
                    request.setAttribute("home_view", "/academic/deleteRoom.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    break;
                case "disable":
                    String idDisableString = request.getParameter("id");
                    int idDisable = Integer.parseInt(idDisableString);
                    Room roomDisable = rdao.getRoomByID(idDisable);

                    // Get list of classes using this room
                    List<String[]> classesUsingRoomDisable = rdao.getClassesUsingRoom(idDisable);

                    request.setAttribute("roomDisable", roomDisable);
                    request.setAttribute("classesUsingRoom", classesUsingRoomDisable);
                    request.setAttribute("home_view", "/academic/disableRoom.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    break;
                case "enable":
                    String idEnableString = request.getParameter("id");
                    int idEnable = Integer.parseInt(idEnableString);
                    Room roomEnable = rdao.getRoomByID(idEnable);

                    request.setAttribute("roomEnable", roomEnable);
                    request.setAttribute("home_view", "/academic/enableRoom.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    break;
                case "update":
                    String idUpdateString = request.getParameter("id");
                    int idUpdate = Integer.parseInt(idUpdateString);
                    Room roomUpdate = rdao.getRoomByID(idUpdate);
                    request.setAttribute("roomUpdate", roomUpdate);
                    request.setAttribute("home_view", "/academic/updateRoom.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "An error occurred. Please try again.");
            request.getSession().setAttribute("messageType", "error");
            response.sendRedirect("room");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            RoomDAO rdao = new RoomDAO();
            String action = request.getParameter("action");
            if (action == null) {
                action = "all";
            }

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute(("user"));

            if (currentUser == null) {
                response.sendRedirect("login");
                return;
            }
            if (currentUser.getRole() == null || !currentUser.getRole().getManageCourse()) {
                session.setAttribute("message", "Access Denied: You do not have permission to manage rooms!");
                session.setAttribute("messageType", "error");
                response.sendRedirect("dashboard");
                return;
            }

            switch (action) {
                case "all":
                    response.sendRedirect("room");
                    break;
                case "create":
                    String name = request.getParameter("name");
                    String capacityString = request.getParameter("capacity");
                    String type = request.getParameter("type");
                    String statusString = request.getParameter("status");

                    int capacity = Integer.parseInt(capacityString);
                    int status = Integer.parseInt(statusString);

                    // Check if room name already exists
                    if (rdao.checkRoomNameExists(name)) {
                        request.getSession().setAttribute("message", "Room name '" + name + "' already exists. Please use a different name.");
                        request.getSession().setAttribute("messageType", "error");
                        response.sendRedirect("room?action=create");
                    } else if (name == null || name.trim().isEmpty()) {
                        request.getSession().setAttribute("message", "Room name cannot be empty.");
                        request.getSession().setAttribute("messageType", "error");
                        response.sendRedirect("room?action=create");
                    } else if (type == null || type.trim().isEmpty()) {
                        request.getSession().setAttribute("message", "Room description cannot be empty.");
                        request.getSession().setAttribute("messageType", "error");
                        response.sendRedirect("room?action=create");
                    } else {
                        int changes = rdao.createRoom(name, capacity, type, status);
                        if (changes != -1) {
                            request.getSession().setAttribute("message", "Room created successfully!");
                            request.getSession().setAttribute("messageType", "success");
                            response.sendRedirect("room");
                        } else {
                            request.getSession().setAttribute("message", "Failed to create room.");
                            request.getSession().setAttribute("messageType", "error");
                            response.sendRedirect("room?action=create");
                        }
                    }
                    break;
                case "delete":
                    String idDelString = request.getParameter("id");
                    int idDel = Integer.parseInt(idDelString);

                    if (rdao.isRoomInUse(idDel)) {
                        request.getSession().setAttribute("message", "Cannot delete room because it is assigned to a class or schedule.");
                        request.getSession().setAttribute("messageType", "error");
                        response.sendRedirect("room");
                        break;
                    }

                    // Delete room (only for rooms not in use)
                    int deleted = rdao.deleteRoombyID(idDel);

                    if (deleted > 0) {
                        request.getSession().setAttribute("message", "Room deleted successfully.");
                        request.getSession().setAttribute("messageType", "success");
                    } else {
                        request.getSession().setAttribute("message", "Failed to delete room.");
                        request.getSession().setAttribute("messageType", "error");
                    }
                    response.sendRedirect("room");
                    break;
                case "disable":
                    String idDisableString = request.getParameter("id");
                    int idDisable = Integer.parseInt(idDisableString);

                    if (rdao.isRoomInUse(idDisable)) {
                        request.getSession().setAttribute("message", "Cannot disable room because it is assigned to a class or schedule.");
                        request.getSession().setAttribute("messageType", "error");
                        response.sendRedirect("room");
                        break;
                    }

                    // Disable room
                    int disabled = rdao.disableRoom(idDisable);

                    if (disabled > 0) {
                        request.getSession().setAttribute("message", "Room has been disabled successfully.");
                        request.getSession().setAttribute("messageType", "success");
                    } else {
                        request.getSession().setAttribute("message", "Failed to disable room.");
                        request.getSession().setAttribute("messageType", "error");
                    }
                    response.sendRedirect("room");
                    break;
                case "enable":
                    String idEnableString = request.getParameter("id");
                    int idEnable = Integer.parseInt(idEnableString);

                    // Enable room
                    int enabled = rdao.enableRoom(idEnable);

                    if (enabled > 0) {
                        request.getSession().setAttribute("message", "Room has been enabled successfully.");
                        request.getSession().setAttribute("messageType", "success");
                    } else {
                        request.getSession().setAttribute("message", "Failed to enable room.");
                        request.getSession().setAttribute("messageType", "error");
                    }
                    response.sendRedirect("room");
                    break;
                case "update":
                    String idUpdateString = request.getParameter("id");
                    String nameUpdateString = request.getParameter("name");
                    String capacityUpdateString = request.getParameter("capacity");
                    String typeUpdateString = request.getParameter("type");
                    String statusUpdateString = request.getParameter("status");

                    int idUpdate = Integer.parseInt(idUpdateString);
                    int capacityUpdate = Integer.parseInt(capacityUpdateString);
                    int statusUpdate = Integer.parseInt(statusUpdateString);

                    int updated = rdao.updateRoom(idUpdate, nameUpdateString, capacityUpdate, typeUpdateString, statusUpdate);
                    if (updated > 0) {
                        request.getSession().setAttribute("message", "Room updated successfully!");
                        request.getSession().setAttribute("messageType", "success");
                    } else {
                        request.getSession().setAttribute("message", "Failed to update room.");
                        request.getSession().setAttribute("messageType", "error");
                    }
                    response.sendRedirect("room");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "An error occurred. Please try again.");
            request.getSession().setAttribute("messageType", "error");
            response.sendRedirect("room");
        }
    }
}
