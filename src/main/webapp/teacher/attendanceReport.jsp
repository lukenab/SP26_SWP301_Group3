<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container-fluid px-4 content-body">
    <div class="mb-4 mt-3">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=teacher" class="text-decoration-none">Dashboard</a></li>
                <li class="breadcrumb-item">
                    <a href="class" class="text-decoration-none">My Classes</a>
                </li>
                <li class="breadcrumb-item active">Attendance Matrix</li>
            </ol>
        </div>

        <div class="content-header d-flex justify-content-between align-items-center">
            <div>
                <h2 class="page-title fw-bold text-dark mb-1">Attendance Report Matrix</h2>
                <p class="text-muted small mb-0">
                    Tracking attendance status and absence percentage for class: 
                    <strong class="text-primary">
                        <c:choose>
                            <c:when test="${not empty className}">${className}</c:when>
                            <c:otherwise>${scheduleList[0].classes.className}</c:otherwise>
                        </c:choose>
                    </strong>
                </p>
            </div>

            <div class="d-flex gap-2">
                <button onclick="window.print()" class="btn btn-outline-secondary shadow-sm btn-sm px-3">
                    <i class='bx bx-printer'></i> Print
                </button>
                <a href="class?action=all" class="btn-secondary" 
                   >
                    <i class='bx bx-arrow-left me-1'></i> Back to Dashboard
                </a>
            </div>
        </div>
    </div>

    <div class="card shadow-sm border-0 rounded-3 overflow-hidden">
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle text-center mb-0">
            </table>
        </div>
    </div>
</div>

<div class="container-fluid px-4 mt-3">
    <div class="card shadow-sm border-0 rounded-3">
        <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center border-bottom">
            <h5 class="fw-bold mb-0 text-dark"><i class='bx bx-table me-2 text-primary'></i>Attendance Report Matrix</h5>

        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-bordered table-hover align-middle text-center mb-0">
                    <jsp:useBean id="today" class="java.util.Date" />
                    <fmt:formatDate var="currentDate" value="${today}" pattern="yyyy-MM-dd" />
                    <thead class="table-light small">
                        <tr>
                            <th class="text-start ps-3" style="min-width: 220px; background-color: #f8f9fa;">Student Name</th>
                                <c:forEach var="sch" items="${scheduleList}">
                                <th class="p-2" style="min-width: 100px;">
                                    <div class="fw-bold text-dark" style="font-size: 0.75rem;">${sch.learningDate}</div>
                                    <div class="text-muted fw-normal" style="font-size: 0.65rem;">Slot ${sch.slot.slotID}</div>
                                </th>
                            </c:forEach>
                            <th class="bg-light fw-bold text-danger">Absent %</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="stu" items="${studentList}">
                            <tr>
                                <td class="text-start ps-3">
                                    <div class="d-flex align-items-center">
                                        <div class="avatar-container me-2" style="width: 35px; height: 35px; position: relative;">
                                            <c:choose>
                                                <c:when test="${not empty stu.avatar}">
                                                    <img src="${pageContext.request.contextPath}/${stu.avatar}" 
                                                         class="rounded-circle" 
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

                                        <div class="d-flex flex-column">
                                            <span class="fw-bold text-dark small" style="line-height: 1.2;">${stu.fullName}</span>
                                            <small class="text-muted" style="font-size: 0.65rem;">ID: ${stu.userId}</small>
                                        </div>
                                    </div>
                                </td>

                                <c:set var="absentCount" value="0" />
                                <c:forEach var="sch" items="${scheduleList}">
                                    <c:set var="key" value="${stu.userId}_${sch.scheduleId}" />
                                    <c:set var="status" value="${reportMap[key]}" />

                                    <fmt:formatDate var="schDate" value="${sch.learningDate}" pattern="yyyy-MM-dd" />

                                    <td>
                                        <c:choose>

                                            <c:when test="${schDate > currentDate}">
                                                <span class="text-muted small opacity-50">-</span>
                                            </c:when>

                                            <c:when test="${status == 'Present'}">
                                                <i class='bx bxs-check-circle text-success fs-5'></i>
                                            </c:when>

                                            <c:when test="${status == 'Absent'}">
                                                <i class='bx bxs-x-circle text-danger fs-5'></i>
                                                <c:set var="absentCount" value="${absentCount + 1}" />
                                            </c:when>

                                            <c:otherwise>
                                                <span class="text-danger fw-bold" title="Missing Attendance Data">
                                                    <i class='bx bx-minus'></i>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:forEach>

                                <c:set var="total" value="${scheduleList.size()}" />
                                <c:set var="percent" value="${total > 0 ? (absentCount / total) * 100 : 0}" />
                                <td class="fw-bold ${percent >= 20 ? 'bg-danger bg-opacity-10 text-danger' : 'text-success'}">
                                    <fmt:formatNumber value="${percent}" maxFractionDigits="1"/>%
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="card-footer bg-light py-2 small">
            <span class="me-3"><i class='bx bxs-check-circle text-success'></i> Present</span>
            <span class="me-3"><i class='bx bxs-x-circle text-danger'></i> Absent</span>
            <span class="text-muted fst-italic ms-3">Note: Red percentage indicates students who have missed more than 20% of classes.</span>
        </div>
    </div>
</div>