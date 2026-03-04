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
                <label for="discountType">Discount Type <span class="text-danger">*</span></label>
                <select id="discountType" name="discountType" required>
                    <option value="amount" selected>Amount (VND)</option>
                    <option value="percent">Percent (%)</option>
                </select>
            </div>
            <div class="form-group">
                <label for="discountValue" id="discountValueLabel">Discount Amount <span class="text-danger">*</span></label>
                <input type="number" id="discountValue" name="discountValue" min="0" step="1000" placeholder="Enter amount (VND)" required>
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

<script>
    (function () {
        const discountType = document.getElementById("discountType");
        const discountValue = document.getElementById("discountValue");
        const discountValueLabel = document.getElementById("discountValueLabel");

        function syncDiscountInput() {
            const isAmount = discountType.value === "amount";
            if (isAmount) {
                discountValueLabel.innerHTML = 'Discount Amount <span class="text-danger">*</span>';
                discountValue.placeholder = "Enter amount (VND)";
                discountValue.step = "1000";
                discountValue.min = "0";
                discountValue.max = "";
            } else {
                discountValueLabel.innerHTML = 'Discount Percent <span class="text-danger">*</span>';
                discountValue.placeholder = "Enter percent (%)";
                discountValue.step = "0.1";
                discountValue.min = "0";
                discountValue.max = "100";
                if (discountValue.value && parseFloat(discountValue.value) > 100) {
                    discountValue.value = "";
                }
            }
        }

        discountType.addEventListener("change", syncDiscountInput);
        syncDiscountInput();
    })();
</script>
