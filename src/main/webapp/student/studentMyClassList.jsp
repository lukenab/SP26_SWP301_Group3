<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container-fluid student-course-container">

    <!-- HEADER -->
    <div class="mb-4 mt-4">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item">
                <a href="dashboard"><i class="bx bx-home-alt"></i></a>
            </li>
            <li class="breadcrumb-item active">My Classes</li>
        </ol>

        <h2 class="fw-bold mb-1">My Enrolled Classes</h2>
        <p class="text-muted small">
            View the classes you are currently taking or have completed.
        </p>
    </div>

    <!-- SEARCH -->
    <form action="class" method="get" class="row mb-4 g-2">
        <input type="hidden" name="action" value="myClasses"/>

        <div class="col-md-5">
            <input type="text"
                   name="keyword"
                   value="${keyword}"
                   class="form-control"
                   placeholder="Search class / course / teacher">
        </div>

        <div class="col-md-5">
            <select name="status" class="form-select">
                <option value="">All Status</option>
                <option value="UnPaid" ${status eq 'UnPaid' || status eq 'Unpaid' ? 'selected' : ''}>UnPaid</option>
                <option value="Paid" ${status eq 'Paid' ? 'selected' : ''}>Paid</option>
                <option value="Active" ${status eq 'Active' ? 'selected' : ''}>Active</option>
                <option value="Completed" ${status eq 'Completed' ? 'selected' : ''}>Completed</option>
            </select>
        </div>

        <div class="col-md-2">
            <button type="submit" class="btn btn-primary w-100">
                Search
            </button>
        </div>
    </form>

    <!-- LIST -->
    <div class="row">

        <c:choose>

            <c:when test="${not empty classList}">

                <c:forEach var="row" items="${classList}">
                    <c:set var="e" value="${row[0]}"/>

                    <div class="col-lg-4 col-md-6 mb-4">

                        <div class="card shadow-sm h-100">

                            <div class="card-body">

                                <!-- TITLE -->
                                <h5 class="d-flex justify-content-between">
                                    ${e.classes.className}

                                    <span class="badge ${e.status eq 'Active' ? 'bg-primary' : (e.status eq 'Completed' ? 'bg-success' : (e.status eq 'Paid' ? 'bg-info text-dark' : 'bg-warning text-dark'))}">
                                        ${e.status}
                                    </span>
                                </h5>

                                <!-- COURSE -->
                                <p class="text-muted">
                                    ${e.classes.course.courseName}
                                </p>

                                <!-- TEACHER -->
                                <p>
                                    <i class="bx bx-user"></i>
                                    ${row[1]}
                                </p>

                                <!-- SCHEDULE -->
                                <p>
                                    <c:choose>
                                        <c:when test="${not empty row[2]}">
                                            <i class="bx bx-calendar"></i> ${row[2]}
                                            |
                                            <i class="bx bx-time"></i> ${row[3]}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">
                                                No schedule this week
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <!-- ROOM -->
                                <p>
                                    <i class="bx bx-map"></i>
                                    <c:choose>
                                        <c:when test="${not empty row[4]}">
                                            ${row[4]}
                                        </c:when>
                                        <c:otherwise>
                                            N/A
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <hr>

                                <!-- FINAL GRADE (FIX QUAN TRỌNG) -->
                                <p>
                                    <strong>Final Grade:</strong>

                                    <c:choose>
                                        <c:when test="${e.status eq 'Active' || e.status eq 'Completed'}">
                                            <c:choose>
                                                <c:when test="${e.finalGrade ne -1}">
                                                    <fmt:formatNumber value="${e.finalGrade}" pattern="#0.00"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Not graded yet</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Locked until enrollment is active</span>
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <c:if test="${e.status ne 'Active' && e.status ne 'Completed'}">
                                    <div class="alert alert-warning py-2 px-3 small mb-3">
                                        This class is reserved for you, but attendance and grades will unlock after the enrollment becomes Active.
                                    </div>
                                </c:if>

                                <!-- BUTTON -->
                                <div class="text-end">
                                    <a href="class?action=detail&classId=${e.classes.classid}&source=myClasses"
                                       class="btn btn-primary">
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

            <!-- PREVIOUS -->
            <c:if test="${currentPage > 1}">
                <a href="class?action=myClasses&page=${currentPage - 1}&keyword=${keyword}&status=${status}"
                   class="btn btn-outline-primary me-2">
                    Previous
                </a>
            </c:if>

            <!-- PAGE INFO -->
            <span class="align-self-center fw-bold">
                Page ${currentPage} / ${totalPages}
            </span>

            <!-- NEXT -->
            <c:if test="${currentPage < totalPages}">
                <a href="class?action=myClasses&page=${currentPage + 1}&keyword=${keyword}&status=${status}"
                   class="btn btn-outline-primary ms-2">
                    Next
                </a>
            </c:if>

        </div>
    </c:if>

</div>
