package controller;

import dao.LeadDAO;
import dao.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Lead;
import model.Course;

@WebServlet(name = "LeadController", urlPatterns = {"/lead"})
public class LeadController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        LeadDAO leadDAO = new LeadDAO();
        CourseDAO courseDAO = new CourseDAO();

        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "all":
                List<Lead> leadList = leadDAO.getAllLeads();
                request.setAttribute("leadList", leadList);
                request.setAttribute("totalLeads", leadList.size());
                request.setAttribute("home_view", "/sale/viewLeadList.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "add":
                List<Course> courseList = courseDAO.getActiveCourses();
                request.setAttribute("courseList", courseList);
                request.setAttribute("home_view", "/Lead/AddLead.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "detail":
                int detailId = Integer.parseInt(request.getParameter("id"));
                Lead detailLead = leadDAO.getLeadByID(detailId);
                if (detailLead == null || "Inactive".equalsIgnoreCase(detailLead.getStatus())) {
                    request.getSession().setAttribute("message", "Inactive lead cannot be viewed.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("lead?action=all");
                    return;
                }
                request.setAttribute("lead", detailLead);
                request.setAttribute("home_view", "/sale/leadDetail.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Lead lead = leadDAO.getLeadByID(editId);
                if (lead == null || "Inactive".equalsIgnoreCase(lead.getStatus())) {
                    request.getSession().setAttribute("message", "Inactive lead cannot be edited.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("lead?action=all");
                    return;
                }
                request.setAttribute("lead", lead);
                request.setAttribute("home_view", "/sale/editLead.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                Lead dLead = leadDAO.getLeadByID(deleteId);
                request.setAttribute("dLead", dLead);
                request.setAttribute("home_view", "/sale/deleteLead.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("lead?action=all");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        LeadDAO leadDAO = new LeadDAO();
        HttpSession session = request.getSession();

        if ("create".equals(action)) {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            int interestedCourseID = Integer.parseInt(request.getParameter("interestedCourseID"));
            String status = normalizeStatus(request.getParameter("status"));
            String note = request.getParameter("note");

            Lead lead = new Lead();
            lead.setFullName(fullName);
            lead.setEmail(email);
            lead.setPhone(phone);
            lead.setInterestedCourseID(interestedCourseID);
            lead.setStatus(status);
            lead.setNote(note);
            leadDAO.insertLead(lead);

            session.setAttribute("message", "Add new lead successfully!");
            session.setAttribute("messageType", "success");
            response.sendRedirect("lead?action=all");
            return;
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("leadId"));
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            int interestedCourseID = Integer.parseInt(request.getParameter("interestedCourseID"));
            String status = normalizeStatus(request.getParameter("status"));
            String note = request.getParameter("note");

            boolean updated = leadDAO.updateLead(id, fullName, email, phone, interestedCourseID, status, note);
            if (updated) {
                session.setAttribute("message", "Update lead successfully!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Update lead failed.");
                session.setAttribute("messageType", "error");
            }
            response.sendRedirect("lead?action=all");
            return;
        }

        if ("delete".equals(action)) {
            String idParam = request.getParameter("leadID");
            if (idParam == null || idParam.isEmpty()) {
                idParam = request.getParameter("leadId");
            }
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                leadDAO.deleteLead(id);
                session.setAttribute("message", "Lead has been moved to inactive.");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Delete lead failed.");
                session.setAttribute("messageType", "error");
            }
            response.sendRedirect("lead?action=all");
            return;
        }

        if ("restore".equals(action)) {
            String idParam = request.getParameter("leadID");
            if (idParam == null || idParam.isEmpty()) {
                idParam = request.getParameter("leadId");
            }
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                leadDAO.restoreLead(id);
                session.setAttribute("message", "Lead has been restored to new.");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Restore lead failed.");
                session.setAttribute("messageType", "error");
            }
            response.sendRedirect("lead?action=all");
            return;
        }

        response.sendRedirect("lead?action=all");
    }

    private String normalizeStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "New";
        }

        String cleaned = status.trim().toLowerCase();
        switch (cleaned) {
            case "new":
                return "New";
            case "contacted":
                return "Contacted";
            case "consulting":
                return "Consulting";
            case "converted":
                return "Converted";
            case "lost":
                return "Lost";
            case "inactive":
                return "Inactive";
            default:
                return status.trim();
        }
    }

}
