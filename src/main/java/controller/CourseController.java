/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CourseDAO;
import dao.FeedbackDAO;
import dao.SyllabusDAO;
import dao.AssessmentDAO;
import dao.SystemLogDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.util.List;
import java.math.BigDecimal;
import java.io.File;
import java.util.Collections;
import model.Employee;
import model.Course;
import model.Syllabus;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "CourseController", urlPatterns = {"/course"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class CourseController extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private void prepareCourseForm(HttpServletRequest request, Course course, String formAction, String pageTitle) {
        request.setAttribute("course", course);
        request.setAttribute("formAction", formAction);
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("home_view", "/academic/course_form.jsp");
    }

    private String uploadCourseImage(HttpServletRequest request, String partName, String fallbackImage) {
        try {
            Part filePart = request.getPart(partName);
            if (filePart == null || filePart.getSize() <= 0) {
                return fallbackImage == null ? "" : fallbackImage;
            }

            String submittedFileName = java.nio.file.Paths.get(filePart.getSubmittedFileName())
                    .getFileName()
                    .toString();
            if (submittedFileName == null || submittedFileName.trim().isEmpty()) {
                return fallbackImage == null ? "" : fallbackImage;
            }

            String sanitizedFileName = submittedFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String uniqueFileName = System.currentTimeMillis() + "_" + sanitizedFileName;

            String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            filePart.write(uploadPath + File.separator + uniqueFileName);
            return uniqueFileName;
        } catch (Exception e) {
            System.out.println("Error uploading course image: " + e.getMessage());
            return fallbackImage == null ? "" : fallbackImage;
        }
    }

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

    private List<Course> paginateCourseList(List<Course> source, int page, int pageSize, HttpServletRequest request) {
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

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        CourseDAO courseDAO = new CourseDAO();
        if (action == null) {
            action = "all";
        }

        HttpSession uSession = request.getSession();
        User currentUser = (User) uSession.getAttribute("user");

        if (!"publicDetails".equals(action)) {
            if (currentUser == null) {
                response.sendRedirect("login");
                return;
            }
            if (currentUser.getRole() == null || !currentUser.getRole().getManageCourse()) {
                uSession.setAttribute("message", "Access Denied: You don't have permission to manage courses!");
                uSession.setAttribute("messageType", "error");

                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }
        }

        switch (action) {
            case "all":
                String searchQuery = request.getParameter("searchQuery");
                String statusFilter = request.getParameter("status");
                String categoryFilter = request.getParameter("category");
                String normalizedSearchQuery = searchQuery == null ? "" : searchQuery.trim();
                String normalizedStatusFilter = statusFilter == null ? "all" : statusFilter.trim().toLowerCase();
                String normalizedCategoryFilter = categoryFilter == null ? "all" : categoryFilter.trim().toLowerCase();

                List<Course> list = normalizedSearchQuery.isEmpty()
                        ? courseDAO.getAllCourse()
                        : courseDAO.searchCourses(normalizedSearchQuery);

                if (!"all".equals(normalizedCategoryFilter)) {
                    list.removeIf(course -> {
                        String courseName = course.getCourseName();
                        if (courseName == null) {
                            return true;
                        }
                        return !courseName.trim().toLowerCase().startsWith(normalizedCategoryFilter);
                    });
                }

                if ("active".equals(normalizedStatusFilter)) {
                    list.removeIf(course -> !course.isStatus());
                } else if ("inactive".equals(normalizedStatusFilter)) {
                    list.removeIf(Course::isStatus);
                }

                int totalCourse = list.size();
                List<Course> pagedList;
                boolean shouldShowAllResults = !normalizedSearchQuery.isEmpty()
                        || !"all".equals(normalizedStatusFilter)
                        || !"all".equals(normalizedCategoryFilter);
                if (shouldShowAllResults) {
                    pagedList = list;
                    request.setAttribute("currentPage", 1);
                    request.setAttribute("pageSize", totalCourse == 0 ? DEFAULT_PAGE_SIZE : totalCourse);
                    request.setAttribute("totalItems", totalCourse);
                    request.setAttribute("totalPages", 1);
                    request.setAttribute("startItem", totalCourse == 0 ? 0 : 1);
                    request.setAttribute("endItem", totalCourse);
                } else {
                    pagedList = paginateCourseList(list, parsePage(request), DEFAULT_PAGE_SIZE, request);
                }
                request.setAttribute("totalCourse", totalCourse);
                request.setAttribute("courseList", pagedList);
                request.setAttribute("filteredCourseList", list);
                request.setAttribute("searchQuery", normalizedSearchQuery);
                request.setAttribute("statusFilter", normalizedStatusFilter);
                request.setAttribute("categoryFilter", normalizedCategoryFilter);
                request.setAttribute("showAllFilteredResults", shouldShowAllResults);
                request.setAttribute("paginationAction", "all");
                StringBuilder paginationQuery = new StringBuilder();
                if (!normalizedSearchQuery.isEmpty()) {
                    paginationQuery.append("&searchQuery=").append(normalizedSearchQuery);
                }
                if (!normalizedStatusFilter.isEmpty()) {
                    paginationQuery.append("&status=").append(normalizedStatusFilter);
                }
                if (!normalizedCategoryFilter.isEmpty()) {
                    paginationQuery.append("&category=").append(normalizedCategoryFilter);
                }
                request.setAttribute("paginationQuery", paginationQuery.toString());
                request.setAttribute("home_view", "/academic/course_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "active":
                List<Course> activeList = courseDAO.getActiveCourses();
                request.setAttribute("courseList", paginateCourseList(activeList, parsePage(request), DEFAULT_PAGE_SIZE, request));
                request.setAttribute("paginationAction", "active");
                request.setAttribute("paginationQuery", "");
                request.setAttribute("totalCourse", activeList.size());
                request.setAttribute("home_view", "/academic/course_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "details":
                String courseIdParam = request.getParameter("courseId");
                if (courseIdParam != null && !courseIdParam.isEmpty()) {
                    try {
                        int courseId = Integer.parseInt(courseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if (course != null) {
                            request.setAttribute("course", course);
                            HttpSession session = request.getSession(false);
                            boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
                            if (isLoggedIn) {
                                request.setAttribute("home_view", "/academic/course_details.jsp");
                                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                            } else {
                                if (!course.isStatus()) {
                                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                                    return;
                                }
                                request.getRequestDispatcher("/academic/course_details.jsp").forward(request, response);
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "publicDetails":
                String publicCourseIdParam = request.getParameter("courseId");
                if (publicCourseIdParam == null || publicCourseIdParam.isEmpty()) {
                    publicCourseIdParam = request.getParameter("id");
                }

                if (publicCourseIdParam != null && !publicCourseIdParam.isEmpty()) {
                    try {
                        int courseId = Integer.parseInt(publicCourseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if (course != null && course.isStatus()) {
                            SyllabusDAO syllabusDAO = new SyllabusDAO();
                            FeedbackDAO feedbackDAO = new FeedbackDAO();
                            List<Syllabus> syllabusList = syllabusDAO.getSyllabusByCourseId(courseId);
                            Object[] instructorProfile = courseDAO.getInstructorProfileByCourseId(courseId);
                            List<Object[]> reviewList = feedbackDAO.getCourseReviews(courseId);

                            request.setAttribute("course", course);
                            request.setAttribute("syllabusList", syllabusList);
                            request.setAttribute("reviewList", reviewList);
                            request.setAttribute("reviewCount", reviewList.size());
                            request.setAttribute("averageRating", feedbackDAO.getAverageRatingByCourseId(courseId));

                            if (instructorProfile != null) {
                                request.setAttribute("instructor", (User) instructorProfile[0]);
                                request.setAttribute("instructorEmployee", (Employee) instructorProfile[1]);
                                request.setAttribute("instructorClassCount", (Integer) instructorProfile[2]);
                            }

                            request.getRequestDispatcher("courseDetailPublic.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "add":
                // Show add form
                prepareCourseForm(request, new Course(), "add", "Add New Course");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "edit":
                String editCourseIdParam = request.getParameter("courseId");
                if (editCourseIdParam != null && !editCourseIdParam.isEmpty()) {
                    try {
                        int courseId = Integer.parseInt(editCourseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if (course != null) {
                            prepareCourseForm(request, course, "update", "Edit Course");
                            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "delete":
                String deleteCourseIdParam = request.getParameter("courseId");
                if (deleteCourseIdParam != null && !deleteCourseIdParam.isEmpty()) {
                    try {
                        int courseId = Integer.parseInt(deleteCourseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if (course != null) {
                            request.setAttribute("course", course);
                            request.setAttribute("home_view", "/academic/course_delete_confirm.jsp");
                            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "assessment":
                String assessmentCourseIdParam = request.getParameter("courseId");
                if (assessmentCourseIdParam != null && !assessmentCourseIdParam.isEmpty()) {
                    try {
                        int courseId = Integer.parseInt(assessmentCourseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if (course != null) {
                            AssessmentDAO assessmentDAO = new AssessmentDAO();
                            List<model.Assessment> assessments = assessmentDAO.getAssessmentsByCourse(courseId);
                            double totalWeight = assessmentDAO.getTotalWeightByCourse(courseId);

                            request.setAttribute("course", course);
                            request.setAttribute("assessments", assessments);
                            request.setAttribute("totalWeight", totalWeight);
                            request.setAttribute("home_view", "/academic/assessment_management.jsp");
                            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "deleteAssessment":
                String delAssessmentIdParam = request.getParameter("assessmentId");
                String delCourseIdParam = request.getParameter("courseId");
                if (delAssessmentIdParam != null && delCourseIdParam != null) {
                    try {
                        int assessmentId = Integer.parseInt(delAssessmentIdParam);
                        int courseId = Integer.parseInt(delCourseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if (course != null) {
                            AssessmentDAO assessmentDAO = new AssessmentDAO();
                            model.Assessment assessment = assessmentDAO.getAssessmentById(assessmentId);
                            if (assessment != null) {
                                request.setAttribute("course", course);
                                request.setAttribute("assessment", assessment);
                                request.setAttribute("home_view", "/academic/assessment_delete_confirm.jsp");
                                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                            } else {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "editAssessment":
                String editAsmtIdParam = request.getParameter("assessmentId");
                String editAsmtCourseIdParam = request.getParameter("courseId");
                if (editAsmtIdParam != null && editAsmtCourseIdParam != null) {
                    try {
                        int assessmentId = Integer.parseInt(editAsmtIdParam);
                        int courseId = Integer.parseInt(editAsmtCourseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if (course != null) {
                            AssessmentDAO assessmentDAO = new AssessmentDAO();
                            model.Assessment assessment = assessmentDAO.getAssessmentById(assessmentId);
                            if (assessment != null) {
                                request.setAttribute("course", course);
                                request.setAttribute("assessment", assessment);
                                request.setAttribute("home_view", "/academic/assessment_edit_confirm.jsp");
                                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                            } else {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "search":
                String keyword = request.getParameter("keyword");
                List<Course> searchResults;
                if (keyword != null && !keyword.trim().isEmpty()) {
                    searchResults = courseDAO.searchCourses(keyword.trim());
                } else {
                    searchResults = courseDAO.getAllCourse();
                }
                request.setAttribute("courseList", paginateCourseList(searchResults, parsePage(request), DEFAULT_PAGE_SIZE, request));
                request.setAttribute("totalCourse", searchResults.size());
                request.setAttribute("searchKeyword", keyword);
                request.setAttribute("paginationAction", "search");
                request.setAttribute("paginationQuery", keyword != null && !keyword.trim().isEmpty() ? "&keyword=" + keyword.trim() : "");
                request.setAttribute("home_view", "/academic/course_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            default:
                List<Course> defaultList = courseDAO.getAllCourse();
                request.setAttribute("courseList", paginateCourseList(defaultList, parsePage(request), DEFAULT_PAGE_SIZE, request));
                request.setAttribute("totalCourse", defaultList.size());
                request.setAttribute("paginationAction", "all");
                request.setAttribute("paginationQuery", "");
                request.setAttribute("home_view", "/academic/course_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        CourseDAO courseDAO = new CourseDAO();

        if (action == null) {
            action = "list";
        }

        HttpSession uSession = request.getSession();
        User currentUser = (User) uSession.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        if (currentUser.getRole() == null || !currentUser.getRole().getManageCourse()) {
            uSession.setAttribute("message", "Access Denied: You don't have permission to manage courses!");
            uSession.setAttribute("messageType", "error");
            response.sendRedirect("dashboard");
            return;
        }

        switch (action) {
            case "add":
                String addReturnUrl = request.getParameter("returnUrl");
                // Get form data
                String courseName = request.getParameter("courseName");
                String description = request.getParameter("description");
                String totalSlotsStr = request.getParameter("totalSlots");
                String tuitionFeeStr = request.getParameter("tuitionFee");
                String statusStr = request.getParameter("status");
                String images = uploadCourseImage(request, "imageFile", "");

                // Validate input
                if (courseName == null || courseName.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Course name is required");
                    Course invalidCourse = new Course();
                    invalidCourse.setDescription(description != null ? description.trim() : "");
                    invalidCourse.setImages(images);
                    prepareCourseForm(request, invalidCourse, "add", "Add New Course");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    return;
                }

                try {
                    int totalSlots = Integer.parseInt(totalSlotsStr);
                    BigDecimal tuitionFee = new BigDecimal(tuitionFeeStr);
                    boolean status = Boolean.parseBoolean(statusStr);

                    Course newCourse = new Course();
                    newCourse.setCourseName(courseName.trim());
                    newCourse.setDescription(description != null ? description.trim() : "");
                    newCourse.setTotalSlots(totalSlots);
                    newCourse.setTuitionFee(tuitionFee);
                    newCourse.setStatus(status);
                    newCourse.setImages(images != null ? images.trim() : "");

                    boolean success = courseDAO.addCourse(newCourse);
                    if (success) {

                        SystemLogDAO logDAO = new SystemLogDAO();
                        User logUser = (User) request.getSession().getAttribute("user");

                        String actorName = (logUser != null) ? logUser.getFullName() : "System";
                        String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Academic Staff";
                        logDAO.insertLog(actorName, actorRole, "CREATE_COURSE", "Created new course: " + newCourse.getCourseName());

                        uSession.setAttribute("message", "Course added successfully.");
                        uSession.setAttribute("messageType", "success");
                        response.sendRedirect(addReturnUrl != null && !addReturnUrl.isEmpty()
                                ? addReturnUrl
                                : request.getContextPath() + "/course?action=all");
                    } else {
                        request.setAttribute("errorMessage", "Failed to add course");
                        prepareCourseForm(request, newCourse, "add", "Add New Course");
                        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid number format for slots or fee");
                    Course invalidCourse = new Course();
                    invalidCourse.setCourseName(courseName != null ? courseName.trim() : "");
                    invalidCourse.setDescription(description != null ? description.trim() : "");
                    invalidCourse.setImages(images);
                    prepareCourseForm(request, invalidCourse, "add", "Add New Course");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                }
                break;

            case "update":
                String updateReturnUrl = request.getParameter("returnUrl");
                String courseIdStr = request.getParameter("courseId");
                if (courseIdStr == null || courseIdStr.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                try {
                    int courseId = Integer.parseInt(courseIdStr);
                    Course existingCourse = courseDAO.getCourseById(courseId);
                    if (existingCourse == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }

                    // Get form data
                    String updCourseName = request.getParameter("courseName");
                    String updDescription = request.getParameter("description");
                    String updTotalSlotsStr = request.getParameter("totalSlots");
                    String updTuitionFeeStr = request.getParameter("tuitionFee");
                    String updStatusStr = request.getParameter("status");
                    String updImages = uploadCourseImage(request, "imageFile", request.getParameter("images"));

                    // Validate input
                    if (updCourseName == null || updCourseName.trim().isEmpty()) {
                        request.setAttribute("errorMessage", "Course name is required");
                        request.setAttribute("course", existingCourse);
                        prepareCourseForm(request, existingCourse, "update", "Edit Course");
                        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        return;
                    }

                    int updTotalSlots = Integer.parseInt(updTotalSlotsStr);
                    BigDecimal updTuitionFee = new BigDecimal(updTuitionFeeStr);
                    boolean updStatus = (updStatusStr == null) ? existingCourse.isStatus() : Boolean.parseBoolean(updStatusStr);

                    existingCourse.setCourseName(updCourseName.trim());
                    existingCourse.setDescription(updDescription != null ? updDescription.trim() : "");
                    existingCourse.setTotalSlots(updTotalSlots);
                    existingCourse.setTuitionFee(updTuitionFee);
                    existingCourse.setStatus(updStatus);
                    existingCourse.setImages(updImages != null ? updImages.trim() : "");

                    boolean success = courseDAO.updateCourse(existingCourse);
                    if (success) {

                        SystemLogDAO logDAO = new SystemLogDAO();
                        User logUser = (User) request.getSession().getAttribute("user");

                        String actorName = (logUser != null) ? logUser.getFullName() : "System";
                        String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Academic Staff";
                        logDAO.insertLog(actorName, actorRole, "UPDATE_COURSE", "Updated course ID " + courseId + " (" + existingCourse.getCourseName() + ")");

                        uSession.setAttribute("message", "Course updated successfully.");
                        uSession.setAttribute("messageType", "success");
                        response.sendRedirect(updateReturnUrl != null && !updateReturnUrl.isEmpty()
                                ? updateReturnUrl
                                : request.getContextPath() + "/course?action=all");
                    } else {
                        request.setAttribute("errorMessage", "Failed to update course");
                        prepareCourseForm(request, existingCourse, "update", "Edit Course");
                        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid number format for slots or fee");
                    String id = request.getParameter("courseId");
                    if (id != null) {
                        try {
                            Course course = courseDAO.getCourseById(Integer.parseInt(id));
                            prepareCourseForm(request, course, "update", "Edit Course");
                        } catch (Exception ex) {
                            prepareCourseForm(request, new Course(), "update", "Edit Course");
                        }
                    }
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                }
                break;

            case "delete":
                String deleteCourseIdStr = request.getParameter("courseId");
                if (deleteCourseIdStr == null || deleteCourseIdStr.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                try {
                    int deleteCourseId = Integer.parseInt(deleteCourseIdStr);
                    boolean success = courseDAO.deleteCourse(deleteCourseId);
                    HttpSession session = request.getSession();
                    if (success) {

                        SystemLogDAO logDAO = new SystemLogDAO();
                        User logUser = (User) request.getSession().getAttribute("user");

                        String actorName = (logUser != null) ? logUser.getFullName() : "System";
                        String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Academic Staff";
                        logDAO.insertLog(actorName, actorRole, "DEACTIVATE_COURSE", "Deactivated course ID: " + deleteCourseId);

                        session.setAttribute("message", "Inactivate Course Success!");
                        session.setAttribute("messageType", "success");
                    } else {
                        session.setAttribute("message", "Fail To Inactivate Course");
                        session.setAttribute("messageType", "error");
                    }
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                } catch (NumberFormatException e) {
                    HttpSession session = request.getSession();
                    session.setAttribute("message", "Invalid course ID");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                }
                break;

            case "activate":
                String activateCourseIdStr = request.getParameter("courseId");
                if (activateCourseIdStr == null || activateCourseIdStr.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                try {
                    int activateCourseId = Integer.parseInt(activateCourseIdStr);
                    boolean success = courseDAO.activateCourse(activateCourseId);
                    HttpSession session = request.getSession();
                    if (success) {

                        SystemLogDAO logDAO = new SystemLogDAO();
                        User logUser = (User) request.getSession().getAttribute("user");

                        String actorName = (logUser != null) ? logUser.getFullName() : "System";
                        String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Academic Staff";
                        logDAO.insertLog(actorName, actorRole, "ACTIVATE_COURSE", "Activated course ID: " + activateCourseId);

                        session.setAttribute("message", "Activate Course Success!");
                        session.setAttribute("messageType", "success");
                    } else {
                        session.setAttribute("message", "Fail To Activate Course");
                        session.setAttribute("messageType", "error");
                    }
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                } catch (NumberFormatException e) {
                    HttpSession session = request.getSession();
                    session.setAttribute("message", "Invalid course ID");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                }
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/course?action=all");
                break;
        }
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
