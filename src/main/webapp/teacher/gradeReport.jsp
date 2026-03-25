<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container-fluid px-4 content-body">
    <div class="mb-4 mt-3">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=teacher" class="text-decoration-none">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="class" class="text-decoration-none">My Classes</a></li>
                <li class="breadcrumb-item active">Grade Report</li>
            </ol>
        </div>

        <div class="content-header d-flex justify-content-between align-items-center">
            <div>
                <h2 class="page-title fw-bold text-dark mb-1">Grade Report Matrix - ${className}</h2>
                <p class="text-muted small mb-0">Overview of student academic performance and final status</p>
            </div>

            <div class="d-flex gap-2">
                <button onclick="window.print()" class="btn btn-outline-secondary shadow-sm btn-sm px-3">
                    <i class='bx bx-printer'></i> Print
                </button>
                <a href="class" class="btn-secondary" 
                  >
                    <i class='bx bx-arrow-left'></i> Back to Classes
                </a>
            </div>
        </div>
    </div>

    <div class="card shadow-sm border-0 rounded-3 overflow-hidden">
        <div class="card-header bg-white py-3 border-bottom">
            <h5 class="fw-bold mb-0 text-dark"><i class='bx bx-table me-2 text-primary'></i>Grade Report Matrix</h5>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-bordered table-hover align-middle text-center mb-0">
                    <thead class="table-light small">
                        <tr>
                            <th class="text-start ps-4" style="min-width: 250px; background-color: #f8f9fa;">Student Information</th>

                            <c:forEach var="ass" items="${assessmentList}">
                                <th style="min-width: 120px;">
                                    <div class="fw-bold">${ass.assessmentName}</div>
                                    <span class="text-muted d-block" style="font-size: 0.65rem; font-weight: normal;">
                                        Weight: ${ass.weight}%
                                    </span>
                                </th>
                            </c:forEach>

                            <th class="bg-primary bg-opacity-10 text-primary fw-bold" style="min-width: 100px;">Average</th>
                            <th style="min-width: 100px;">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="totalPass" value="0" />
                        <c:set var="totalFail" value="0" />
                        <c:set var="gradedCount" value="0" />

                        <c:forEach var="g" items="${gradeList}">
                            <tr>
                                <td class="text-start ps-4">
                                    <div class="d-flex align-items-center">
                                        <div class="avatar-container me-2" style="width: 35px; height: 35px; position: relative; flex-shrink: 0;">
                                            <c:choose>
                                                <c:when test="${not empty g.avatar}">
                                                    <img src="${pageContext.request.contextPath}/${g.avatar}" 
                                                         class="rounded-circle border" 
                                                         style="width: 100%; height: 100%; object-fit: cover; display: block;" 
                                                         alt="Student"
                                                         onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">

                                                    <div class="avatar-placeholder rounded-circle bg-light align-items-center justify-content-center border" 
                                                         style="display: none; width: 100%; height: 100%; position: absolute; top: 0; left: 0;">
                                                        <i class='bx bx-user text-muted' style="font-size: 1.2rem;"></i>
                                                    </div>
                                                </c:when>

                                                <c:otherwise>
                                                    <div class="avatar-placeholder rounded-circle bg-light d-flex align-items-center justify-content-center border" 
                                                         style="width: 100%; height: 100%;">
                                                        <i class='bx bx-user text-muted' style="font-size: 1.2rem;"></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="ms-1">
                                            <div class="fw-bold text-dark small" style="line-height: 1.2;">${g.fullName}</div>
                                            <small class="text-muted" style="font-size: 0.65rem;">ID: ${g.userId}</small>
                                        </div>
                                    </div>
                                </td>

                                <c:set var="hasVisibleScores" value="false" />
                                <c:forEach var="ass" items="${assessmentList}">
                                    <c:set var="scoreKey" value="${ass.assessmentName}" />
                                    <c:set var="val" value="${g.scores[scoreKey]}" />
                                    <c:if test="${not empty val}">
                                        <c:set var="hasVisibleScores" value="true" />
                                    </c:if>
                                    <td class="${val < 5.0 ? 'text-danger' : ''}">
                                        ${not empty val ? val : '-'}
                                    </td>
                                </c:forEach>

                                <c:set var="finalAvg" value="${avgMap[g.userId]}" />
                                <td class="fw-bold ${hasVisibleScores and not empty finalAvg ? 'text-primary' : 'text-muted'}">
                                    <c:choose>
                                        <c:when test="${hasVisibleScores and not empty finalAvg}">
                                            <fmt:formatNumber value="${finalAvg}" maxFractionDigits="2"/>
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${not hasVisibleScores}">
                                            <span class="badge bg-secondary bg-opacity-10 text-secondary rounded-pill px-3">Not graded</span>
                                        </c:when>
                                        <c:when test="${finalAvg >= 5.0}">
                                            <span class="badge bg-success bg-opacity-10 text-success rounded-pill px-3">Passed</span>
                                            <c:set var="totalPass" value="${totalPass + 1}" />
                                            <c:set var="gradedCount" value="${gradedCount + 1}" />
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger bg-opacity-10 text-danger rounded-pill px-3">Failed</span>
                                            <c:set var="totalFail" value="${totalFail + 1}" />
                                            <c:set var="gradedCount" value="${gradedCount + 1}" />
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot class="table-light fw-bold">
                        <c:set var="totalStudents" value="${gradeList.size()}" />
                        <c:set var="passRate" value="${gradedCount > 0 ? (totalPass / gradedCount) * 100 : 0}" />

                        <tr>
                            <td class="text-start ps-4">TOTAL SUMMARY</td>

                            <td colspan="${assessmentList.size()}" class="text-center">
                                Passing Rate: 
                                <span class="text-primary">
                                    <fmt:formatNumber value="${passRate}" maxFractionDigits="1"/>%
                                </span>
                            </td>

                            <td class="text-success">${totalPass} Pass</td>

                            <td class="text-danger">${totalFail} Fail</td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
        <div class="card-footer bg-light py-2 small d-flex justify-content-between">
            <span class="text-muted fst-italic">* Status is based on an average score of 5.0 or higher.</span>
            <span class="fw-bold text-dark">Total Students: ${gradeList.size()}</span>
        </div>
    </div>
</div>