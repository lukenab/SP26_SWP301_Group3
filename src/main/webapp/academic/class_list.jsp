<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US"/>

<link href="css/course_list.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Class Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Class Management</h2>
                <p class="text-muted small mb-0">Manage classes and assign students.</p>
            </div>
            <a href="enrollment?action=createClassForm" class="btn btn-add-new">
                <i class='bx bx-user-plus'></i> Create New Class
            </a>
        </div>
    </div>

    <c:set var="activeClass" value="0"/>
    <c:set var="pendingClass" value="0"/>
    <c:set var="inactiveClass" value="0"/>

    <c:forEach items="${filteredClassList}" var="c">
        <c:if test="${c[6] == 'Active'}">
            <c:set var="activeClass" value="${activeClass + 1}"/>
        </c:if>
        <c:if test="${c[6] == 'Pending'}">
            <c:set var="pendingClass" value="${pendingClass + 1}"/>
        </c:if>
        <c:if test="${c[6] == 'Inactive'}">
            <c:set var="inactiveClass" value="${inactiveClass + 1}"/>
        </c:if>
    </c:forEach>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Classes</p>
                <h3>${totalItems}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-school'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Active Class</p>
                <h3>${activeClass}</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-check-shield'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Pending Class</p>
                <h3>${pendingClass}</h3>
            </div>
            <div class="icon-wrapper orange">
                <i class='bx bxs-time-five'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Inactive Class</p>
                <h3>${inactiveClass}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-info-shield'></i>
            </div>
        </div>
    </div>

    <c:if test="${not empty sessionScope.message}">
        <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
            <div class="toast-icon">
                <c:choose>
                    <c:when test="${sessionScope.messageType == 'success'}">
                        <i class='bx bx-check-circle'></i>
                    </c:when>
                    <c:otherwise>
                        <i class='bx bx-error-circle'></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="toast-content">
                <span class="toast-title">
                    ${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}
                </span>
                <span class="toast-message">${sessionScope.message}</span>
            </div>
            <button class="toast-close" onclick="closeToast()">
                <i class='bx bx-x'></i>
            </button>
        </div>

        <c:remove var="message" scope="session" />
        <c:remove var="messageType" scope="session" />
    </c:if>

    <div class="filter-container flex-wrap">
        <form action="enrollment" method="get" class="custom-search-bar" id="classSearchForm">
            <input type="hidden" name="action" value="classes">
            <input type="hidden" name="month" value="${empty monthFilter ? 'all' : monthFilter}">
            <input type="hidden" name="status" value="${empty statusFilter ? 'all' : statusFilter}">
            <i class='bx bx-search text-muted fs-5'></i>
            <input type="text"
                   id="classSearchInput"
                   name="searchQuery"
                   value="${fn:escapeXml(searchQuery)}"
                   placeholder="Search by class name..."
                   aria-label="Search classes">
        </form>

        <div class="d-flex gap-3">
            <div class="dropdown">
                <button class="custom-select-filter" type="button" data-bs-toggle="dropdown">
                    <i class='bx bx-filter-alt'></i>
                    <c:choose>
                        <c:when test="${monthFilter == '1'}">January</c:when>
                        <c:when test="${monthFilter == '2'}">February</c:when>
                        <c:when test="${monthFilter == '3'}">March</c:when>
                        <c:when test="${monthFilter == '4'}">April</c:when>
                        <c:when test="${monthFilter == '5'}">May</c:when>
                        <c:when test="${monthFilter == '6'}">June</c:when>
                        <c:when test="${monthFilter == '7'}">July</c:when>
                        <c:when test="${monthFilter == '8'}">August</c:when>
                        <c:when test="${monthFilter == '9'}">September</c:when>
                        <c:when test="${monthFilter == '10'}">October</c:when>
                        <c:when test="${monthFilter == '11'}">November</c:when>
                        <c:when test="${monthFilter == '12'}">December</c:when>
                        <c:otherwise>All Classes</c:otherwise>
                    </c:choose>
                    <i class='bx bx-chevron-down ms-1'></i>
                </button>
                <ul class="dropdown-menu">
                    <c:url var="allMonthUrl" value="enrollment">
                        <c:param name="action" value="classes"/>
                        <c:param name="searchQuery" value="${searchQuery}"/>
                        <c:param name="status" value="${empty statusFilter ? 'all' : statusFilter}"/>
                        <c:param name="month" value="all"/>
                    </c:url>
                    <li><a class="dropdown-item" href="${allMonthUrl}">All Classes</a></li>
                    <c:forEach begin="1" end="12" var="month">
                        <c:url var="monthUrl" value="enrollment">
                            <c:param name="action" value="classes"/>
                            <c:param name="searchQuery" value="${searchQuery}"/>
                            <c:param name="status" value="${empty statusFilter ? 'all' : statusFilter}"/>
                            <c:param name="month" value="${month}"/>
                        </c:url>
                        <li>
                            <a class="dropdown-item" href="${monthUrl}">
                                <c:choose>
                                    <c:when test="${month == 1}">January</c:when>
                                    <c:when test="${month == 2}">February</c:when>
                                    <c:when test="${month == 3}">March</c:when>
                                    <c:when test="${month == 4}">April</c:when>
                                    <c:when test="${month == 5}">May</c:when>
                                    <c:when test="${month == 6}">June</c:when>
                                    <c:when test="${month == 7}">July</c:when>
                                    <c:when test="${month == 8}">August</c:when>
                                    <c:when test="${month == 9}">September</c:when>
                                    <c:when test="${month == 10}">October</c:when>
                                    <c:when test="${month == 11}">November</c:when>
                                    <c:otherwise>December</c:otherwise>
                                </c:choose>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>

            <div class="dropdown">
                <button class="custom-select-filter d-flex align-items-center gap-2" type="button" data-bs-toggle="dropdown">
                    <i class='bx bx-slider-alt'></i>
                    <c:choose>
                        <c:when test="${statusFilter == 'Active'}">Active</c:when>
                        <c:when test="${statusFilter == 'Pending'}">Pending</c:when>
                        <c:when test="${statusFilter == 'Inactive'}">Inactive</c:when>
                        <c:otherwise>All Status</c:otherwise>
                    </c:choose>
                    <i class='bx bx-chevron-down ms-1'></i>
                </button>
                <ul class="dropdown-menu">
                    <c:url var="allClassStatusUrl" value="enrollment">
                        <c:param name="action" value="classes"/>
                        <c:param name="searchQuery" value="${searchQuery}"/>
                        <c:param name="month" value="${empty monthFilter ? 'all' : monthFilter}"/>
                        <c:param name="status" value="all"/>
                    </c:url>
                    <c:url var="activeClassStatusUrl" value="enrollment">
                        <c:param name="action" value="classes"/>
                        <c:param name="searchQuery" value="${searchQuery}"/>
                        <c:param name="month" value="${empty monthFilter ? 'all' : monthFilter}"/>
                        <c:param name="status" value="Active"/>
                    </c:url>
                    <c:url var="pendingClassStatusUrl" value="enrollment">
                        <c:param name="action" value="classes"/>
                        <c:param name="searchQuery" value="${searchQuery}"/>
                        <c:param name="month" value="${empty monthFilter ? 'all' : monthFilter}"/>
                        <c:param name="status" value="Pending"/>
                    </c:url>
                    <c:url var="inactiveClassStatusUrl" value="enrollment">
                        <c:param name="action" value="classes"/>
                        <c:param name="searchQuery" value="${searchQuery}"/>
                        <c:param name="month" value="${empty monthFilter ? 'all' : monthFilter}"/>
                        <c:param name="status" value="Inactive"/>
                    </c:url>
                    <li><a class="dropdown-item" href="${allClassStatusUrl}">All Status</a></li>
                    <li><a class="dropdown-item" href="${activeClassStatusUrl}">Active</a></li>
                    <li><a class="dropdown-item" href="${pendingClassStatusUrl}">Pending</a></li>
                    <li><a class="dropdown-item" href="${inactiveClassStatusUrl}">Inactive</a></li>
                </ul>
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 5%">#</th>
                        <th style="width: 24%">Class Info</th>
                        <th style="width: 22%">Course</th>
                        <th style="width: 16%">Teacher</th>
                        <th style="width: 12%">Quantity</th>
                        <th style="width: 11%">Status</th>
                        <th style="width: 10%">Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:if test="${empty classList}">
                        <tr>
                            <td colspan="7" class="text-center text-muted py-4">No classes found.</td>
                        </tr>
                    </c:if>

                    <c:forEach items="${classList}" var="c" varStatus="loop">
                        <tr>
                            <c:set var="isActive" value="${c[6] == 'Active'}"/>
                            <td>${startItem + loop.index}</td>

                            <td>
                                <div class="user-item">
                                    <div class="user-avatar-placeholder" style="background:#3b82f6;">
                                        ${fn:substring(c[1], 0, 1)}
                                    </div>
                                    <div class="d-flex flex-column">
                                        <span class="user-name">${c[1]}</span>
                                    </div>
                                </div>
                            </td>

                            <td>${c[2]}</td>
                            <td>${empty c[3] ? 'N/A' : c[3]}</td>
                            <td>
                                <span class="text-secondary">${c[7]}/${c[8]}</span>
                            </td>

                            <td>
                                <span class="class-status-badge ${c[6] == 'Active' ? 'active' : (c[6] == 'Pending' ? 'pending' : 'inactive')}">
                                    ${c[6]}
                                </span>
                            </td>

                            <td class="actions-cell">
                                <div class="table-actions">
                                    <a href="enrollment?action=classDetails&classId=${c[0]}"
                                       class="action-btn"
                                       title="View Details">
                                        <i class='bx bx-eye'></i>
                                    </a>
                                    <a href="enrollment?action=editClassForm&classId=${c[0]}" class="action-btn" title="Edit Class">
                                        <i class='bx bx-edit'></i>
                                    </a>
                                    <a href="enrollment?action=addStudentForm&classId=${c[0]}" class="action-btn" title="Add Student">
                                        <i class='bx bx-user-plus'></i>
                                    </a>
                                    <c:choose>
                                        <c:when test="${isActive}">
                                            <a href="enrollment?action=deleteClass&classId=${c[0]}" class="action-btn delete" title="Set Inactive">
                                                <i class='bx bx-lock'></i>
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="enrollment?action=deleteClass&classId=${c[0]}" class="action-btn" title="Set Active">
                                                <i class='bx bx-lock-open'></i>
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-between align-items-center p-3 border-top">
            <div class="text-muted small">Showing ${startItem}-${endItem} of ${totalItems} classes</div>
            <c:if test="${not showAllFilteredResults}">
                <div>
                <ul class="pagination pagination-sm mb-0">
                    <c:url var="prevPageUrl" value="enrollment">
                        <c:param name="action" value="classes"/>
                        <c:param name="searchQuery" value="${searchQuery}"/>
                        <c:param name="month" value="${empty monthFilter ? 'all' : monthFilter}"/>
                        <c:param name="status" value="${empty statusFilter ? 'all' : statusFilter}"/>
                        <c:param name="page" value="${currentPage - 1}"/>
                    </c:url>
                    <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${prevPageUrl}">
                            <i class='bx bx-chevron-left'></i> Previous
                        </a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                        <c:url var="pageUrl" value="enrollment">
                            <c:param name="action" value="classes"/>
                            <c:param name="searchQuery" value="${searchQuery}"/>
                            <c:param name="month" value="${empty monthFilter ? 'all' : monthFilter}"/>
                            <c:param name="status" value="${empty statusFilter ? 'all' : statusFilter}"/>
                            <c:param name="page" value="${pageNumber}"/>
                        </c:url>
                        <li class="page-item ${pageNumber == currentPage ? 'active' : ''}">
                            <a class="page-link" href="${pageUrl}">${pageNumber}</a>
                        </li>
                    </c:forEach>
                    <c:url var="nextPageUrl" value="enrollment">
                        <c:param name="action" value="classes"/>
                        <c:param name="searchQuery" value="${searchQuery}"/>
                        <c:param name="month" value="${empty monthFilter ? 'all' : monthFilter}"/>
                        <c:param name="status" value="${empty statusFilter ? 'all' : statusFilter}"/>
                        <c:param name="page" value="${currentPage + 1}"/>
                    </c:url>
                    <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="${nextPageUrl}">
                            Next <i class='bx bx-chevron-right'></i>
                        </a>
                    </li>
                </ul>
                </div>
            </c:if>
        </div>
    </div>
</div>
<script src="js/manageUser.js" type="text/javascript"></script>
<script>
    (function () {
        const searchInput = document.getElementById('classSearchInput');
        const searchForm = document.getElementById('classSearchForm');
        let debounceTimer;

        if (!searchInput || !searchForm) {
            return;
        }

        searchInput.addEventListener('input', function () {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(function () {
                searchForm.submit();
            }, 400);
        });
    })();
</script>
