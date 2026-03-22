
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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

    <form action="class" method="get" class="row mb-4 g-2">
        <input type="hidden" name="action" value="myClasses"/>

        <div class="col-md-5">
            <input type="text"
                   name="keyword"
                   value="${param.keyword}"
                   class="form-control"
                   placeholder="Search class / course / teacher">
        </div>

        <div class="col-md-5">
            <select name="status" class="form-select">
                <option value="">All Status</option>
                <option value="Active" ${param.status == 'Active' ? 'selected' : ''}>Active</option>
                <option value="Completed" ${param.status == 'Completed' ? 'selected' : ''}>Completed</option>
            </select>
        </div>

        <div class="col-md-2">
            <button type="submit"
                    class="btn btn-primary w-100 d-flex justify-content-center align-items-center">
                Search
            </button>
        </div>
    </form>

    <div class="row">

        <c:choose>

            <c:when test="${not empty classList}">

                <c:forEach var="row" items="${classList}">

                    <c:set var="e" value="${row[0]}"/>

                    <div class="col-lg-4 col-md-6 col-sm-12 mb-4">

                        <div class="card shadow-sm h-100 border-0" style="border-radius:8px; padding:5px;">

                            <div class="card-body">

                                <h5 class="card-title d-flex justify-content-between">
                                    Class: ${e.classes.className}

                                    <span class="badge ${e.status == 'Active' ? 'bg-primary' : 'bg-success'} mb-2">
                                        ${e.status}
                                    </span>
                                </h5>

                                <p class="text-muted">
                                    Course: ${e.classes.course.courseName}
                                </p>

                                <p>
                                    <i class="bx bx-user"></i>
                                    Instructor: ${row[1]}
                                </p>

                                <p>
                                    <c:choose>
                                        <c:when test="${not empty row[2]}">
                                            <i class="bx bx-calendar"></i>
                                            ${row[2]}
                                            <span class="mx-1">|</span>
                                            <i class="bx bx-time"></i>
                                            ${row[3]}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted fst-italic">
                                                <i class="bx bx-calendar-x"></i>
                                                No schedule this week
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <p>
                                    <i class="bx bx-map"></i>
                                    Room:
                                    <c:choose>
                                        <c:when test="${not empty row[4]}">
                                            ${row[4]}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">N/A</span>
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <hr>

                                <p>
                                    <strong>Final Grade:</strong>
                                    <c:choose>
                                        <c:when test="${e.finalGrade > 0}">
                                            <fmt:formatNumber value="${e.finalGrade}" maxFractionDigits="2"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Not graded yet</span>
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <div class="d-flex justify-content-end mt-3">
                                    <a href="class?action=detail&classId=${e.classes.classid}&source=myClasses"
                                       class="btn btn-outline-secondary"
                                       style="background-color:#6c757d; color:white; border-color:#6c757d;">
                                        View Details
                                    </a>
                                </div>

                            </div>

                        </div>

                    </div>

                </c:forEach>

            </c:when>

            <c:otherwise>
                <div class="col-12 text-center mt-5">
                    <h5 class="text-muted">No classes found</h5>
                </div>
            </c:otherwise>

        </c:choose>

    </div>

    <c:if test="${totalPages >= 1}">
        <div class="d-flex justify-content-center mt-4">

            <nav>
                <ul class="pagination">

                    <c:if test="${currentPage >= 1}">
                        <li class="page-item">
                            <a class="page-link"
                               href="class?action=myClasses&page=${currentPage - 1}&keyword=${param.keyword}&status=${param.status}">
                                Previous
                            </a>
                        </li>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link"
                               href="class?action=myClasses&page=${i}&keyword=${param.keyword}&status=${param.status}">
                                ${i}
                            </a>
                        </li>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link"
                               href="class?action=myClasses&page=${currentPage + 1}&keyword=${param.keyword}&status=${param.status}">
                                Next
                            </a>
                        </li>
                    </c:if>

                </ul>
            </nav>

        </div>
    </c:if>

</div>

