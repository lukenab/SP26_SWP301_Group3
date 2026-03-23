<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/viewLeadList.css" rel="stylesheet" type="text/css"/>
<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Revenue Report</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Revenue Report</h2>
                <p class="text-muted small mb-0">Revenue from verified tuition payments</p>
            </div>
        </div>
    </div>

    <form action="lead" method="GET" class="filter-container flex-wrap">
        <input type="hidden" name="action" value="revenueReport">
        <div class="d-flex gap-3">
            <input type="date" class="custom-select-filter" name="fromDate" value="${fromDate}" title="From date">
            <input type="date" class="custom-select-filter" name="toDate" value="${toDate}" title="To date">
            <button type="submit" class="btn btn-add-new">
                <i class='bx bx-filter-alt'></i> Apply
            </button>
            <a href="lead?action=revenueReport" class="btn btn-cancel">Reset</a>
        </div>
    </form>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Revenue</p>
                <h3><fmt:formatNumber value="${totalRevenue}" pattern="#,##0" /> VND</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-wallet'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Verified Payments</p>
                <h3>${verifiedPayments}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-badge-check'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Paying Students</p>
                <h3>${payingStudents}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-user-check'></i>
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 25%">Month</th>
                        <th style="width: 35%">Revenue</th>
                        <th style="width: 20%">Payments</th>
                        <th style="width: 20%">Students</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty monthlyRevenueRows}">
                            <c:forEach items="${monthlyRevenueRows}" var="row">
                                <tr>
                                    <td>${row[0]}</td>
                                    <td><fmt:formatNumber value="${row[1]}" pattern="#,##0" /> VND</td>
                                    <td>${row[2]}</td>
                                    <td>${row[3]}</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4" class="text-center text-muted py-4">No revenue data found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>

</div>
