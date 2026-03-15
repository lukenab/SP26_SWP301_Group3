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
                <li class="breadcrumb-item active" aria-current="page">Sales Reports</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Sales Reports</h2>
                <p class="text-muted small mb-0">Lead conversion and student registration effectiveness</p>
            </div>
        </div>
    </div>

    <form action="lead" method="GET" class="filter-container flex-wrap">
        <input type="hidden" name="action" value="salesReport">
        <div class="d-flex gap-3">
            <input type="date" class="custom-select-filter" name="fromDate" value="${fromDate}" title="From date">
            <input type="date" class="custom-select-filter" name="toDate" value="${toDate}" title="To date">
            <button type="submit" class="btn btn-add-new">
                <i class='bx bx-filter-alt'></i> Apply
            </button>
            <a href="lead?action=salesReport" class="btn btn-cancel">Reset</a>
        </div>
    </form>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Leads</p>
                <h3>${totalLeads}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-reading'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Converted Leads</p>
                <h3>${convertedLeads}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-user-check'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Conversion Rate</p>
                <h3><fmt:formatNumber value="${conversionRate}" minFractionDigits="2" maxFractionDigits="2"/>%</h3>
            </div>
            <div class="icon-wrapper orange">
                <i class='bx bxs-doughnut-chart'></i>
            </div>
        </div>
    </div>

    <div class="chart-section mt-4">
        <div class="chart-card">
            <h4 class="chart-title">Sales Performance</h4>
            <c:choose>
                <c:when test="${not empty monthlyRows}">
                    <div class="chart-container">
                        <canvas id="salesChart"></canvas>
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
                        <th style="width: 18%">Date</th>
                        <th style="width: 22%">Course</th>
                        <th style="width: 15%">Leads Created</th>
                        <th style="width: 15%">Leads Converted</th>
                        <th style="width: 15%">Conversion Rate</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty monthlyRows}">
                            <c:forEach items="${monthlyRows}" var="row">
                                <tr>
                                    <td>${row[0]}</td>
                                    <td>${row[1]}</td>
                                    <td>${row[2]}</td>
                                    <td>${row[3]}</td>
                                    <td><fmt:formatNumber value="${row[5]}" minFractionDigits="2" maxFractionDigits="2"/>%</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="text-center text-muted py-4">No sales report data found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<c:if test="${not empty dailyTotals}">
    <script>
        (function () {
            const labels = [
            <c:forEach items="${dailyTotals}" var="row" varStatus="loop">
                "<c:out value='${row[0]}'/>"
                <c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];

            const leadsCreated = [
            <c:forEach items="${dailyTotals}" var="row" varStatus="loop">
                ${row[1]}<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];

            const leadsConverted = [
            <c:forEach items="${dailyTotals}" var="row" varStatus="loop">
                ${row[2]}<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ];

            const ctx = document.getElementById('salesChart');
            if (!ctx) {
                return;
            }

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels,
                    datasets: [
                        {
                            label: 'Leads Created',
                            data: leadsCreated,
                            backgroundColor: 'rgba(37, 99, 235, 0.6)',
                            borderColor: 'rgba(37, 99, 235, 1)',
                            borderWidth: 1
                        },
                        {
                            label: 'Leads Converted',
                            data: leadsConverted,
                            backgroundColor: 'rgba(14, 116, 144, 0.6)',
                            borderColor: 'rgba(14, 116, 144, 1)',
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
