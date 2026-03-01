<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<style>
    .schedule-card {
        padding: 10px 12px;
        border-radius: 8px;
        color: white;
        font-size: 0.8rem;
        margin-bottom: 6px;
        cursor: pointer;
        transition: all 0.2s ease;
        line-height: 1.4;
    }
    .schedule-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
    .schedule-card.color-0 { background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%); }
    .schedule-card.color-1 { background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%); }
    .schedule-card.color-2 { background: linear-gradient(135deg, #10b981 0%, #059669 100%); }
    .schedule-card.color-3 { background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%); }
    .schedule-card.color-4 { background: linear-gradient(135deg, #ec4899 0%, #db2777 100%); }
    .schedule-card.color-5 { background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%); }
    .schedule-card.color-6 { background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%); }
    .schedule-card.color-7 { background: linear-gradient(135deg, #14b8a6 0%, #0d9488 100%); }

    .schedule-table {
        border-collapse: separate;
        border-spacing: 0;
        width: 100%;
    }
    .schedule-table thead th {
        background-color: #f9fafb;
        font-weight: 600;
        color: #374151;
        padding: 14px 10px;
        border-bottom: 2px solid #e5e7eb;
        text-align: center;
        font-size: 0.95rem;
        position: sticky;
        top: 0;
        z-index: 10;
    }
    .schedule-table thead th:first-child {
        text-align: left;
    }
    .schedule-table tbody td {
        vertical-align: top;
        padding: 10px;
        border: 1px solid #e5e7eb;
        min-width: 140px;
        background-color: #ffffff;
    }
    .schedule-table tbody td:first-child {
        background-color: #f9fafb;
        min-width: 100px;
        position: sticky;
        left: 0;
        z-index: 5;
    }
    .time-slot {
        font-weight: 600;
        color: #1f2937;
        font-size: 0.9rem;
    }
    .time-slot small {
        display: block;
        color: #6b7280;
        font-weight: 400;
        margin-top: 2px;
        font-size: 0.75rem;
    }
    .filter-section {
        background: white;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 20px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    }
    .filter-section .form-label {
        font-weight: 500;
        color: #6b7280;
        font-size: 0.875rem;
        margin-bottom: 6px;
    }
    .schedule-card .course-code {
        font-size: 0.95rem;
        font-weight: 700;
        margin-bottom: 2px;
    }
    .schedule-card .course-name {
        font-size: 0.8rem;
        margin-bottom: 4px;
        opacity: 0.95;
    }
    .schedule-card small {
        display: block;
        opacity: 0.9;
        line-height: 1.4;
        font-size: 0.75rem;
    }
    .table-wrapper {
        overflow-x: auto;
        max-height: 70vh;
    }
    .empty-state {
        text-align: center;
        padding: 60px 20px;
        color: #6b7280;
    }
    .empty-state i {
        font-size: 4rem;
        color: #d1d5db;
        margin-bottom: 16px;
    }
    .btn-outline-primary {
        color: #2563eb;
        border: 1px solid #2563eb;
        background: transparent;
        padding: 8px 16px;
        border-radius: 6px;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 6px;
        transition: all 0.2s ease;
    }
    .btn-outline-primary:hover {
        background: #2563eb;
        color: white;
    }

    /* Add Schedule Modal */
    .modal {
        display: none;
        position: fixed;
        z-index: 1000;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0,0,0,0.5);
        animation: fadeIn 0.3s;
    }
    .modal.show {
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .modal-content {
        background-color: white;
        padding: 0;
        border-radius: 12px;
        width: 90%;
        max-width: 600px;
        max-height: 90vh;
        overflow-y: auto;
        animation: slideDown 0.3s;
        box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1);
    }
    .modal-header {
        padding: 20px 24px;
        border-bottom: 1px solid #e5e7eb;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .modal-header h3 {
        margin: 0;
        font-size: 1.25rem;
        font-weight: 600;
    }
    .modal-body {
        padding: 24px;
    }
    .modal-footer {
        padding: 16px 24px;
        border-top: 1px solid #e5e7eb;
        display: flex;
        justify-content: flex-end;
        gap: 12px;
    }
    .close {
        background: none;
        border: none;
        font-size: 1.5rem;
        cursor: pointer;
        color: #6b7280;
        line-height: 1;
    }
    .close:hover {
        color: #1f2937;
    }
    .color-picker {
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
    }
    .color-option {
        width: 40px;
        height: 40px;
        border-radius: 8px;
        cursor: pointer;
        border: 3px solid transparent;
        transition: all 0.2s;
    }
    .color-option:hover {
        transform: scale(1.1);
    }
    .color-option.selected {
        border-color: #1f2937;
        transform: scale(1.15);
    }
    @keyframes fadeIn {
        from { opacity: 0; }
        to { opacity: 1; }
    }
    @keyframes slideDown {
        from { transform: translateY(-50px); opacity: 0; }
        to { transform: translateY(0); opacity: 1; }
    }
</style>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Class Schedule</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Class Schedule</h2>
                <p class="text-muted small mb-0">Manage class timetable, teachers and rooms</p>
            </div>
            <div class="d-flex gap-2">
                <a href="schedule?action=viewAll" class="btn-outline-primary">
                    <i class='bx bx-calendar'></i> See All Schedule
                </a>
                <button class="btn btn-add-new" onclick="openAddModal()">
                    <i class='bx bx-plus-circle'></i> Add Schedule
                </button>
            </div>
        </div>
    </div>

    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("message"); %>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("error"); %>
    </c:if>

    <div class="card border-0 filter-section">
        <div class="row g-3">
            <div class="col-md-4">
                <label class="form-label">Filter by Room</label>
                <select class="form-select" id="roomFilter">
                    <option value="">All Rooms</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Filter by Teacher</label>
                <select class="form-select" id="teacherFilter">
                    <option value="">All Teachers</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Filter by Subject</label>
                <select class="form-select" id="subjectFilter">
                    <option value="">All Subjects</option>
                </select>
            </div>
        </div>
        <div class="mt-3">
            <button class="btn btn-primary" onclick="applyFilters()">
                <i class='bx bx-filter'></i> Apply Filters
            </button>
            <button class="btn btn-secondary ms-2" onclick="clearFilters()">
                <i class='bx bx-x'></i> Clear
            </button>
        </div>
    </div>

    <div id="scheduleResults">
        <c:choose>
            <c:when test="${not empty allSchedules}">
                <div class="card user-table-card border-0 bg-white">
                    <div class="card-header bg-white border-bottom d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">Filtered Results (${fn:length(allSchedules)} schedules found)</h5>
                        <button class="btn btn-sm btn-secondary" onclick="clearFilters()">
                            <i class='bx bx-x'></i> Clear Filters
                        </button>
                    </div>
                    <div class="table-wrapper">
                        <table class="table mb-0 schedule-table">
                            <thead>
                                <tr>
                                    <th style="width: 100px;">Time<br>Slot</th>
                                    <th>Monday</th>
                                    <th>Tuesday</th>
                                    <th>Wednesday</th>
                                    <th>Thursday</th>
                                    <th>Friday</th>
                                    <th>Saturday</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="slot" begin="1" end="6">
                                    <tr>
                                        <td>
                                            <div class="time-slot">
                                                Slot ${slot}
                                                <small>
                                                    <c:choose>
                                                        <c:when test="${slot == 1}">07:00<br>09:00</c:when>
                                                        <c:when test="${slot == 2}">09:15<br>11:15</c:when>
                                                        <c:when test="${slot == 3}">12:30<br>14:30</c:when>
                                                        <c:when test="${slot == 4}">14:45<br>16:45</c:when>
                                                        <c:when test="${slot == 5}">17:00<br>19:00</c:when>
                                                        <c:when test="${slot == 6}">19:15<br>21:15</c:when>
                                                    </c:choose>
                                                </small>
                                            </div>
                                        </td>
                                        <c:forEach var="day" items="${['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']}">
                                            <td>
                                                <c:forEach var="schedule" items="${allSchedules}">
                                                    <c:if test="${schedule.slot == slot}">
                                                        <fmt:formatDate var="dayOfWeek" value="${schedule.learningDate}" pattern="EEEE"/>
                                                        <c:if test="${dayOfWeek == day}">
                                                            <c:set var="colorIndex" value="${schedule.classes.classid % 8}"/>
                                                            <div class="schedule-card color-${colorIndex}">
                                                                <div class="course-code">${schedule.classes.className}</div>
                                                                <div class="course-name">${schedule.classes.course.courseName}</div>
                                                                <small>Teacher: ID ${schedule.classes.employee.employeeId}</small>
                                                                <small>Room: ${schedule.room.roomName}</small>
                                                            </div>
                                                        </c:if>
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                        </c:forEach>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class='bx bx-search-alt'></i>
                    <h4>Search for Schedules</h4>
                    <p>Enter filter criteria and click "Apply Filters" to view schedules,<br>or click "See All Schedule" to view by date.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Add Schedule Modal -->
<div id="addScheduleModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Add New Schedule</h3>
            <button type="button" class="close" onclick="closeAddModal()">&times;</button>
        </div>
        <form action="schedule" method="post" id="addScheduleForm">
            <input type="hidden" name="action" value="create">
            <div class="modal-body">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Subject Code <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="subjectCode" placeholder="e.g. CS101" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Subject Name <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="subjectName" placeholder="e.g. Data Structures" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Teacher <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="teacher" placeholder="e.g. Dr. John Smith" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Room Code <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="roomCode" placeholder="e.g. A201" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Day <span class="text-danger">*</span></label>
                        <select class="form-control" name="day" required>
                            <option value="">Select day...</option>
                            <option value="Monday">Monday</option>
                            <option value="Tuesday">Tuesday</option>
                            <option value="Wednesday">Wednesday</option>
                            <option value="Thursday">Thursday</option>
                            <option value="Friday">Friday</option>
                            <option value="Saturday">Saturday</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Time Slot <span class="text-danger">*</span></label>
                        <select class="form-control" name="slot" required>
                            <option value="">Select slot...</option>
                            <option value="1">Slot 1 (07:00 - 09:00)</option>
                            <option value="2">Slot 2 (09:15 - 11:15)</option>
                            <option value="3">Slot 3 (12:30 - 14:30)</option>
                            <option value="4">Slot 4 (14:45 - 16:45)</option>
                            <option value="5">Slot 5 (17:00 - 19:00)</option>
                            <option value="6">Slot 6 (19:15 - 21:15)</option>
                        </select>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Display Color</label>
                        <div class="color-picker">
                            <div class="color-option selected" style="background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);"
                                 data-color="0" onclick="selectColor(this)"></div>
                            <div class="color-option" style="background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%);"
                                 data-color="1" onclick="selectColor(this)"></div>
                            <div class="color-option" style="background: linear-gradient(135deg, #10b981 0%, #059669 100%);"
                                 data-color="2" onclick="selectColor(this)"></div>
                            <div class="color-option" style="background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);"
                                 data-color="3" onclick="selectColor(this)"></div>
                            <div class="color-option" style="background: linear-gradient(135deg, #ec4899 0%, #db2777 100%);"
                                 data-color="4" onclick="selectColor(this)"></div>
                            <div class="color-option" style="background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);"
                                 data-color="5" onclick="selectColor(this)"></div>
                            <div class="color-option" style="background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);"
                                 data-color="6" onclick="selectColor(this)"></div>
                            <div class="color-option" style="background: linear-gradient(135deg, #14b8a6 0%, #0d9488 100%);"
                                 data-color="7" onclick="selectColor(this)"></div>
                        </div>
                        <input type="hidden" name="color" id="selectedColor" value="0">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeAddModal()">Cancel</button>
                <button type="submit" class="btn btn-primary">Add Schedule</button>
            </div>
        </form>
    </div>
</div>

<script>
    // Data from database (loaded via AJAX)
    let rooms = [];
    let teachers = [];
    let classes = [];

    // Load data on page load and populate select boxes
    fetch('api/schedule?action=rooms')
        .then(r => r.json())
        .then(data => {
            rooms = data;
            populateRoomSelect();
        })
        .catch(e => console.error('Error loading rooms:', e));

    fetch('api/schedule?action=teachers')
        .then(r => r.json())
        .then(data => {
            teachers = data;
            populateTeacherSelect();
        })
        .catch(e => console.error('Error loading teachers:', e));

    fetch('api/schedule?action=classes')
        .then(r => r.json())
        .then(data => {
            classes = data;
            populateSubjectSelect();
            populateModalClassSelect();
        })
        .catch(e => console.error('Error loading classes:', e));

    // Populate filter dropdowns
    function populateRoomSelect() {
        const select = document.getElementById('roomFilter');
        rooms.forEach(room => {
            const option = document.createElement('option');
            option.value = room;
            option.textContent = room;
            select.appendChild(option);
        });
    }

    function populateTeacherSelect() {
        const select = document.getElementById('teacherFilter');
        teachers.forEach(teacher => {
            const option = document.createElement('option');
            option.value = teacher;
            option.textContent = teacher;
            select.appendChild(option);
        });
    }

    function populateSubjectSelect() {
        const select = document.getElementById('subjectFilter');
        classes.forEach(cls => {
            const option = document.createElement('option');
            option.value = cls.code;
            option.textContent = cls.code + ' - ' + cls.name;
            select.appendChild(option);
        });
    }

    // Populate modal class select
    function populateModalClassSelect() {
        const select = document.getElementById('modalClassName');
        if (select && select.tagName === 'SELECT') {
            classes.forEach(cls => {
                const option = document.createElement('option');
                option.value = cls.code;
                option.textContent = cls.code;
                select.appendChild(option);
            });
        }
    }

    // Modal functions
    function openAddModal() {
        // Pre-fill from filter values
        const roomFilter = document.getElementById('roomFilter').value;
        const teacherFilter = document.getElementById('teacherFilter').value;
        const subjectFilter = document.getElementById('subjectFilter').value;

        if (roomFilter) document.getElementById('modalRoomCode').value = roomFilter;
        if (teacherFilter) document.getElementById('modalTeacher').value = teacherFilter;
        if (subjectFilter) {
            const modalClass = document.getElementById('modalClassName');
            if (modalClass.tagName === 'SELECT') {
                modalClass.value = subjectFilter;
            } else {
                modalClass.value = subjectFilter;
            }
            const classObj = classes.find(c => c.code === subjectFilter);
            if (classObj) {
                document.getElementById('modalCourseName').value = classObj.name;
            }
        }

        document.getElementById('addScheduleModal').classList.add('show');
    }

    function closeAddModal() {
        document.getElementById('addScheduleModal').classList.remove('show');
        document.getElementById('addScheduleForm').reset();
        const conflictDiv = document.getElementById('conflictWarning');
        if (conflictDiv) conflictDiv.innerHTML = '';
    }

    function selectColor(element) {
        document.querySelectorAll('.color-option').forEach(el => el.classList.remove('selected'));
        element.classList.add('selected');
        document.getElementById('selectedColor').value = element.getAttribute('data-color');
    }

    // Check room conflict via API
    function checkRoomConflict() {
        const room = document.getElementById('modalRoomCode').value;
        const day = document.getElementById('modalDay').value;
        const slot = document.getElementById('modalSlot').value;

        const conflictDiv = document.getElementById('conflictWarning');
        if (!conflictDiv) return;

        if (!room || !day || !slot) {
            conflictDiv.innerHTML = '';
            return;
        }

        fetch('api/schedule?action=checkConflict&room=' + encodeURIComponent(room) + '&day=' + encodeURIComponent(day) + '&slot=' + slot)
            .then(r => r.json())
            .then(data => {
                if (data.conflict) {
                    conflictDiv.innerHTML = `
                        <div class="conflict-warning">
                            <i class='bx bx-error-circle'></i>
                            <strong>Conflict detected!</strong> ${data.message}
                            <br>Please choose a different room, day, or time slot.
                        </div>
                    `;
                } else {
                    conflictDiv.innerHTML = '';
                }
            })
            .catch(e => {
                console.error('Error checking conflict:', e);
                conflictDiv.innerHTML = '';
            });
    }

    // Validate before submit
    function validateSchedule() {
        const conflictDiv = document.getElementById('conflictWarning');
        if (conflictDiv && conflictDiv.innerHTML.trim() !== '') {
            alert('Cannot create schedule: Room conflict detected!');
            return false;
        }
        return true;
    }

    // Close modal when clicking outside
    window.onclick = function(event) {
        const modal = document.getElementById('addScheduleModal');
        if (event.target == modal) {
            closeAddModal();
        }
    }

    // Filter functionality
    function applyFilters() {
        const roomFilter = document.getElementById('roomFilter').value;
        const teacherFilter = document.getElementById('teacherFilter').value;
        const subjectFilter = document.getElementById('subjectFilter').value;

        if (!roomFilter && !teacherFilter && !subjectFilter) {
            alert('Please select at least one filter criteria');
            return;
        }

        // Build query string
        let query = 'schedule?action=filter';
        if (roomFilter) query += '&room=' + encodeURIComponent(roomFilter);
        if (teacherFilter) query += '&teacher=' + encodeURIComponent(teacherFilter);
        if (subjectFilter) query += '&subject=' + encodeURIComponent(subjectFilter);

        window.location.href = query;
    }

    function clearFilters() {
        window.location.href = 'schedule?action=manage';
    }

    // Preserve filter values on page load
    document.addEventListener('DOMContentLoaded', function() {
        <c:if test="${not empty roomFilter}">
            setTimeout(() => {
                document.getElementById('roomFilter').value = '${roomFilter}';
            }, 500);
        </c:if>
        <c:if test="${not empty teacherFilter}">
            setTimeout(() => {
                document.getElementById('teacherFilter').value = '${teacherFilter}';
            }, 500);
        </c:if>
        <c:if test="${not empty subjectFilter}">
            setTimeout(() => {
                document.getElementById('subjectFilter').value = '${subjectFilter}';
            }, 500);
        </c:if>
    });
</script>

