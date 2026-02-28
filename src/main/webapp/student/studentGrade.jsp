<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<link href="css/viewGrade.css" rel="stylesheet"/>

<div class="container-fluid px-4">

    <!-- ============================= -->
    <!-- ===== BREADCRUMB + HEADER ==== -->
    <!-- ============================= -->

    <div class="mb-4 mt-4">

        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item">
                    <a href="dashboard">
                        <i class="bx bx-home-alt"></i>
                    </a>
                </li>
                <li class="breadcrumb-item active" aria-current="page">
                    My Grades
                </li>
            </ol>
        </div>

        <div class="content-header d-flex justify-content-between align-items-center">
            <div>
                <h2 class="page-title">Grade Overview</h2>
                <p class="text-muted small mb-0">
                    View your academic performance and assessment results.
                </p>
            </div>

            <!--            <form method="get" action="student-grade" class="d-flex">
                            <select name="courseId" class="form-select me-2">
                                <option value="">All Courses</option>
            <c:forEach items="${courseList}" var="c">
                <option value="${c.courseId}"
                <c:if test="${param.courseId == c.courseId}">selected</c:if>>
                ${c.courseName}
            </option>
            </c:forEach>
        </select>
        <button class="btn btn-primary">Filter</button>
    </form>-->
        </div>

    </div>


    <!-- ============================= -->
    <!-- ===== GRADE TABLE CARD ====== -->
    <!-- ============================= -->

    <div class="profile-content-card">

        <div class="section-title">
            <i class='bx bx-bar-chart-alt-2'></i>
            Assessment Results
        </div>

        <c:choose>

            <c:when test="${not empty gradeList}">

                <table class="custom-table">

                    <thead>
                        <tr>
                            <th>Course</th>
                            <th>Class</th>
                            <th>Assessment</th>
                            <th>Score</th>
                            <th>Status</th>
                        </tr>
                    </thead>

                    <tbody>

                        <c:forEach items="${gradeList}" var="g">

                            <tr>
                                <td>
                                    ${g.enrollment.classes.course.courseName}
                                </td>

                                <td>
                                    ${g.enrollment.classes.className}
                                </td>
                                <td>
                                    ${g.assessment.assessmentName}
                                </td>

                                <td class="score-cell">
                                    ${g.score}
                                </td>

                                <td>
                                    <c:choose>

                                        <c:when test="${g.score >= 8}">
                                            <span class="badge-excellent">
                                                Excellent
                                            </span>
                                        </c:when>

                                        <c:when test="${g.score >= 6.5}">
                                            <span class="badge-good">
                                                Good
                                            </span>
                                        </c:when>

                                        <c:when test="${g.score >= 5}">
                                            <span class="badge-average">
                                                Average
                                            </span>
                                        </c:when>

                                        <c:otherwise>
                                            <span class="badge-fail">
                                                Failed
                                            </span>
                                        </c:otherwise>

                                    </c:choose>
                                </td>

                            </tr>

                        </c:forEach>

                    </tbody>

                </table>

            </c:when>

            <c:otherwise>

                <div class="empty-state">
                    <i class='bx bx-info-circle'></i>
                    <p>No grades available at the moment.</p>
                </div>

            </c:otherwise>

        </c:choose>

    </div>

</div>