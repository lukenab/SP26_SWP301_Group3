<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Write Feedback</title>

        <!-- Bootstrap -->
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

        <!-- Boxicons -->
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>

        <style>
            .feedback-container{
                padding:20px 10px;
            }

            .feedback-card{
                border-radius:18px;
                border:none;
                transition:all .3s ease;
            }

            .feedback-card:hover{
                transform:translateY(-4px);
                box-shadow:0 12px 25px rgba(0,0,0,0.12);
            }

            .course-title{
                font-size:1.25rem;
                font-weight:600;
            }

            .course-meta{
                font-size:14px;
                color:#6c757d;
            }

            .rating-stars{
                display:flex;
                flex-direction:row-reverse;
                justify-content:center;
                gap:6px;
            }

            .rating-stars input{
                display:none;
            }

            .rating-stars label{
                font-size:34px;
                color:#ddd;
                cursor:pointer;
            }

            .rating-stars input:checked ~ label{
                color:#ffc107;
            }

            .rating-stars label:hover,
            .rating-stars label:hover ~ label{
                color:#ffc107;
            }

            textarea{
                resize:none;
                border-radius:10px;
            }

            .btn-custom {
                border-radius: 4px !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
            }

            .btn-cancel-gray {
                background-color: #6c757d !important;
                color: white !important;
            }

            .btn-submit-blue {
                background-color: #0d6efd !important;
                color: white !important;
            }

            /* breadcrumb đẹp hơn */
            .breadcrumb-item a {
                text-decoration: none;
                color: #0d6efd;
                font-weight: 500;
            }

            .breadcrumb-item.active {
                color: #6c757d;
            }

            /* TOAST */
            .custom-toast {
                position: fixed;
                top: 80px;
                right: 24px;
                background: #f8f9fa;
                border-left: 6px solid #28a745;
                border-radius: 10px;
                padding: 16px 18px;
                display: flex;
                align-items: center;
                gap: 14px;
                min-width: 340px;
                max-width: 420px;
                box-shadow: 0 8px 20px rgba(0,0,0,0.08);
                z-index: 9999;
            }

            /* ERROR */
            .toast-error {
                border-left: 6px solid #dc3545;
            }

            /* ICON */
            .toast-icon {
                width: 36px;
                height: 36px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .toast-success .toast-icon {
                background: rgba(40, 167, 69, 0.1);
                color: #28a745;
            }

            .toast-error .toast-icon {
                background: rgba(220, 53, 69, 0.1);
                color: #dc3545;
            }

            .toast-icon i {
                font-size: 20px;
            }

            /* CONTENT */
            .toast-content {
                flex: 1;
            }

            .toast-title {
                font-weight: 600;
                font-size: 15px;
                color: #212529;
            }

            .toast-message {
                font-size: 14px;
                color: #6c757d;
            }

            /* CLOSE BUTTON */
            .toast-close {
                border: none;
                background: transparent;
                font-size: 18px;
                color: #6c757d;
                cursor: pointer;
            }

            .toast-close:hover {
                color: #000;
            }
        </style>
    </head>

    <body>

        <div class="container-fluid feedback-container">

            <div class="mb-4 mt-4">

                <div aria-label="breadcrumb">
                    <ol class="breadcrumb mb-1">

                        <li class="breadcrumb-item">
                            <a href="dashboard">
                                <i class="bx bx-home-alt"></i>
                            </a>
                        </li>

                        <li class="breadcrumb-item">
                            <a href="feedback?action=viewStudentCoursesFeedback">
                                Course Feedback
                            </a>
                        </li>

                        <li class="breadcrumb-item active">
                            Write Feedback
                        </li>

                    </ol>
                </div>

                <h2 class="fw-bold mb-1">Write Feedback</h2>

                <p class="text-muted small">
                    Share your learning experience with this teacher.
                </p>

            </div>

            <!-- TOAST -->
            <c:if test="${not empty sessionScope.message}">
                <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">

                    <div class="toast-icon">
                        <c:choose>
                            <c:when test="${sessionScope.messageType == 'success'}">
                                <i class='bx bx-check'></i>
                            </c:when>
                            <c:otherwise>
                                <i class='bx bx-x'></i>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="toast-content">
                        <span class="toast-title">
                            ${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}
                        </span>
                        <span class="toast-message">
                            ${sessionScope.message}
                        </span>
                    </div>

                    <button class="toast-close" onclick="closeToast()">×</button>
                </div>

                <c:remove var="message" scope="session"/>
                <c:remove var="messageType" scope="session"/>
            </c:if>

            <div class="row g-4">

                <!-- LEFT -->
                <div class="col-lg-4">
                    <div class="card feedback-card shadow-sm h-100">
                        <div class="card-body">

                            <div class="course-title mb-3">${classInfo[1]}</div>
                            <div class="course-meta mb-2">Course: ${classInfo[2]}</div>
                            <div class="course-meta">Teacher: ${classInfo[3]}</div>

                            <c:if test="${not empty feedback}">
                                <hr>

                                <div class="mt-3">
                                    <div class="fw-semibold mb-2 text-primary">
                                        Your Feedback
                                    </div>

                                    <div class="mb-2">
                                        <strong>Rating:</strong> 
                                        <span class="text-warning">
                                            ${feedback[1]} ★
                                        </span>
                                    </div>

                                    <div class="mb-2">
                                        <strong>Comment:</strong>
                                        <div class="small">${feedback[2]}</div>
                                    </div>

                                    <div class="text-muted small">
                                        <fmt:formatDate value="${feedback[3]}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </div>

                                    <div class="mt-3">
                                        <button type="button"
                                                class="btn btn-sm btn-outline-primary"
                                                onclick="enableEdit()">
                                            Edit Feedback
                                        </button>
                                    </div>
                                </div>
                            </c:if>

                        </div>
                    </div>
                </div>

                <!-- RIGHT -->
                <div class="col-lg-8">
                    <div class="card feedback-card shadow-sm">
                        <div class="card-body p-4">

                            <form id="feedbackForm" action="feedback" method="post">

                                <input type="hidden" name="action"
                                       value="${empty feedback ? 'studentFeedback' : 'updateFeedback'}"/>

                                <input type="hidden" name="enrollmentId" value="${enrollmentId}"/>

                                <c:if test="${not empty feedback}">
                                    <input type="hidden" name="feedbackId" value="${feedback[0]}"/>
                                </c:if>

                                <div class="mb-4 text-center">
                                    <label class="form-label fw-semibold mb-3">
                                        Rate this teacher
                                    </label>

                                    <div class="rating-stars">

                                        <input type="radio" name="rating" id="star5" value="5"
                                               ${feedback[1] == 5 ? 'checked' : ''}
                                               ${not empty feedback ? 'disabled' : ''}>
                                        <label for="star5">★</label>

                                        <input type="radio" name="rating" id="star4" value="4"
                                               ${feedback[1] == 4 ? 'checked' : ''}
                                               ${not empty feedback ? 'disabled' : ''}>
                                        <label for="star4">★</label>

                                        <input type="radio" name="rating" id="star3" value="3"
                                               ${feedback[1] == 3 ? 'checked' : ''}
                                               ${not empty feedback ? 'disabled' : ''}>
                                        <label for="star3">★</label>

                                        <input type="radio" name="rating" id="star2" value="2"
                                               ${feedback[1] == 2 ? 'checked' : ''}
                                               ${not empty feedback ? 'disabled' : ''}>
                                        <label for="star2">★</label>

                                        <input type="radio" name="rating" id="star1" value="1"
                                               ${feedback[1] == 1 ? 'checked' : ''}
                                               ${not empty feedback ? 'disabled' : ''}>
                                        <label for="star1">★</label>

                                    </div>
                                </div>

                                <div class="mb-4">
                                    <label class="form-label fw-semibold">
                                        Your Feedback
                                    </label>

                                    <textarea id="commentBox"
                                              name="comment"
                                              class="form-control"
                                              rows="4"
                                              required
                                              ${not empty feedback ? "disabled" : ""}>${feedback[2]}</textarea>
                                </div>

                                <div class="d-flex gap-3">
                                    <a href="feedback?action=viewStudentCoursesFeedback"
                                       class="btn btn-custom btn-cancel-gray w-50">
                                        Cancel
                                    </a>

                                    <button type="submit"
                                            id="submitBtn"
                                            class="btn btn-custom btn-submit-blue w-50"
                                            ${not empty feedback ? "disabled" : ""}>
                                        ${empty feedback ? 'Submit Feedback' : 'Update Feedback'}
                                    </button>
                                </div>

                            </form>

                        </div>
                    </div>
                </div>

            </div>
        </div>

        <script>
            function enableEdit() {
                document.getElementById("commentBox").disabled = false;

                document.querySelectorAll('input[name="rating"]').forEach(el => {
                    el.disabled = false;
                });

                document.getElementById("submitBtn").disabled = false;

                document.querySelector('input[name="action"]').value = "updateFeedback";
            }

            function closeToast() {
                const toast = document.getElementById("toastMessage");
                if (toast)
                    toast.remove();
            }

            setTimeout(() => {
                closeToast();
            }, 5000);
        </script>

    </body>
</html>