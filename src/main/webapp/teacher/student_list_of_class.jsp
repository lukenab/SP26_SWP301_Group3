<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<c:if test="${not empty sessionScope.message}">
    <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
        <i class='bx ${sessionScope.messageType == 'success' ? 'bx-check-circle' : 'bx-error-circle'} me-2'></i>
        <span>${sessionScope.message}</span>
    </div>
    <c:remove var="message" scope="session"/>
    <c:remove var="messageType" scope="session"/>
</c:if>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item">
                    <a href="class">Class Management</a>
                </li>
                <li class="breadcrumb-item active">Student List</li>
            </ol>
        </div>

        <div class="content-header">
            <div>
                <h2 class="page-title">Student List</h2>
                <p class="text-muted small mb-0">
                    Manage and organize student grades for this class
                </p>
            </div>

            <a href="class" class="btn btn-add-new">
                <i class='bx bx-left-arrow-alt'></i> Back
            </a>
        </div>
    </div>
    <div class="card user-table-card border-0 bg-white">
    </div> <div class="row mb-3">
        <div class="col-md-5">
            <div class="input-group shadow-sm border rounded">
                <span class="input-group-text bg-white border-0">
                    <i class='bx bx-search text-muted'></i>
                </span>
                <input type="text" id="studentSearch" class="form-control border-0 ps-0" 
                       placeholder="Search by student name or email...">
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 shadow-sm bg-white rounded-3">
        <div class="table-responsive">
            <table class="table mb-0 align-middle table-hover">
                <thead class="bg-light text-muted small text-uppercase">
                    <tr>
                        <th class="text-center" style="width:60px">#</th>
                        <th>Student Info</th>
                        <th class="text-center">Phone</th>
                        <th class="text-center">Final Score</th>
                        <th class="text-center" style="width:180px">Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="s" items="${studentList}" varStatus="loop">
                        <c:set var="avg" value="${averageMap[s.userId]}" />
                        <tr class="student-row">
                            <td class="text-center text-muted small">${loop.count}</td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <div class="avatar-circle me-3 bg-soft-primary text-primary fw-bold">
                                        ${s.fullName.substring(0,1).toUpperCase()}
                                    </div>
                                    <div>
                                        <div class="fw-bold text-dark student-name">${s.fullName}</div>
                                        <div class="small text-muted student-email">${s.email}</div>
                                    </div>
                                </div>
                            </td>
                            <td class="text-center text-muted small">${s.phone}</td>

                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${avg != null}">
                                        <span class="badge rounded-pill px-3 ${avg < 5 ? 'bg-danger' : 'bg-success'} shadow-sm">
                                            <fmt:formatNumber value="${avg}" maxFractionDigits="1"/>
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted fst-italic small">Not graded</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td class="text-center">
                                <div class="d-flex justify-content-center align-items-center gap-1">
                                    <a href="#" class="btn-icon-minimal" title="View Detail"><i class='bx bx-eye'></i></a>

                                    <c:choose>
                                        <c:when test="${avg == null}">
                                            <a href="grade?action=enter&studentId=${s.userId}&classId=${classId}" 
                                               class="btn-icon-minimal" title="Enter Grade"><i class='bx bx-plus-circle'></i></a>
                                            </c:when>
                                            <c:otherwise>
                                            <a href="grade?action=edit&studentId=${s.userId}&classId=${classId}" 
                                               class="btn-icon-minimal" title="Edit Grade"><i class='bx bx-edit-alt'></i></a>

                                            <form action="grade" method="post" class="d-inline">
                                                <input type="hidden" name="action" value="delete"/>
                                                <input type="hidden" name="studentId" value="${s.userId}"/>
                                                <input type="hidden" name="classId" value="${classId}"/>
                                                <%-- ICON DELETE (ĐÃ THAY THÀNH bx-trash) --%>
                                                <button type="submit" class="btn-icon-minimal" 
                                                        onclick="return confirm('Delete grades for ${s.fullName}?')">
                                                    <i class='bx bx-trash'></i>
                                                </button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                    <a href="#" class="btn-icon-minimal" title="Sync"><i class='bx bx-refresh'></i></a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</div>

<style>
    /* ĐỊNH DẠNG DẤU NGĂN CÁCH BREADCRUMB (GIỐNG TRANG ENTER GRADE) */
    .breadcrumb-item + .breadcrumb-item::before {
        content: ">" !important;
        font-size: 0.8rem;
        color: #adb5bd;
        padding-left: 0.5rem;
        padding-right: 0.5rem;
    }

    /* STYLE ICON TỐI GIẢN - KHÔNG MÀU MÈ */
    .btn-icon-minimal {
        color: #a0a0a0 !important;
        font-size: 1.2rem;
        transition: all 0.2s ease;
        text-decoration: none;
        background: none !important;
        border: none !important;
        padding: 0 6px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
    }

    .btn-icon-minimal:hover {
        color: #6c757d !important;
        transform: scale(1.1);
    }

    .avatar-circle {
        width: 38px;
        height: 38px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
        border: 1px solid #dee2e6;
        background-color: #f8f9fc;
    }
    .bg-soft-primary {
        background-color: #eef2f7;
    }
</style>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const toast = document.getElementById("toastMessage");
        if (toast) {
            setTimeout(() => {
                toast.style.opacity = "0";
                setTimeout(() => toast.remove(), 400);
            }, 3000);
        }

        const searchInput = document.getElementById("studentSearch");
        if (searchInput) {
            searchInput.addEventListener("keyup", function () {
                const query = this.value.toLowerCase().trim();
                const rows = document.querySelectorAll(".student-row");

                rows.forEach(row => {
                    const name = row.querySelector(".student-name").innerText.toLowerCase();
                    const email = row.querySelector(".student-email").innerText.toLowerCase();
                    if (name.includes(query) || email.includes(query)) {
                        row.style.display = "";
                    } else {
                        row.style.display = "none";
                    }
                });
            });
        }
    });
</script>