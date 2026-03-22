<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<link href="css/adminDashboard.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#"><i class="bx bx-home-alt"></i></a></li>
            <li class="breadcrumb-item active" aria-current="page">Dashboard</li>
        </ol>
    </div>
    <div class="content-header">
        <div>
            <h2 class="page-title">Dashboard Overview</h2>
            <p class="text-muted small mb-0">Welcome back! Here's what's happening with your business today.</p>
        </div>
    </div>
</div>

<c:set var="totalActive" value="0" />

<c:forEach items="${userList}" var="u">
    <c:if test="${u.status == true}">
        <c:set var="totalActive" value="${totalActive + 1}"/>
    </c:if>
</c:forEach>

<div class="stat-card-grid">

    <div class="stat-card position-relative overflow-hidden">
        <div class="stat-info">            
            <p class="text-muted mb-1 fw-bold">Revenue This Month</p>
            <h3 class="mb-2">
                <fmt:formatNumber value="${currentMonthRevenue}" type="currency" currencySymbol="VND" maxFractionDigits="0"/>
            </h3>
            <c:choose>
                <c:when test="${revenueGrowth >= 0}">
                    <span class="badge bg-success bg-opacity-10 text-success fw-bold p-2" style="font-size: 0.75rem;">
                        <i class='bx bx-trending-up align-middle me-1'></i> 
                        <fmt:formatNumber value="${revenueGrowth}" type="number" maxFractionDigits="1"/>% vs Last Month
                    </span>
                </c:when>
                <c:otherwise>
                    <span class="badge bg-danger bg-opacity-10 text-danger fw-bold p-2" style="font-size: 0.75rem;">
                        <i class='bx bx-trending-down align-middle me-1'></i> 
                        <fmt:formatNumber value="${revenueGrowth * -1}" type="number" maxFractionDigits="1"/>% vs Last Month
                    </span>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="icon-wrapper green position-absolute top-50 end-0 translate-middle-y me-3">
            <i class='bx bx-dollar fs-2'></i>
        </div>
    </div>

    <div class="stat-card position-relative overflow-hidden">
        <div class="stat-info">
            <p class="text-muted mb-1 fw-bold">Active Users</p>
            <h3 class="mb-2">${totalActive}</h3> 
            <c:choose>
                <c:when test="${userGrowth >= 0}">
                    <span class="badge bg-success bg-opacity-10 text-success fw-bold p-2" style="font-size: 0.75rem;">
                        <i class='bx bx-trending-up align-middle me-1'></i>
                        <fmt:formatNumber value="${userGrowth}" type="number" maxFractionDigits="1"/>% vs Last Month
                    </span>
                </c:when>
                <c:otherwise>
                    <span class="badge bg-danger bg-opacity-10 text-danger fw-bold p-2" style="font-size: 0.75rem;">
                        <i class='bx bx-trending-down align-middle me-1'></i>
                        <fmt:formatNumber value="${userGrowth * -1}" type="number" maxFractionDigits="1"/>% vs Last Month
                    </span>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="icon-wrapper green position-absolute top-50 end-0 translate-middle-y me-3">
            <i class='bx bx-group fs-2'></i>
        </div>
    </div>

    <div class="stat-card position-relative overflow-hidden">
        <div class="stat-info">
            <p class="text-muted mb-1 fw-bold">Total Enrollments</p>
            <h3 class="mb-2">${totalEnrollments}</h3>
            <c:choose>
                <c:when test="${enrollmentGrowth >= 0}">
                    <span class="badge bg-success bg-opacity-10 text-success fw-bold p-2" style="font-size: 0.75rem;">
                        <i class='bx bx-trending-up align-middle me-1'></i>
                        <fmt:formatNumber value="${enrollmentGrowth}" type="number" maxFractionDigits="1"/>% vs Last Month
                    </span>
                </c:when>
                <c:otherwise>
                    <span class="badge bg-danger bg-opacity-10 text-danger fw-bold p-2" style="font-size: 0.75rem;">
                        <i class='bx bx-trending-down align-middle me-1'></i>
                        <fmt:formatNumber value="${enrollmentGrowth * -1}" type="number" maxFractionDigits="1"/>% vs Last Month
                    </span>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="icon-wrapper red position-absolute top-50 end-0 translate-middle-y me-3">
            <i class='bx bx-book-bookmark fs-2'></i>
        </div>
    </div>  

    <div class="stat-card position-relative overflow-hidden">         
        <div class="stat-info">
            <p class="text-muted mb-1 fw-bold">Conversions Rate</p>
            <h3 class="mb-2"><fmt:formatNumber value="${conversionRate}" type="number" maxFractionDigits="2"/>%</h3>
            <c:choose>
                <c:when test="${conversionGrowth >= 0}">
                    <span class="badge bg-success bg-opacity-10 text-success fw-bold p-2" style="font-size: 0.75rem;">
                        <i class='bx bx-trending-up align-middle me-1'></i>
                        <fmt:formatNumber value="${conversionGrowth}" type="number" maxFractionDigits="1"/>% vs Last Month
                    </span>
                </c:when>
                <c:otherwise>
                    <span class="badge bg-danger bg-opacity-10 text-danger fw-bold p-2" style="font-size: 0.75rem;">
                        <i class='bx bx-trending-down align-middle me-1'></i>
                        <fmt:formatNumber value="${conversionGrowth * -1}" type="number" maxFractionDigits="1"/>% vs Last Month
                    </span>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="icon-wrapper cyan position-absolute top-50 end-0 translate-middle-y me-3">
            <i class='bx bx-trending-up fs'></i>
        </div>
    </div>  

</div>

<div class="row">
    <div class="col-lg-6 mb-4">
        <div class="card shadow-sm border-0 p-2">
            <div class="card-body">
                <h5 class="card-title fw-bold">Revenue Overview</h5>
                <div class="chart-container" style="position: relative; height:400px; width:100%">
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>
        </div>
    </div>

    <div class="col-lg-6 mb-4">
        <div class="card shadow-sm border-0 p-2">
            <div class="card-body">
                <h5 class="card-title fw-bold">Profit vs Expenses</h5>
                <div class="chart-container" style="position: relative; height:400px; width:100%">
                    <canvas id="profitExpenseChart"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-6 mb-4">
        <div class="card shadow-sm border-0 h-100">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h5 class="d-flex align-items-center card-title fw-bold mb-0 text-dark">
                    <i class='bx bx-clock-dashed-half text-warning fs-2 me-2'></i>Pending Payments
                </h5>
                <a href="payment?action=list" class="btn btn-sm btn-outline-primary">View All</a>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr style="font-size: 0.85rem;">
                                <th class="ps-4">Student</th>
                                <th>Amount</th>
                                <th>Date</th>
                                <th style="width: 20%">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${pendingPayments}" var="p">
                                <tr>
                                    <td class="ps-4">
                                        <div class="fw-bold text-dark">${p.name}</div>
                                        <small class="text-muted">ID: #${p.id}</small>
                                    </td>
                                    <td class="fw-bold text-success">
                                        <fmt:formatNumber value="${p.amount}" type="currency" currencySymbol=""/> VND
                                    </td>
                                    <td class="text-muted small">
                                        <fmt:formatDate value="${p.date}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <a href="payment?action=approve&id=${p.id}" class="btn btn-success">Approve <i class="bx bx-check"></i></a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty pendingPayments}">
                                <tr><td colspan="4" class="text-center py-4 text-muted">No pending payments.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>


    <div class="col-lg-6 mb-4">
        <div class="card shadow-sm border-0 h-100">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h5 class="d-flex align-items-center card-title fw-bold mb-0 text-dark">
                    <i class='bx bx-history text-primary fs-2 me-2'></i>Recent Activities
                </h5>
                <a href="dashboard?action=report&tab=logs" class="btn btn-sm btn-outline-primary">View All</a>
            </div>
            <div class="card-body px-4 mt-3">
                <div class="activity-timeline">
                    <c:forEach items="${recentActivities}" var="log" varStatus="status">
                        <div class="d-flex mb-4 position-relative">
                            <c:if test="${!status.last}">
                                <div class="position-absolute" style="left: 15px; top: 30px; bottom: -20px; width: 2px; background: #e2e8f0;"></div>
                            </c:if>

                            <div class="flex-shrink-0 z-1">
                                <c:choose>
                                    <c:when test="${fn:contains(log.actionType, 'LOGIN')}">
                                        <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center" style="width: 32px; height: 32px;">
                                            <i class='bx bx-user' style="font-size: 18px;"></i>
                                        </div>
                                    </c:when>
                                    <c:when test="${fn:contains(log.actionType, 'CREATE') || fn:contains(log.actionType, 'APPROVE')}">
                                        <div class="rounded-circle bg-success text-white d-flex align-items-center justify-content-center" style="width: 32px; height: 32px;">
                                            <i class='bx bx-plus-circle' style="font-size: 18px;"></i>
                                        </div>
                                    </c:when>
                                    <c:when test="${fn:contains(log.actionType, 'UPDATE')}">
                                        <div class="rounded-circle bg-warning text-white d-flex align-items-center justify-content-center" style="width: 32px; height: 32px;">
                                            <i class='bx bx-edit-alt' style="font-size: 18px;"></i>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="rounded-circle bg-secondary text-white d-flex align-items-center justify-content-center" style="width: 32px; height: 32px;">
                                            <i class='bx bx-info-circle' style="font-size: 18px;"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="flex-grow-1 ms-3">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div>
                                        <span class="fw-bold text-dark">${log.actorName}</span> 
                                        <span class="text-muted">performed</span> 
                                        <span class="badge bg-light text-dark border">${log.actionType}</span>
                                    </div>
                                    <small class="text-muted">
                                        <i class='bx bx-time-five me-1'></i>
                                        <fmt:formatDate value="${log.logDate}" pattern="HH:mm, dd/MM"/>
                                    </small>
                                </div>
                                <p class="mb-0 text-muted small mt-1">${log.description}</p>
                            </div>
                        </div>
                    </c:forEach>

                    <c:if test="${empty recentActivities}">
                        <div class="text-center py-4 text-muted">
                            <p>No recent activities recorded.</p>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    document.addEventListener("DOMContentLoaded", function () {
        const ctxRevenue = document.getElementById('revenueChart').getContext('2d');
        const revenueChart = new Chart(ctxRevenue, {
            type: 'line',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                datasets: [{
                        label: 'Revenue',
                        data: [${revenueData}],
                        borderColor: '#3b82f6',
                        backgroundColor: 'rgba(59, 130, 246, 0.15)',
                        borderWidth: 2,
                        pointRadius: 0,
                        pointHoverRadius: 6,
                        tension: 0.4,
                        fill: true
                    }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {display: false}
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            color: '#94a3b8',
                            callback: function (value, index, values) {
                                if (value >= 1000000) {
                                    return (value / 1000000) + 'M';
                                }
                                return value;
                            }
                        },
                        grid: {
                            color: '#f1f5f9',
                            borderDash: [5, 5]
                        },
                        border: {display: false}
                    },
                    x: {
                        ticks: {color: '#94a3b8'},
                        grid: {display: false},
                        border: {color: '#e2e8f0'}
                    }
                }
            }
        });

        // 2. PROFIT VS EXPENSES (Bar Chart)
        const ctxProfit = document.getElementById('profitExpenseChart').getContext('2d');
        const profitExpenseChart = new Chart(ctxProfit, {
            type: 'bar',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                datasets: [
                    {
                        label: 'Expenses',
                        data: [1500, 1500, 700, 1100, 1200, 1500, 1700, 1900, 2100, 2300, 2500, 2600],
                        backgroundColor: '#64748b',
                        borderRadius: 4,
                        barPercentage: 0.6,
                        categoryPercentage: 0.8
                    },
                    {
                        label: 'Profit',
                        data: [2300, 1300, 10000, 3800, 4700, 3600, 4200, 5100, 4000, 5400, 6100, 7000],
                        backgroundColor: '#3b82f6',
                        borderRadius: 4,
                        barPercentage: 0.6,
                        categoryPercentage: 0.8
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'bottom',
                        labels: {
                            usePointStyle: true,
                            boxWidth: 8
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 10000,
                        ticks: {
                            stepSize: 2500,
                            color: '#94a3b8'
                        },
                        grid: {
                            color: '#f1f5f9',
                            borderDash: [5, 5]
                        },
                        border: {display: false}
                    },
                    x: {
                        ticks: {color: '#94a3b8'},
                        grid: {display: false},
                        border: {color: '#e2e8f0'}
                    }
                }
            }
        });
    });
</script>


