<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/voucherDetail.css" rel="stylesheet" type="text/css"/>

<div class="page-header">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="voucher?action=all">Vouchers</a></li>
                <li class="breadcrumb-item active" aria-current="page">Voucher Information</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Voucher Management</h2>
                <p class="text-muted small mb-0">Manage and organize your vouchers</p>
            </div>
            <a href="voucher?action=all" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Vouchers
            </a>
        </div>
    </div>

    <div class="profile-header-card">
        <div class="profile-avatar-lg" style="background-color: #<c:out value='${voucher.code.hashCode() % 999999}'/>">
            ${fn:substring(voucher.code, 0, 1)}
        </div>
        <div class="profile-header-info">
            <h2 class="profile-name">${voucher.code}</h2>
            <span class="profile-active">${voucher.status ? 'Active' : 'Inactive'}</span>
            <div class="profile-info-content">
                <div class="profile-header-left">
                    <span class="user-email"><i class="bx bx-money"></i>${voucher.discountAmount} VND</span>
                    <span class="user-email"><i class="bx bx-percent"></i>${voucher.discountPercent}%</span>
                </div>
            </div>
        </div>
    </div>

    <div class="profile-content-card">
        <div class="profile-tabs">            
            <a href="#" class="tab-item active"><i class='bx bxs-user'></i>Voucher Information</a>
        </div>

        <div class="tab-content" id="overview">
            <div class="info-section">
                <div class="info-grid">
                    <div class="info-item">
                        <p>Voucher ID</p>
                        <span>${voucher.voucherId}</span>
                    </div>
                    <div class="info-item">
                        <p>Code</p>
                        <span>${voucher.code}</span>
                    </div>
                    <div class="info-item">
                        <p>Discount Amount</p>
                        <span><fmt:formatNumber value="${voucher.discountAmount}" type="number"/> VND</span>
                    </div>
                    <div class="info-item">
                        <p>Discount Percent</p>
                        <span>${voucher.discountPercent}%</span>
                    </div> 
                    <div class="info-item">
                        <p>Valid Until</p>
                        <span><fmt:formatDate value="${voucher.validUntil}" pattern="dd/MM/yyyy"/></span>
                    </div> 
                    <div class="info-item">
                        <p>Status</p>
                        <span>${voucher.status ? 'Active' : 'Inactive'}</span>
                    </div> 
                </div>
            </div>
        </div>
    </div>
</div>
