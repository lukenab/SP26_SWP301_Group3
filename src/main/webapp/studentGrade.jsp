<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="grade-container">
    <div class="grade-title">My Grades</div>

    <c:choose>
        <c:when test="${not empty gradeList}">

            <!-- ===== Summary Section ===== -->
            <c:set var="totalScore" value="0" />
            <c:set var="count" value="0" />

            <c:forEach items="${gradeList}" var="g">
                <c:set var="totalScore" value="${totalScore + g.score}" />
                <c:set var="count" value="${count + 1}" />
            </c:forEach>

            <c:set var="avg" value="${count > 0 ? totalScore / count : 0}" />

            <div class="grade-summary">
                <div class="summary-card">
                    <div class="summary-title">Average Score</div>
                    <div class="summary-value">
                        <fmt:formatNumber value="${avg}" maxFractionDigits="2"/>
                    </div>
                </div>

                <div class="summary-card">
                    <div class="summary-title">Total Assessments</div>
                    <div class="summary-value">${count}</div>
                </div>
            </div>

            <!-- ===== Table ===== -->
            <table class="grade-table">
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
                            <td data-label="Course">
                                ${g.enrollment.classes.course.courseName}
                            </td>
                            <td data-label="Class">
                                ${g.enrollment.classes.className}
                            </td>
                            <td data-label="Assessment">
                                ${g.assessment.assessmentName}
                            </td>
                            <td data-label="Score">
                                ${g.score}
                            </td>
                            <td data-label="Status">
                                <c:choose>
                                    <c:when test="${g.score >= 8}">
                                        <span class="grade-badge grade-excellent">
                                            Excellent
                                        </span>
                                    </c:when>
                                    <c:when test="${g.score >= 6.5}">
                                        <span class="grade-badge grade-good">
                                            Good
                                        </span>
                                    </c:when>
                                    <c:when test="${g.score >= 5}">
                                        <span class="grade-badge grade-average">
                                            Average
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="grade-badge grade-fail">
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
            <div class="grade-empty">
                No grades available.
            </div>
        </c:otherwise>
    </c:choose>
</div>