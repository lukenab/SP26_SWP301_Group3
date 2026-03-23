<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<link href="css/adminDashboard.css" rel="stylesheet" type="text/css"/>
<link href="css/viewLeadList.css" rel="stylesheet" type="text/css"/>
<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#"><i class="bx bx-home-alt"></i></a></li>
                <li class="breadcrumb-item active" aria-current="page">Voucher Report</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Voucher Report</h2>
                <p class="text-muted small mb-0">Voucher inventory, status, and remaining capacity</p>
            </div>
        </div>
    </div>

    <form action="voucher" method="GET" class="filter-container flex-wrap">
        <input type="hidden" name="action" value="report">
        <div class="d-flex gap-3">
            <input type="date" class="custom-select-filter" name="fromDate" value="${fromDate}" title="From date">
            <input type="date" class="custom-select-filter" name="toDate" value="${toDate}" title="To date">
            <button type="submit" class="btn btn-add-new">
                <i class='bx bx-filter-alt'></i> Apply
            </button>
            <a href="voucher?action=report" class="btn btn-cancel">Reset</a>
        </div>
    </form>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Vouchers</p>
                <h3>${totalVouchers}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-purchase-tag'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Active Vouchers</p>
                <h3>${activeVouchers}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-badge-check'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Issued</p>
                <h3>${totalIssued}</h3>
            </div>
            <div class="icon-wrapper orange">
                <i class='bx bxs-coupon'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Remaining</p>
                <h3>${totalRemaining}</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-package'></i>
            </div>
        </div>
    </div>

    <div class="chart-section mt-4">
        <div class="chart-card">
            <h4 class="chart-title">Voucher Capacity Overview</h4>
            <c:choose>
                <c:when test="${not empty monthlyRows}">
                    <div class="chart-container">
                        <canvas id="voucherReportChart"></canvas>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-muted small">No chart data available.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 18%">Code</th>
                        <th style="width: 16%">Discount</th>
                        <th style="width: 12%">Issued</th>
                        <th style="width: 12%">Used</th>
                        <th style="width: 12%">Remaining</th>
                        <th style="width: 15%">Valid Until</th>
                        <th style="width: 15%">Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty voucherRows}">
                            <c:forEach items="${voucherRows}" var="row">
                                <tr>
                                    <td>${row[0]}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${row[1] != null && row[1] > 0}">
                                                <fmt:formatNumber value="${row[1]}" pattern="#,##0" /> VND
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:formatNumber value="${row[2]}" minFractionDigits="0" maxFractionDigits="2" />%
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${row[3]}</td>
                                    <td>${row[4]}</td>
                                    <td>${row[5]}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${row[6] != null}">
                                                <fmt:formatDate value="${row[6]}" pattern="dd/MM/yyyy" />
                                            </c:when>
                                            <c:otherwise>No expiry</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="badge ${row[7] ? 'badge-saleStaff' : 'badge-inactive'}">
                                            ${row[7] ? 'Active' : 'Inactive'}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" class="text-center text-muted py-4">No voucher report data found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<c:if test="${not empty monthlyRows}">
    <script>
        (function () {
            const labels = [
            <c:forEach items="${monthlyRows}" var="row" varStatus="loop">
                "<c:out value='${row[0]}'/>"
                <c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];

            const issuedCounts = [
            <c:forEach items="${monthlyRows}" var="row" varStatus="loop">
                ${row[1]}<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];

            const usedCounts = [
            <c:forEach items="${monthlyRows}" var="row" varStatus="loop">
                ${row[2]}<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];

            const remainingCounts = [
            <c:forEach items="${monthlyRows}" var="row" varStatus="loop">
                ${row[3]}<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];

            const ctx = document.getElementById('voucherReportChart');
            if (!ctx) {
                return;
            }

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels,
                    datasets: [
                        {
                            label: 'Issued',
                            data: issuedCounts,
                            backgroundColor: 'rgba(37, 99, 235, 0.6)',
                            borderColor: 'rgba(37, 99, 235, 1)',
                            borderWidth: 1
                        },
                        {
                            label: 'Used',
                            data: usedCounts,
                            backgroundColor: 'rgba(249, 115, 22, 0.6)',
                            borderColor: 'rgba(249, 115, 22, 1)',
                            borderWidth: 1
                        },
                        {
                            label: 'Remaining',
                            data: remainingCounts,
                            backgroundColor: 'rgba(132, 204, 22, 0.6)',
                            borderColor: 'rgba(132, 204, 22, 1)',
                            borderWidth: 1
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        x: { ticks: { autoSkip: false } },
                        y: {
                            beginAtZero: true,
                            ticks: {
                                stepSize: 1,
                                precision: 0
                            }
                        }
                    }
                }
            });
        })();
    </script>
</c:if>
