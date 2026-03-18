<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/class_management.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body class-management-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="enrollment?action=classes">Class Management</a></li>
                <li class="breadcrumb-item active" aria-current="page">Create Class</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Create New Class</h2>
                <p class="text-muted small mb-0">Create a class and assign course/teacher.</p>
            </div>
            <a href="enrollment?action=classes" class="btn btn-back">
                <i class='bx bx-left-arrow-alt'></i> Back to Class List
            </a>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="card-body p-4">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger mb-3">${errorMessage}</div>
            </c:if>
            <c:if test="${empty teacherOptions}">
                <div class="alert alert-warning mb-3">
                    No teacher found. Please create at least one user with Teacher role first.
                </div>
            </c:if>

            <form action="enrollment" method="post">
                <input type="hidden" name="action" value="createClass"/>

                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Class Name</label>
                        <input type="text" class="form-control" name="className" value="${param.className}" required>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Status</label>
                        <select class="form-select" name="status" required>
                            <option value="">Select status</option>
                            <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>Pending</option>
                            <option value="Active" ${param.status == 'Active' ? 'selected' : ''}>Active</option>
                            <option value="Inactive" ${param.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Course</label>
                        <select class="form-select" name="courseId" id="courseId" required>
                            <option value="">Select course</option>
                            <c:forEach items="${courseOptions}" var="co">
                                <option value="${co[0]}" data-max-capacity="${co[2]}" ${param.courseId == co[0].toString() ? 'selected' : ''}>
                                    ${co[1]}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Teacher</label>
                        <select class="form-select" name="teacherId" required>
                            <option value="">Select teacher</option>
                            <c:forEach items="${teacherOptions}" var="t">
                                <option value="${t[0]}" ${param.teacherId == t[0].toString() ? 'selected' : ''}>
                                    ${t[1]} - ${t[2]}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Start Date</label>
                        <input type="date" class="form-control" name="startDate" value="${param.startDate}" required>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">End Date</label>
                        <input type="date" class="form-control" name="endDate" value="${param.endDate}" required>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Max Capacity</label>
                        <input type="number" class="form-control" id="maxCapacity" name="maxCapacity" value="${param.maxCapacity}" min="1" placeholder="Enter max capacity" required>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Room (Optional)</label>
                        <input type="text" class="form-control" value="Assign later in schedule management" readonly>
                    </div>

                </div>

                <div class="d-flex justify-content-end mt-4">
                    <button type="submit" class="btn btn-add-new">
                        <i class='bx bx-save'></i> Create Class
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    (function () {
        var courseSelect = document.getElementById('courseId');
        var maxCapacityInput = document.getElementById('maxCapacity');

        if (!courseSelect || !maxCapacityInput) {
            return;
        }

        function updateCapacity() {
            var selectedOption = courseSelect.options[courseSelect.selectedIndex];
            var maxCapacity = selectedOption ? selectedOption.getAttribute('data-max-capacity') : '';
            if (!maxCapacityInput.value && maxCapacity) {
                maxCapacityInput.value = maxCapacity;
            }
        }

        courseSelect.addEventListener('change', updateCapacity);
        updateCapacity();
    })();
</script>
