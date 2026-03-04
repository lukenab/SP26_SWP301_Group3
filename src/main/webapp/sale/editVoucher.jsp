<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="css/editVoucher.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Edit Voucher</h1>
        </div>
        <div class="content-header-actions">
            <a href="voucher?action=all" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Vouchers
            </a>
        </div>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="voucher?action=all">Voucher Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Edit Voucher</li>
        </ol>
    </div>
</div>

<div class="profile-header-card">
    <div class="lead-avatar">
        <span>${fn:toUpperCase(fn:substring(voucher.code, 0, 1))}</span>
    </div>

        <div class="profile-header-info">
        <h2 class="profile-name">${voucher.code}</h2>
        <span class="profile-active ${voucher.status ? 'status-consulting' : 'status-lost'}">${voucher.status ? 'Active' : 'Inactive'}</span>
        <div class="profile-info-content">
            <div class="profile-header-left">
                <span class="user-email">
                    <i class="bx bx-money"></i>
                    <c:choose>
                        <c:when test="${voucher.discountAmount > 0}">
                            ${voucher.discountAmount} VND
                        </c:when>
                        <c:otherwise>
                            ${voucher.discountPercent}%
                        </c:otherwise>
                    </c:choose>
                </span>
            </div>
        </div>
    </div>
</div>

<div class="form-container">
    <form action="voucher" method="POST" class="form-body">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="voucherId" value="${voucher.voucherId}">

        <div class="form-row">
            <div class="form-group">
                <label for="code">Code</label>
                <input type="text" id="code" name="code" value="${voucher.code}" required>
            </div>
            <div class="form-group">
                <label for="validUntil">Valid Until</label>
                <input type="date" id="validUntil" name="validUntil" value="${voucher.validUntil}">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="discountType">Discount Type</label>
                <select id="discountType" name="discountType" required>
                    <option value="amount" ${voucher.discountAmount > 0 ? 'selected' : ''}>Amount (VND)</option>
                    <option value="percent" ${voucher.discountAmount <= 0 ? 'selected' : ''}>Percent (%)</option>
                </select>
            </div>
            <div class="form-group">
                <label for="discountValue" id="discountValueLabel">Discount Value</label>
                <input type="number" id="discountValue" name="discountValue"
                       value="${voucher.discountAmount > 0 ? voucher.discountAmount : voucher.discountPercent}"
                       min="0" step="${voucher.discountAmount > 0 ? '1000' : '0.1'}"
                       max="${voucher.discountAmount > 0 ? '' : '100'}" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="status">Status</label>
                <select name="status" id="status" required>
                    <option value="1" ${voucher.status ? 'selected' : ''}>Active</option>
                    <option value="0" ${!voucher.status ? 'selected' : ''}>Inactive</option>
                </select>
            </div>
        </div>

        <div class="form-buttons">
            <a href="voucher?action=all" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-save'></i> Update Voucher
            </button>
        </div>
    </form>
</div>

<script>
    (function () {
        const discountType = document.getElementById("discountType");
        const discountValue = document.getElementById("discountValue");
        const discountValueLabel = document.getElementById("discountValueLabel");

        function syncDiscountInput() {
            const isAmount = discountType.value === "amount";
            if (isAmount) {
                discountValueLabel.textContent = "Discount Amount";
                discountValue.step = "1000";
                discountValue.min = "0";
                discountValue.max = "";
                discountValue.placeholder = "Enter amount (VND)";
            } else {
                discountValueLabel.textContent = "Discount Percent";
                discountValue.step = "0.1";
                discountValue.min = "0";
                discountValue.max = "100";
                discountValue.placeholder = "Enter percent (%)";
                if (discountValue.value && parseFloat(discountValue.value) > 100) {
                    discountValue.value = "";
                }
            }
        }

        discountType.addEventListener("change", syncDiscountInput);
        syncDiscountInput();
    })();
</script>
