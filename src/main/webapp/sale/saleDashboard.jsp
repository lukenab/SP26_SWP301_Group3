<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/saleDashboard.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body sale-dashboard">
    <div class="sale-hero">
        <div>
            <h2>Sales Dashboard</h2>
            <p>Quick view of sales activity and shortcuts to key workflows.</p>
        </div>
    </div>

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

    <div class="sale-section">
        <h4>Quick Actions</h4>
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
            <a class="sale-quick-card" href="voucher?action=all">
                <i class='bx bx-purchase-tag'></i>
                <span>Voucher List</span>
            </a>
        </div>
    </div>

    <div class="sale-section">
        <h4>Notes</h4>
        <div class="sale-empty">
            No extra notes yet. Use quick actions to start working on leads, payments, and reports.
        </div>
    </div>
</div>
