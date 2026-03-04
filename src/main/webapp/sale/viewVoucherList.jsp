<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/viewVoucherList.css" rel="stylesheet" type="text/css"/>
<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Voucher Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Voucher Management</h2>
                <p class="text-muted small mb-0">Manage and organize your vouchers</p>
            </div>
            <a href="voucher?action=add" class="btn btn-add-new">
                <i class='bx bx-plus-circle'></i> Add New Voucher
            </a>
        </div>
    </div>

    <c:set var="activeVoucher" value="0"/>
    <c:set var="inactiveVoucher" value="0"/>

    <c:forEach items="${voucherList}" var="v">
        <c:if test="${v.status}">
            <c:set var="activeVoucher" value="${activeVoucher + 1}"/>
        </c:if>
        <c:if test="${!v.status}">
            <c:set var="inactiveVoucher" value="${inactiveVoucher + 1}"/>
        </c:if>
    </c:forEach>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Vouchers</p>
                <h3>${fn:length(voucherList)}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-coupon'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Active Vouchers</p>
                <h3>${activeVoucher}</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-check-shield'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Inactive Vouchers</p>
                <h3>${inactiveVoucher}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-lock-alt'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Active Rate</p>
                <h3>
                    <c:choose>
                        <c:when test="${fn:length(voucherList) > 0}">
                            <fmt:formatNumber value="${(activeVoucher * 100.0) / fn:length(voucherList)}"
                                              minFractionDigits="2"
                                              maxFractionDigits="2"/>%
                        </c:when>
                        <c:otherwise>0%</c:otherwise>
                    </c:choose>
                </h3>
            </div>
            <div class="icon-wrapper orange">
                <i class='bx bxs-pie-chart'></i>
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

    <form action="voucher" method="GET" class="filter-container flex-wrap">
        <input type="hidden" name="action" value="all">
        <div class="custom-search-bar">
            <i class='bx bx-search text-muted fs-5'></i>
            <input type="text" name="searchQuery" value="${searchQuery}" placeholder="Search by voucher code...">
        </div>

        <div class="d-flex gap-3">
            <select class="custom-select-filter" name="status">
                <option value="all" ${statusFilter == 'all' ? 'selected' : ''}>All Status</option>
                <option value="1" ${statusFilter == '1' ? 'selected' : ''}>Active</option>
                <option value="0" ${statusFilter == '0' ? 'selected' : ''}>Inactive</option>
            </select>
            <button type="submit" class="btn btn-add-new">
                <i class='bx bx-filter-alt'></i> Filter
            </button>
            <a href="voucher?action=all" class="btn btn-cancel">Reset</a>
        </div>
    </form>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 5%">#</th>
                        <th style="width: 22%">Code</th>
                        <th style="width: 22%">Discount</th>
                        <th style="width: 20%">Valid Until</th>
                        <th style="width: 11%">Status</th>
                        <th style="width: 20%">Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach items="${voucherList}" var="v" varStatus="loop">
                        <tr class="${!v.status ? 'row-inactive' : ''}">
                            <td>${loop.count}</td>
                            <td>${v.code}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.discountAmount > 0}">
                                        <fmt:formatNumber value="${v.discountAmount}" type="number"/> VND
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${v.discountPercent}" minFractionDigits="0" maxFractionDigits="2"/>%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${v.validUntil}" pattern="dd/MM/yyyy"/></td>

                            <td>
                                <c:choose>
                                    <c:when test="${v.status}">
                                        <span class="badge badge-saleStaff">Active</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-academicStaff">Inactive</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${!v.status}">
                                        <span class="action-btn action-disabled"><i class='bx bx-eye'></i></span>
                                        <span class="action-btn action-disabled"><i class='bx bx-edit'></i></span>
                                        <a href="voucher?action=delete&id=${v.voucherId}" class="action-btn">
                                            <i class='bx bx-lock-open'></i>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="voucher?action=detail&id=${v.voucherId}" class="action-btn"><i class='bx bx-eye'></i></a>
                                        <a href="voucher?action=edit&id=${v.voucherId}" class="action-btn"><i class='bx bx-edit'></i></a>
                                        <a href="voucher?action=delete&id=${v.voucherId}" class="action-btn delete">
                                            <i class='bx bx-lock'></i>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-between align-items-center p-3 border-top">
            <div class="text-muted small">Total ${fn:length(voucherList)} vouchers</div>
            <div>
                <ul class="pagination pagination-sm mb-0">
                    <li class="page-item disabled"><a class="page-link" href="#"><i class='bx bx-chevron-left'></i> Previous</a></li>
                    <li class="page-item active"><a class="page-link" href="#">1</a></li>
                    <li class="page-item disabled"><a class="page-link" href="#">Next <i class='bx bx-chevron-right'></i></a></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>
