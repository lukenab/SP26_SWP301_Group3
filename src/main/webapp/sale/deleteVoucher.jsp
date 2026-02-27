<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/deleteVoucher.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">${voucher.status ? 'Deactivate Voucher' : 'Restore Voucher'}</h1>
        </div>
        <a href="voucher?action=all" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Vouchers
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="voucher?action=all">Voucher Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">${voucher.status ? 'Deactivate Voucher' : 'Restore Voucher'}</li>
        </ol>
    </div>
</div>

<div class="form-container">
    <p class="form-title">Voucher Information</p>

    <c:choose>
        <c:when test="${!voucher.status}">
            <form action="voucher?action=restore" method="POST" class="form-body">
                <input type="hidden" name="voucherId" value="${voucher.voucherId}">

                <div class="form-row">
                    <div class="form-group">
                        <label for="code">Code</label>
                        <input type="text" id="code" value="${voucher.code}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="status">Current Status</label>
                        <input type="text" id="status" value="Inactive" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="amount">Discount Amount</label>
                        <input type="text" id="amount" value="${voucher.discountAmount}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="percent">Discount Percent</label>
                        <input type="text" id="percent" value="${voucher.discountPercent}%" readonly>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="voucher?action=all" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-unlock">
                        <i class='bx bx-lock-open'></i> Restore Voucher
                    </button>
                </div>
            </form>
        </c:when>
        <c:otherwise>
            <form action="voucher?action=delete" method="POST" class="form-body">
                <input type="hidden" name="voucherId" value="${voucher.voucherId}">

                <div class="form-row">
                    <div class="form-group">
                        <label for="code">Code</label>
                        <input type="text" id="code" value="${voucher.code}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="status">Current Status</label>
                        <input type="text" id="status" value="Active" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="amount">Discount Amount</label>
                        <input type="text" id="amount" value="${voucher.discountAmount}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="percent">Discount Percent</label>
                        <input type="text" id="percent" value="${voucher.discountPercent}%" readonly>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="voucher?action=all" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-lock">
                        <i class='bx bx-lock'></i> Deactivate Voucher
                    </button>
                </div>
            </form>
        </c:otherwise>
    </c:choose>
</div>
