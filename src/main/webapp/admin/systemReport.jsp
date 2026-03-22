<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="d-flex align-items-center mb-4">
    <h3 class="fw-bold mb-0">System Analysis & Reports</h3>
</div>

<ul class="nav nav-pills mb-3 bg-white p-2 rounded shadow-sm" id="reportTabs" role="tablist">
    <li class="nav-item" role="presentation">
        <button class="nav-link active d-flex align-items-center" id="logs-tab" data-bs-toggle="pill" data-bs-target="#logs-content" type="button" role="tab">
            <i class='bx bx-history me-2'></i> System Logs
        </button>
    </li>
    <li class="nav-item" role="presentation">
        <button class="nav-link d-flex align-items-center" id="usage-tab" data-bs-toggle="pill" data-bs-target="#usage-content" type="button" role="tab">
            <i class='bx bx-pie-chart-alt-2 me-2'></i> System Usage
        </button>
    </li>
    <li class="nav-item" role="presentation">
        <button class="nav-link d-flex align-items-center" id="growth-tab" data-bs-toggle="pill" data-bs-target="#growth-content" type="button" role="tab">
            <i class='bx bx-trending-up me-2'></i> Growth Report
        </button>
    </li>
</ul>

<div class="tab-content" id="reportTabsContent">

    <div class="tab-pane fade show active" id="logs-content" role="tabpanel">
        <div class="card shadow-sm border-0 mt-4">
            <div class="card-header bg-white d-flex justify-content-between align-items-center py-3">
                <h5 class="card-title fw-bold mb-0 d-flex align-items-center">
                    <i class='bx bx-list-ul text-primary me-2'></i> System Audit Logs
                </h5>
                <form action="report" method="GET" class="d-flex gap-2">
                    <input type="hidden" name="action" value="report">
                    <input type="hidden" name="tab" value="logs">
                    
                    <select name="filterAction" class="form-select form-select-sm" onchange="this.form.submit()">
                        <option value="ALL" ${currentFilter == 'ALL' ? 'selected' : ''}>All Actions</option>
                        <option value="LOGIN" ${currentFilter == 'LOGIN' ? 'selected' : ''}>Logins</option>
                        <option value="CREATE_USER" ${currentFilter == 'CREATE_USER' ? 'selected' : ''}>Create User</option>
                        <option value="UPDATE_USER" ${currentFilter == 'UPDATE_USER' ? 'selected' : ''}>Updates</option>
                        <option value="LOCK_USER" ${currentFilter == 'LOCK_USER' ? 'selected' : ''}>Lock User</option>
                        <option value="ERROR" ${currentFilter == 'ERROR' ? 'selected' : ''}>Errors</option>
                    </select>
                </form>
            </div>

            <div class="card-body p-0">
                <div class="table-responsive" style="max-height: 600px; overflow-y: auto;">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light position-sticky top-0" style="z-index: 1;">
                            <tr>
                                <th style="width: 15%">Time</th>
                                <th style="width: 20%">Actor</th>
                                <th style="width: 15%">Role</th>
                                <th style="width: 15%">Action Type</th> 
                                <th style="width: 35%">Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${systemLogs}" var="log">
                                <tr>
                                    <td class="text-muted small">
                                        <fmt:formatDate value="${log.logDate}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                    </td>
                                    <td class="fw-bold">${log.actorName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fn:contains(log.actorRole, 'Admin')}">
                                                <span class="badge badge-role badge-admin">${log.actorRole}</span>
                                            </c:when>
                                            <c:when test="${log.actorRole == 'Teacher'}">
                                                <span class="badge badge-role badge-teacher">${log.actorRole}</span>
                                            </c:when>
                                            <c:when test="${log.actorRole == 'Student'}">
                                                <span class="badge badge-role badge-student">${log.actorRole}</span>
                                            </c:when>
                                            <c:when test="${log.actorRole == 'Sale Staff'}">
                                                <span class="badge badge-role badge-saleStaff">${log.actorRole}</span>
                                            </c:when>
                                            <c:when test="${log.actorRole == 'Academic Staff'}">
                                                <span class="badge badge-role badge-academicStaff">${log.actorRole}</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fn:contains(log.actionType, 'CREATE')}">
                                                <span class="badge bg-success p-2">${log.actionType}</span>
                                            </c:when>
                                            <c:when test="${fn:contains(log.actionType, 'UPDATE')}">
                                                <span class="badge bg-warning text-dark p-2">${log.actionType}</span>
                                            </c:when>
                                            <c:when test="${fn:contains(log.actionType, 'LOCK') || fn:contains(log.actionType, 'DELETE')}">
                                                <span class="badge bg-danger p-2">${log.actionType}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-primary p-2">${log.actionType}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${log.description}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty systemLogs}">
                                <tr>
                                    <td colspan="5" class="text-center text-muted py-4">No system logs found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="tab-pane fade" id="usage-content" role="tabpanel">
        <div class="card border-0 shadow-sm p-4 mt-4">
            <h5 class="fw-bold mb-4">User Demographics Statistics</h5>
            <div class="row align-items-center">
                <div class="col-md-8">
                    <div style="height: 450px; position: relative;">
                        <canvas id="usageChart"></canvas> 
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="p-3 bg-light rounded shadow-sm border">
                        <h6 class="fw-bold text-secondary mb-3 text-uppercase" style="font-size: 0.85rem; letter-spacing: 0.5px;">Detail Breakdown</h6>
                        <table class="table table-borderless mb-0">
                            <tbody>
                                <c:forEach items="${usageStats}" var="entry">
                                    <tr>
                                        <td class="py-2 px-0">
                                            <i class='bx bxs-user-circle me-2 text-primary fs-5 align-middle'></i>
                                            <span class="align-middle fw-medium">${entry.key}</span>
                                        </td>
                                        <td class="text-end fw-bold py-2 px-0 text-dark fs-5 align-middle">${entry.value}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="tab-pane fade" id="growth-content" role="tabpanel">
        <div class="card border-0 shadow-sm p-4 mt-4">
            <h5 class="fw-bold mb-4">Enrollment Growth Tracking</h5>
            <div style="height: 450px;">
                <canvas id="growthChart"></canvas>
            </div>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const urlParams = new URLSearchParams(window.location.search);
        const activeTab = urlParams.get('tab');

        if (activeTab) {
            var someTabTriggerEl = document.querySelector('#' + activeTab + '-tab');
            if (someTabTriggerEl) {
                var tab = new bootstrap.Tab(someTabTriggerEl);
                tab.show();
            }
        }

        var triggerTabList = [].slice.call(document.querySelectorAll('#reportTabs button'));
        triggerTabList.forEach(function (triggerEl) {
            triggerEl.addEventListener('shown.bs.tab', function (event) {
                const tabTarget = event.target.getAttribute('data-bs-target').replace('-content', '').replace('#', '');
                const newUrl = window.location.pathname + '?action=report&tab=' + tabTarget;
                window.history.replaceState({}, '', newUrl);
            });
        });

        const ctxUsage = document.getElementById('usageChart').getContext('2d');
        new Chart(ctxUsage, {
            type: 'doughnut',
            data: {
                labels: [${chartLabels}],
                datasets: [{
                    data: [${chartData}],
                    backgroundColor: ['#7E22CE', '#1E40AF', '#047857', '#9A3412', '#9D174D'],
                    borderWidth: 2,
                    borderColor: '#ffffff',
                    hoverOffset: 10
                }]
            },
            options: { 
                responsive: true,
                maintainAspectRatio: false,
                cutout: '55%', 
                layout: {
                    padding: 20 
                },
                plugins: {
                    legend: {
                        display: false 
                    },
                    tooltip: {
                        bodyFont: { size: 14 },
                        padding: 10,
                        boxPadding: 5
                    }
                }
            }
        });

        // --- GROWTH REPORT ---
        const ctxGrowth = document.getElementById('growthChart').getContext('2d');
        new Chart(ctxGrowth, {
            type: 'line',
            data: {
                labels: [${growthLabels}],
                datasets: [{
                    label: 'New Enrollments',
                    data: [${growthData}],
                    borderColor: '#4e73df',
                    backgroundColor: 'rgba(78, 115, 223, 0.1)',
                    borderWidth: 3,
                    pointRadius: 4,
                    pointBackgroundColor: '#4e73df',
                    fill: true,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: { 
                        beginAtZero: true, 
                        ticks: { stepSize: 1, font: { size: 13 } } 
                    },
                    x: {
                        ticks: { font: { size: 13 } }
                    }
                }
            }
        });
    });
</script>