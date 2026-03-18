<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/class_management.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body class-management-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="enrollment?action=classes">Class Management</a></li>
                <li class="breadcrumb-item active" aria-current="page">Add Student</li>
            </ol>
        </div>

        <div class="content-header">
            <div>
                <h2 class="page-title">Add Student to Class</h2>
                <p class="text-muted small mb-0">
                    Class: <strong>${classInfo[1]}</strong> | Course: ${classInfo[2]} | Current Students: ${classInfo[7]} / ${classInfo[8]} | Remaining Slots: ${remainingSlots}
                </p>
            </div>
            <a href="enrollment?action=classes" class="btn btn-back">
                <i class='bx bx-left-arrow-alt'></i> Back to Class List
            </a>
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
        </div>
        <c:remove var="message" scope="session"/>
        <c:remove var="messageType" scope="session"/>
    </c:if>

    <div class="row g-4">
        <div class="col-lg-7">
            <div class="card user-table-card border-0 bg-white h-100">
                <div class="card-header bg-white border-bottom-0 pt-3 px-3">
                    <h5 class="mb-0">Available Students</h5>
                    <p class="text-muted small mb-0 mt-2">
                        <c:choose>
                            <c:when test="${remainingSlots > 0}">
                                You can add up to <strong>${remainingSlots}</strong> more student(s) to this class.
                            </c:when>
                            <c:otherwise>
                                This class has reached maximum capacity.
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
                <div class="table-responsive">
                    <form action="enrollment" method="post">
                        <input type="hidden" name="action" value="addStudents"/>
                        <input type="hidden" name="classId" value="${classInfo[0]}"/>

                        <table class="table mb-0 align-middle">
                            <thead>
                                <tr>
                                    <th style="width: 10%">
                                        <input class="form-check-input" type="checkbox" id="selectAllAvailable"/>
                                    </th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Enrollment Date</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty availableStudents}">
                                    <tr>
                                        <td colspan="5" class="text-center text-muted py-4">No available students.</td>
                                    </tr>
                                </c:if>
                                <c:forEach items="${availableStudents}" var="s">
                                    <tr>
                                        <td>
                                            <input class="form-check-input student-checkbox"
                                                   type="checkbox"
                                                   name="studentIds"
                                                   value="${s[0]}"
                                                   ${remainingSlots <= 0 ? 'disabled' : ''}/>
                                        </td>
                                        <td class="fw-semibold">${s[1]}</td>
                                        <td>${s[2]}</td>
                                        <td><fmt:formatDate value="${s[3]}" pattern="dd/MM/yyyy"/></td>
                                        <td>
                                            <span class="badge-status badge-inactive">
                                                ${s[4]}
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div class="p-3 border-top d-flex justify-content-end align-items-center gap-3">
                            <div class="payment-status-toggle" role="radiogroup" aria-label="Payment status">
                                <input type="radio" id="statusPaid" name="enrollmentStatus" value="Paid">
                                <label for="statusPaid">Paid</label>

                                <input type="radio" id="statusUnpaid" name="enrollmentStatus" value="UnPaid" checked>
                                <label for="statusUnpaid">UnPaid</label>
                            </div>
                            <button type="submit" class="btn btn-add-new" ${remainingSlots <= 0 ? 'disabled' : ''}>
                                <i class='bx bx-user-plus'></i> Add Selected Students
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-lg-5">
            <div class="card user-table-card border-0 bg-white h-100">
                <div class="card-header bg-white border-bottom-0 pt-3 px-3">
                    <h5 class="mb-0">Students In Class (${studentsInClass.size()})</h5>
                </div>
                <div class="table-responsive">
                    <form action="enrollment" method="post">
                        <input type="hidden" name="action" value="removeStudents"/>
                        <input type="hidden" name="classId" value="${classInfo[0]}"/>

                        <table class="table mb-0 align-middle">
                            <thead>
                                <tr>
                                    <th style="width: 10%">
                                        <input class="form-check-input" type="checkbox" id="selectAllInClass"/>
                                    </th>
                                    <th>Name</th>
                                    <th>Enrollment Date</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty studentsInClass}">
                                    <tr>
                                        <td colspan="4" class="text-center text-muted py-4">No students in class.</td>
                                    </tr>
                                </c:if>

                                <c:forEach items="${studentsInClass}" var="s">
                                    <tr>
                                        <td>
                                            <input class="form-check-input inclass-checkbox"
                                                   type="checkbox"
                                                   name="studentIds"
                                                   value="${s[1]}"/>
                                        </td>
                                        <td>
                                            <div class="fw-semibold">${s[2]}</div>
                                            <small class="text-muted">${s[3]}</small>
                                        </td>
                                        <td><fmt:formatDate value="${s[4]}" pattern="dd/MM/yyyy"/></td>
                                        <td>
                                            <span class="badge-status ${s[5] == 'Paid' || s[5] == 'Active' || s[5] == 'Completed' ? 'badge-active' : 'badge-inactive'}">
                                                ${s[5] == 'Paid' || s[5] == 'Active' || s[5] == 'Completed' ? 'Paid' : 'UnPaid'}
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <c:if test="${not empty studentsInClass}">
                            <div class="p-3 border-top d-flex justify-content-end">
                                <button type="submit" class="btn btn-remove-student">
                                    <i class='bx bx-user-minus'></i> Remove Students
                                </button>
                            </div>
                        </c:if>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    (function () {
        const remainingSlots = ${remainingSlots};
        const selectAllAvailable = document.getElementById('selectAllAvailable');
        const availableCheckboxes = document.querySelectorAll('.student-checkbox');
        const addForm = document.querySelector('form[action="enrollment"] input[name="action"][value="addStudents"]')?.closest('form');

        function syncAvailableSelection() {
            const checkedCount = Array.from(availableCheckboxes).filter(cb => cb.checked).length;
            availableCheckboxes.forEach(cb => {
                if (!cb.checked) {
                    cb.disabled = remainingSlots <= 0 || checkedCount >= remainingSlots;
                }
            });
            if (selectAllAvailable) {
                const enabledCheckboxes = Array.from(availableCheckboxes).filter(cb => !cb.disabled);
                selectAllAvailable.checked = availableCheckboxes.length > 0 && checkedCount === availableCheckboxes.length;
                selectAllAvailable.disabled = remainingSlots <= 0 || enabledCheckboxes.length === 0;
            }
        }

        if (selectAllAvailable) {
            selectAllAvailable.addEventListener('change', function () {
                let selected = 0;
                availableCheckboxes.forEach(cb => {
                    if (selectAllAvailable.checked && selected < remainingSlots && !cb.disabled) {
                        cb.checked = true;
                        selected++;
                    } else {
                        cb.checked = false;
                    }
                });
                syncAvailableSelection();
            });
        }

        availableCheckboxes.forEach(cb => {
            cb.addEventListener('change', syncAvailableSelection);
        });

        if (addForm) {
            addForm.addEventListener('submit', function (event) {
                const checkedCount = Array.from(availableCheckboxes).filter(cb => cb.checked).length;
                if (checkedCount > remainingSlots) {
                    event.preventDefault();
                    alert('You can only add up to ' + remainingSlots + ' student(s) to this class.');
                }
            });
        }

        const selectAllInClass = document.getElementById('selectAllInClass');
        const inClassCheckboxes = document.querySelectorAll('.inclass-checkbox');
        if (selectAllInClass) {
            selectAllInClass.addEventListener('change', function () {
                inClassCheckboxes.forEach(cb => cb.checked = selectAllInClass.checked);
            });
        }

        syncAvailableSelection();
    })();
</script>
