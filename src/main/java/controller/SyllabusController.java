package controller;

import dao.SyllabusDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Syllabus;

@WebServlet(name = "SyllabusController", urlPatterns = {"/syllabus"})
public class SyllabusController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "manage";
        }

        SyllabusDAO syllabusDAO = new SyllabusDAO();

        switch (action) {
            case "manage":
                List<Syllabus> syllabusList = syllabusDAO.getAllSyllabus();
                request.setAttribute("syllabusList", syllabusList);
                request.setAttribute("home_view", "/academic/syllabus_management.jsp");
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
        if ("update".equals(action)) {
            String syllabusIdParam = request.getParameter("syllabusId");
            String orderIndexParam = request.getParameter("orderIndex");
            String topicName = request.getParameter("topicName");
            String description = request.getParameter("description");

            if (syllabusIdParam == null || syllabusIdParam.isEmpty()
                    || orderIndexParam == null || orderIndexParam.isEmpty()
                    || topicName == null || topicName.trim().isEmpty()
                    || description == null || description.trim().isEmpty()) {
                HttpSession session = request.getSession();
                session.setAttribute("message", "Please fill all required fields.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("syllabus?action=manage");
                return;
            }

            try {
                Syllabus syllabus = new Syllabus();
                syllabus.setSyllabusId(Integer.parseInt(syllabusIdParam));
                syllabus.setOrderIndex(Integer.parseInt(orderIndexParam));
                syllabus.setTopicName(topicName.trim());
                syllabus.setDescription(description.trim());

                boolean updated = syllabusDAO.updateSyllabus(syllabus);
                HttpSession session = request.getSession();
                if (updated) {
                    session.setAttribute("message", "Syllabus updated successfully.");
                    session.setAttribute("messageType", "success");
                } else {
                    session.setAttribute("message", "Failed to update syllabus.");
                    session.setAttribute("messageType", "error");
                }
            } catch (NumberFormatException e) {
                HttpSession session = request.getSession();
                session.setAttribute("message", "Invalid syllabus data.");
                session.setAttribute("messageType", "error");
            }

            response.sendRedirect("syllabus?action=manage");
            return;
        }
        response.sendRedirect("syllabus?action=manage");
    }
}
