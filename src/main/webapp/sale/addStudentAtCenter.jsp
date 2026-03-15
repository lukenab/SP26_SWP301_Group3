<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="css/viewLeadList.css" rel="stylesheet" type="text/css"/>
<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="lead?action=all">Lead Management</a></li>
                <li class="breadcrumb-item active" aria-current="page">Add Walk-in Student</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Add Walk-in Student</h2>
                <p class="text-muted small mb-0">Create student account, enroll in class, and mark payment as completed.</p>
            </div>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-body p-4">
            <form action="lead" method="POST" class="form-body">
                <input type="hidden" name="action" value="createStudentAtCenter">

                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Full Name</label>
                        <input type="text" class="form-control" name="fullName" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Email</label>
                        <input type="email" class="form-control" name="email" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Phone</label>
                        <input type="text" class="form-control" name="phone" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Gender</label>
                        <select class="form-select" name="gender" required>
                            <option value="">Select gender</option>
                            <option value="male">Male</option>
                            <option value="female">Female</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Date of Birth</label>
                        <input type="date" class="form-control" name="dob" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Course</label>
                        <select class="form-select" id="courseSelect">
                            <option value="">Select course</option>
                            <c:forEach items="${openCourseList}" var="co">
                                <option value="${co[0]}">${co[1]}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Class</label>
                        <select class="form-select" id="classSelect" name="classId" required>
                            <option value="">Select class</option>
                            <c:forEach items="${classList}" var="cls">
                                <option value="${cls[0]}" data-course-id="${cls[2]}" data-price="${cls[4]}">
                                    ${cls[1]} - ${cls[3]}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="card shadow-sm mt-4">
                    <div class="card-body p-3">
                        <h6 class="mb-3">Payment Summary</h6>
                        <div class="d-flex justify-content-between">
                            <span class="text-muted">Base Tuition Fee:</span>
                            <span id="basePrice">0</span>
                        </div>
                        <div class="d-flex justify-content-between mt-2">
                            <span class="text-muted">Voucher Code:</span>
                            <div class="d-flex gap-2 w-50">
                                <input type="text" class="form-control form-control-sm" id="voucherCode" name="voucherCode" placeholder="Optional">
                                <button type="button" class="btn btn-sm btn-outline-primary" id="applyVoucherBtn">Apply</button>
                            </div>
                        </div>
                        <div class="mt-2 small" id="voucherMessage" style="display:none;"></div>
                        <div class="d-flex justify-content-between mt-2">
                            <span class="text-muted">Discount:</span>
                            <span id="discountAmount">0</span>
                        </div>
                        <div class="d-flex justify-content-between mt-2">
                            <span class="fw-bold">Grand Total:</span>
                            <span class="fw-bold text-danger" id="finalAmount">0</span>
                        </div>
                        <div class="small text-muted mt-2">
                            Final amount will be calculated after submission based on voucher validity.
                        </div>
                    </div>
                </div>

                <div class="mt-4 d-flex gap-2">
                    <button type="submit" class="btn btn-add-new">Create & Enroll</button>
                    <a href="lead?action=all" class="btn btn-cancel">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    (function () {
        const courseSelect = document.getElementById('courseSelect');
        const classSelect = document.getElementById('classSelect');
        const basePrice = document.getElementById('basePrice');
        const discountAmount = document.getElementById('discountAmount');
        const finalAmount = document.getElementById('finalAmount');
        const voucherCodeInput = document.getElementById('voucherCode');
        const applyVoucherBtn = document.getElementById('applyVoucherBtn');
        const voucherMessage = document.getElementById('voucherMessage');

        function formatMoney(value) {
            if (value == null || isNaN(value)) {
                return '0';
            }
            return Number(value).toLocaleString('vi-VN') + ' VND';
        }

        function resetVoucherDisplay() {
            discountAmount.textContent = formatMoney(0);
            const option = classSelect.options[classSelect.selectedIndex];
            const price = option ? option.getAttribute('data-price') : null;
            const base = price ? Number(price) : 0;
            finalAmount.textContent = formatMoney(base);
            voucherMessage.style.display = 'none';
            voucherMessage.textContent = '';
            voucherMessage.className = 'mt-2 small';
        }

        function updatePrice() {
            const option = classSelect.options[classSelect.selectedIndex];
            const price = option ? option.getAttribute('data-price') : null;
            const base = price ? Number(price) : 0;
            basePrice.textContent = formatMoney(base);
            resetVoucherDisplay();
        }

        function filterClasses() {
            const courseId = courseSelect.value;
            let firstVisibleValue = '';
            for (let i = 0; i < classSelect.options.length; i++) {
                const opt = classSelect.options[i];
                if (!opt.value) {
                    opt.hidden = false;
                    continue;
                }
                const optCourseId = opt.getAttribute('data-course-id');
                opt.hidden = courseId && optCourseId !== courseId;
                if (!opt.hidden && !firstVisibleValue) {
                    firstVisibleValue = opt.value;
                }
            }
            classSelect.value = firstVisibleValue || '';
            updatePrice();
        }

        courseSelect.addEventListener('change', filterClasses);
        classSelect.addEventListener('change', updatePrice);
        classSelect.addEventListener('input', updatePrice);
        classSelect.addEventListener('click', updatePrice);

        applyVoucherBtn.addEventListener('click', async function () {
            const classId = classSelect.value;
            const voucherCode = voucherCodeInput.value.trim();
            if (!classId) {
                voucherMessage.style.display = 'block';
                voucherMessage.className = 'mt-2 small text-danger';
                voucherMessage.textContent = 'Please select a class first.';
                return;
            }
            if (!voucherCode) {
                resetVoucherDisplay();
                return;
            }
            try {
                const params = new URLSearchParams({
                    action: 'validateVoucher',
                    classId,
                    voucherCode
                });
                const response = await fetch(`${pageContext.request.contextPath}/lead`, {
                    method: 'POST',
                    headers: { 'Accept': 'application/json', 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: params.toString()
                });
                const text = await response.text();
                let data = null;
                try {
                    data = JSON.parse(text);
                } catch (parseErr) {
                    data = null;
                }
                if (!response.ok || !data) {
                    throw new Error('Invalid response');
                }
                if (data.valid) {
                    discountAmount.textContent = formatMoney(data.discountAmount);
                    finalAmount.textContent = formatMoney(data.finalAmount);
                    voucherMessage.style.display = 'block';
                    voucherMessage.className = 'mt-2 small text-success';
                    voucherMessage.textContent = data.message;
                } else {
                    resetVoucherDisplay();
                    voucherMessage.style.display = 'block';
                    voucherMessage.className = 'mt-2 small text-danger';
                    voucherMessage.textContent = data.message || 'Invalid voucher code.';
                }
            } catch (err) {
                voucherMessage.style.display = 'block';
                voucherMessage.className = 'mt-2 small text-danger';
                voucherMessage.textContent = 'Failed to apply voucher. Please try again.';
            }
        });

        updatePrice();
    })();
</script>
