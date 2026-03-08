<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/viewLeadList.css" rel="stylesheet" type="text/css"/>
<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Payment Status</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Payment Status</h2>
                <p class="text-muted small mb-0">Track tuition payment progress by student</p>
            </div>
        </div>
    </div>

    <form id="paymentFilterForm" action="payment" method="GET" class="filter-container flex-wrap">
        <input type="hidden" name="action" value="all">
        <div class="custom-search-bar">
            <i class='bx bx-search text-muted fs-5'></i>
            <input type="text" id="searchQueryInput" name="searchQuery" value="${searchQuery}" placeholder="Search by student name or email...">
        </div>

        <div class="d-flex gap-3">
            <select class="custom-select-filter" id="statusFilterSelect" name="status">
                <option value="all" ${statusFilter == 'all' ? 'selected' : ''}>All Status</option>
                <option value="Pending" ${statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
                <option value="Completed" ${statusFilter == 'Completed' ? 'selected' : ''}>Completed</option>
                <option value="Rejected" ${statusFilter == 'Rejected' ? 'selected' : ''}>Rejected</option>
            </select>
            <input type="date" class="custom-select-filter" name="fromDate" id="fromDateInput" value="${fromDate}" title="From date">
            <input type="date" class="custom-select-filter" name="toDate" id="toDateInput" value="${toDate}" title="To date">
            <button type="submit" class="btn btn-add-new">
                <i class='bx bx-filter-alt'></i> Filter
            </button>
            <a href="payment?action=all" class="btn btn-cancel">Reset</a>
        </div>
    </form>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 6%">#</th>
                        <th style="width: 20%">Student</th>
                        <th style="width: 16%">Amount</th>
                        <th style="width: 16%">Payment Date</th>
                        <th style="width: 14%">Method</th>
                        <th style="width: 12%">Status</th>
                        <th style="width: 16%">Voucher</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty paymentList}">
                            <c:forEach items="${paymentList}" var="p" varStatus="loop">
                                <tr>
                                    <td>${loop.count}</td>
                                    <td>
                                        <div class="fw-semibold">${p[2]}</div>
                                        <div class="text-muted small">${p[3]}</div>
                                    </td>
                                    <td><fmt:formatNumber type="currency" value="${p[4]}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty p[5]}">
                                                ${fn:substring(p[5], 8, 10)}/${fn:substring(p[5], 5, 7)}/${fn:substring(p[5], 0, 4)}
                                                ${fn:substring(p[5], 11, 16)}
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${empty p[6] ? '-' : p[6]}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p[7] == 'Paid'}">
                                                <span class="badge badge-saleStaff">Paid</span>
                                            </c:when>
                                            <c:when test="${p[7] == 'Complete' || p[7] == 'Completed'}">
                                                <span class="badge badge-saleStaff">${p[7]}</span>
                                            </c:when>
                                            <c:when test="${p[7] == 'Pending'}">
                                                <span class="badge badge-admin">Pending</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-teacher">${empty p[7] ? 'Unknown' : p[7]}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${empty p[8] ? '-' : p[8]}</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" class="text-center text-muted py-4">No payment records found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-between align-items-center p-3 border-top">
            <div class="text-muted small">Total ${totalPayments} payment records</div>
        </div>
    </div>
</div>

<script>
    (function () {
        const form = document.getElementById('paymentFilterForm');
        const searchInput = document.getElementById('searchQueryInput');
        const statusSelect = document.getElementById('statusFilterSelect');
        let timer = null;

        if (!form || !searchInput || !statusSelect) {
            return;
        }

        statusSelect.addEventListener('change', function () {
            form.submit();
        });

        searchInput.addEventListener('input', function () {
            if (timer) {
                clearTimeout(timer);
            }
            timer = setTimeout(function () {
                form.submit();
            }, 350);
        });
    })();
</script>
