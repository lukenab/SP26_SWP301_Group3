<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container-fluid px-4 content-body sale-dashboard">
    <div class="sale-hero">
        <div>
            <div class="sale-breadcrumb">Sales workspace</div>
            <h2>Sales Dashboard</h2>
            <p>Follow new leads, active vouchers, payments, and quick actions from one board.</p>
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
                        <i class='bx bx-cross-circle'></i>
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


    <div class="sale-stat-grid">
        <div class="sale-stat-card">
            <div>
                <p>Total Leads</p>
                <h3>${totalLeads}</h3>
            </div>
            <div class="sale-stat-icon blue">
                <i class='bx bx-user'></i>
            </div>
        </div>
        <div class="sale-stat-card">
            <div>
                <p>Converted Leads</p>
                <h3>${convertedLeads}</h3>
            </div>
            <div class="sale-stat-icon green">
                <i class='bx bx-user-check'></i>
            </div>
        </div>
        <div class="sale-stat-card">
            <div>
                <p>Pending Payments</p>
                <h3>${pendingPayments}</h3>
            </div>
            <div class="sale-stat-icon amber">
                <i class='bx bx-time-five'></i>
            </div>
        </div>
        <div class="sale-stat-card">
            <div>
                <p>Approved Payments</p>
                <h3>${approvedPayments}</h3>
            </div>
            <div class="sale-stat-icon gray">
                <i class='bx bx-check-circle'></i>
            </div>
        </div>
    </div>

    <div class="sale-board-grid">
        <section class="sale-panel sale-panel-wide">
            <div class="sale-panel-header">
                <div>
                    <h4>New Leads</h4>
                    <p>Latest leads that need follow-up from sales.</p>
                </div>
                <a href="lead?action=all">View all</a>
            </div>

            <div class="sale-list-head sale-lead-grid">
                <span>Lead</span>
                <span>Course</span>
                <span>Status</span>
            </div>

            <div class="sale-list-wrap">
                <c:choose>
                    <c:when test="${not empty latestNewLeads}">
                        <c:forEach var="lead" items="${latestNewLeads}">
                            <div class="sale-list-row sale-lead-grid">
                                <div class="sale-person-cell">
                                    <div class="sale-avatar">${fn:toUpperCase(fn:substring(lead.fullName, 0, 1))}</div>
                                    <div>
                                        <strong>${lead.fullName}</strong>
                                        <span>${lead.email}</span>
                                        <span class="sale-meta-line">Created ${fn:substring(lead.createDate, 0, 10)}</span>
                                    </div>
                                </div>
                                <div class="sale-list-text">${lead.course != null ? lead.course.courseName : 'Unassigned'}</div>
                                <div><span class="sale-pill new">${lead.status}</span></div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="sale-table-empty">No new leads at the moment.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <section class="sale-panel sale-panel-wide">
            <div class="sale-panel-header">
                <div>
                    <h4>Active Vouchers</h4>
                    <p>Open vouchers sorted by nearest expiration date.</p>
                </div>
                <a href="voucher?action=all">Voucher list</a>
            </div>

            <div class="sale-list-head sale-voucher-grid">
                <span>Code</span>
                <span>Discount</span>
                <span>Remaining</span>
                <span>Valid Until</span>
            </div>

            <div class="sale-list-wrap">
                <c:choose>
                    <c:when test="${not empty activeVouchers}">
                        <c:forEach var="voucher" items="${activeVouchers}">
                            <div class="sale-list-row sale-voucher-grid">
                                <div><span class="sale-code">${voucher.code}</span></div>
                                <div class="sale-list-text">
                                    <c:choose>
                                        <c:when test="${not empty voucher.discountAmount}">
                                            <fmt:formatNumber value="${voucher.discountAmount}" type="number"/> VND
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber value="${voucher.discountPercent}" type="number"/>%
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="sale-list-text">${voucher.remainingCount}</div>
                                <div class="sale-list-text"><fmt:formatDate value="${voucher.validUntil}" pattern="dd/MM/yyyy"/></div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="sale-table-empty">No active vouchers available.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <section class="sale-panel sale-panel-wide">
            <div class="sale-panel-header">
                <div>
                    <h4>Open Classes</h4>
                    <p>Classes currently open for enrollment and ready for sales follow-up.</p>
                </div>
                <a href="lead?action=openClasses">Open list</a>
            </div>

            <div class="sale-list-head sale-class-grid">
                <span>Class</span>
                <span>Teacher</span>
                <span>Capacity</span>
            </div>

            <div class="sale-list-wrap">
                <c:choose>
                    <c:when test="${not empty openClassPreview}">
                        <c:forEach var="cls" items="${openClassPreview}">
                            <div class="sale-list-row sale-class-grid">
                                <div class="sale-list-text">
                                    <strong>${cls[1]}</strong>
                                    <div class="sale-subtext">${cls[2]}</div>
                                    <div class="sale-subtext">Deadline <fmt:formatDate value="${cls[9]}" pattern="dd/MM/yyyy"/></div>
                                </div>
                                <div class="sale-list-text">${cls[3]}</div>
                                <div class="sale-list-text">${cls[7]}/${cls[8]}</div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="sale-table-empty">No open classes available.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </div>

    <div class="sale-bottom-grid">
        <section class="sale-panel">
            <div class="sale-panel-header">
                <div>
                    <h4>Quick Actions</h4>
                    <p>Fast access to daily sales workflows.</p>
                </div>
            </div>

            <div class="sale-quick-grid">
                <a class="sale-quick-card" href="lead?action=all">
                    <i class='bx bx-user-plus'></i>
                    <span>Manage Leads</span>
                </a>
                <a class="sale-quick-card" href="lead?action=addStudentAtCenter">
                    <i class='bx bx-user-check'></i>
                    <span>Add Walk-in Student</span>
                </a>
                <a class="sale-quick-card" href="lead?action=openClasses">
                    <i class='bx bx-door-open'></i>
                    <span>Open Classes</span>
                </a>
                <a class="sale-quick-card" href="payment?action=list">
                    <i class='bx bx-check-circle'></i>
                    <span>Verify Payments</span>
                </a>
                <a class="sale-quick-card" href="lead?action=salesReport">
                    <i class='bx bx-file'></i>
                    <span>Sales Report</span>
                </a>
                <a class="sale-quick-card" href="lead?action=revenueReport">
                    <i class='bx bx-wallet'></i>
                    <span>Revenue Report</span>
                </a>
            </div>
        </section>

        <section class="sale-panel">
            <div class="sale-panel-header">
                <div>
                    <h4>Today Snapshot</h4>
                    <p>Small briefing for the sales queue.</p>
                </div>
            </div>

            <div class="sale-mini-list">
                <div class="sale-mini-item">
                    <span>New leads waiting</span>
                    <strong>${fn:length(latestNewLeads)}</strong>
                </div>
                <div class="sale-mini-item">
                    <span>Vouchers running</span>
                    <strong>${fn:length(activeVouchers)}</strong>
                </div>
                <div class="sale-mini-item">
                    <span>Open classes</span>
                    <strong>${fn:length(openClassPreview)}</strong>
                </div>
                <div class="sale-mini-item">
                    <span>Pending payments</span>
                    <strong>${pendingPayments}</strong>
                </div>
            </div>
        </section>
    </div>
</div>
