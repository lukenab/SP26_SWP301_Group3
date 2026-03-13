<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container-fluid student-course-container">

    <div class="mb-4 mt-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item">
                    <a href="dashboard">
                        <i class="bx bx-home-alt"></i>
                    </a>
                </li>
                <li class="breadcrumb-item active">
                    My Classes
                </li>
            </ol>
        </div>

        <div>
            <h2 class="fw-bold mb-1">My Enrolled Classes</h2>
            <p class="text-muted small mb-0">
                View the classes you are currently taking or have completed.
            </p>
        </div>
    </div>

    <div class="row">

        <c:forEach var="row" items="${classList}">

            <c:set var="e" value="${row[0]}"/>

            <div class="col-lg-4 col-md-6 col-sm-12 mb-4">

                <div class="card shadow-sm h-100 border-0" style="border-radius: 8px; padding: 5px;">

                    <div class="card-body">

                        <!-- CLASS NAME -->
                        <h5 class="card-title" style="display: flex; justify-content: space-between">
                            Class: ${e.classes.className}

                            <span class="badge 
                                  ${e.status == 'Active' ? 'bg-primary' : 'bg-success'} mb-2">
                                ${e.status}
                            </span>

                        </h5>

                        <!-- COURSE -->
                        <p class="text-muted">
                            Course: ${e.classes.course.courseName}
                        </p>

                        <!-- TEACHER -->
                        <p>
                            <i class="bx bx-user"></i>
                            Instructor: ${row[1]}
                        </p>

                        <!-- SCHEDULE -->
                        <p>
                            <i class="bx bx-calendar"></i>
                            ${row[2]} |
                            <i class="bx bx-time"></i>
                            ${row[3]}
                        </p>

                        <p>
                            <i class="bx bx-map"></i>
                            Room: ${row[4]}
                        </p>

                        <hr>

                        <!-- FINAL GRADE -->
                        <p>
                            <strong>Final Grade:</strong>
                            <c:choose>
                                <c:when test="${e.finalGrade > 0}">
                                    ${e.finalGrade}
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">Not graded yet</span>
                                </c:otherwise>
                            </c:choose>
                        </p>

                        <!-- BUTTON -->
                        <div class="d-flex justify-content-end mt-3">

                            <a href="class?action=detail&classId=${e.classes.classid}&source=myClasses"
                               class="btn btn-outline-secondary"
                               style="background-color: #6c757d; color: white; border-color: #6c757d;">
                                View Details
                            </a>

                        </div>

                    </div>

                </div>

            </div>

        </c:forEach>

    </div>

</div>