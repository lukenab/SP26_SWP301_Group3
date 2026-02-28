<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<link href="css/addVoucher.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Add New Voucher</h1>
        </div>
        <a href="voucher?action=all" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Vouchers
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="voucher?action=all">Voucher Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Add New Voucher</li>
        </ol>
    </div>
</div>

<div class="form-container">
    <p class="form-title">Voucher Information</p>
    <form action="voucher?action=create" method="POST" class="form-body">
        <div class="form-row">
            <div class="form-group">
                <label for="code">Code <span class="text-danger">*</span></label>
                <input type="text" id="code" name="code" required>
            </div>
            <div class="form-group">
                <label for="validUntil">Valid Until</label>
                <input type="date" id="validUntil" name="validUntil">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="discountAmount">Discount Amount <span class="text-danger">*</span></label>
                <input type="number" id="discountAmount" name="discountAmount" min="0" step="1000" value="0" required>
            </div>
            <div class="form-group">
                <label for="discountPercent">Discount Percent <span class="text-danger">*</span></label>
                <input type="number" id="discountPercent" name="discountPercent" min="0" max="100" step="0.1" value="0" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="status">Status</label>
                <select id="status" name="status">
                    <option value="1">Active</option>
                    <option value="0">Inactive</option>
                </select>
            </div>
        </div>

        <div class="form-buttons">
            <a href="voucher?action=all" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-save'></i> Add New Voucher
            </button>
        </div>
    </form>
</div>
