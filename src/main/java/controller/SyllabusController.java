package controller;

import dao.SyllabusDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import model.Syllabus;
import model.User;

@WebServlet(name = "SyllabusController", urlPatterns = {"/syllabus"})
public class SyllabusController extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private int parsePage(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam == null || pageParam.trim().isEmpty()) {
            return 1;
        }
        try {
            return Math.max(1, Integer.parseInt(pageParam));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private List<Syllabus> paginateSyllabusList(List<Syllabus> source, int page, int pageSize, HttpServletRequest request) {
        if (source == null || source.isEmpty()) {
            request.setAttribute("currentPage", 1);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalItems", 0);
            request.setAttribute("totalPages", 1);
            request.setAttribute("startItem", 0);
            request.setAttribute("endItem", 0);
            return Collections.emptyList();
        }

        int totalItems = source.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int currentPage = Math.min(page, totalPages);
        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startItem", fromIndex + 1);
        request.setAttribute("endItem", toIndex);

        return source.subList(fromIndex, toIndex);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "manage";
        }
        HttpSession session = request.getSession();

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        if (action == null) {
            action = "manage";
        }

        if (currentUser.getRole() == null || !currentUser.getRole().getManageCourse()) {
            session.setAttribute("message", "Access Denied: You don't have permission to manage syllabus!");
            session.setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        SyllabusDAO syllabusDAO = new SyllabusDAO();

        switch (action) {
            case "manage":
                List<Syllabus> syllabusList = syllabusDAO.getAllSyllabus();
                request.setAttribute("syllabusList", paginateSyllabusList(syllabusList, parsePage(request), DEFAULT_PAGE_SIZE, request));
                request.setAttribute("home_view", "/academic/syllabus_management.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "add":
                request.setAttribute("courseOptions", syllabusDAO.getCourseOptions());
                request.setAttribute("formMode", "create");
                request.setAttribute("home_view", "/academic/syllabus_form.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "edit":
                String syllabusIdParam = request.getParameter("syllabusId");
                if (syllabusIdParam == null || syllabusIdParam.isEmpty()) {
                    response.sendRedirect("syllabus?action=manage");
                    return;
                }
                try {
                    int syllabusId = Integer.parseInt(syllabusIdParam);
                    Syllabus syllabus = syllabusDAO.getSyllabusById(syllabusId);
                    if (syllabus == null) {
                        response.sendRedirect("syllabus?action=manage");
                        return;
                    }
                    request.setAttribute("syllabus", syllabus);
                    request.setAttribute("courseOptions", syllabusDAO.getCourseOptions());
                    request.setAttribute("formMode", "edit");
                    request.setAttribute("home_view", "/academic/syllabus_form.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    response.sendRedirect("syllabus?action=manage");
                }
                break;
            default:
                response.sendRedirect("syllabus?action=manage");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        SyllabusDAO syllabusDAO = new SyllabusDAO();
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        if (currentUser.getRole() == null || !currentUser.getRole().getManageCourse()) {
            session.setAttribute("message", "Security Alert: Unauthorized syllabus modification!");
            session.setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        if ("create".equals(action) || "update".equals(action)) {
            boolean isUpdate = "update".equals(action);
            String courseIdParam = request.getParameter("courseId");
            String syllabusIdParam = request.getParameter("syllabusId");
            String orderIndexParam = request.getParameter("orderIndex");
            String topicName = request.getParameter("topicName");
            String description = request.getParameter("description");

            if ((isUpdate && (syllabusIdParam == null || syllabusIdParam.isEmpty()))
                    || courseIdParam == null || courseIdParam.isEmpty()
                    || orderIndexParam == null || orderIndexParam.isEmpty()
                    || topicName == null || topicName.trim().isEmpty()
                    || description == null || description.trim().isEmpty()) {
                HttpSession asession = request.getSession();
                asession.setAttribute("message", "Please fill all required fields.");
                asession.setAttribute("messageType", "error");
                response.sendRedirect("syllabus?action=manage");
                return;
            }

            try {
                Syllabus syllabus = new Syllabus();
                syllabus.setCourseId(Integer.parseInt(courseIdParam));
                if (isUpdate) {
                    syllabus.setSyllabusId(Integer.parseInt(syllabusIdParam));
                }
                syllabus.setOrderIndex(Integer.parseInt(orderIndexParam));
                syllabus.setTopicName(topicName.trim());
                syllabus.setDescription(description.trim());

                boolean success = isUpdate ? syllabusDAO.updateSyllabus(syllabus) : syllabusDAO.createSyllabus(syllabus);
                HttpSession usession = request.getSession();
                if (success) {
                    usession.setAttribute("message", isUpdate
                            ? "Syllabus updated successfully."
                            : "Syllabus created successfully.");
                    usession.setAttribute("messageType", "success");
                } else {
                    usession.setAttribute("message", isUpdate
                            ? "Failed to update syllabus."
                            : "Failed to create syllabus.");
                    usession.setAttribute("messageType", "error");
                }
            } catch (NumberFormatException e) {
                HttpSession usession = request.getSession();
                session.setAttribute("message", "Invalid syllabus data.");
                session.setAttribute("messageType", "error");
            }

            response.sendRedirect("syllabus?action=manage");
            return;
        }

        if ("delete".equals(action)) {
            HttpSession dsession = request.getSession();
            String syllabusIdParam = request.getParameter("syllabusId");

            if (syllabusIdParam == null || syllabusIdParam.isEmpty()) {
                session.setAttribute("message", "Invalid syllabus delete request.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("syllabus?action=manage");
                return;
            }

            try {
                int syllabusId = Integer.parseInt(syllabusIdParam);
                boolean deleted = syllabusDAO.deleteSyllabus(syllabusId);
                if (deleted) {
                    session.setAttribute("message", "Syllabus deleted successfully.");
                    session.setAttribute("messageType", "success");
                } else {
                    session.setAttribute("message", "Failed to delete syllabus.");
                    session.setAttribute("messageType", "error");
                }
            } catch (NumberFormatException e) {
                session.setAttribute("message", "Invalid syllabus ID.");
                session.setAttribute("messageType", "error");
            }

            response.sendRedirect("syllabus?action=manage");
            return;
        }

        response.sendRedirect("syllabus?action=manage");
    }
}
