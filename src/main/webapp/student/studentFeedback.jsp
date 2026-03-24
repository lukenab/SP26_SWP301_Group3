<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>

<html>
<head>
    <title>Write Feedback</title>

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

    <style>
        .feedback-container{ padding:20px 10px; }
        .feedback-card{
            border-radius:18px;
            border:none;
            transition:all .3s ease;
        }
        .feedback-card:hover{
            transform:translateY(-4px);
            box-shadow:0 12px 25px rgba(0,0,0,0.12);
        }
        .course-title{ font-size:1.25rem; font-weight:600; }
        .course-meta{ font-size:14px; color:#6c757d; }

        .rating-stars{
            display:flex;
            flex-direction:row-reverse;
            justify-content:center;
            gap:6px;
        }
        .rating-stars input{ display:none; }
        .rating-stars label{
            font-size:34px;
            color:#ddd;
            cursor:pointer;
        }
        .rating-stars input:checked ~ label{ color:#ffc107; }
        .rating-stars label:hover,
        .rating-stars label:hover ~ label{ color:#ffc107; }

        textarea{ resize:none; border-radius:10px; }

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
    </style>
</head>

<body>

<div class="container-fluid feedback-container">

    <!-- HEADER -->
    <div class="mb-4 mt-4">
        <h2 class="fw-bold mb-1">Write Feedback</h2>
    </div>

    <!-- MESSAGE -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
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

                            <!-- ✅ ADDED: Edit button -->
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

                        <!-- ✅ ADDED: dynamic action -->
                        <input type="hidden" name="action"
                               value="${empty feedback ? 'studentFeedback' : 'updateFeedback'}"/>

                        <input type="hidden" name="enrollmentId" value="${enrollmentId}"/>

                        <!-- ✅ ADDED: feedbackId -->
                        <c:if test="${not empty feedback}">
                            <input type="hidden" name="feedbackId" value="${feedback[0]}"/>
                        </c:if>

                        <!-- RATING -->
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

                        <!-- COMMENT -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold">
                                Your Feedback
                            </label>

                            <textarea
                                id="commentBox"
                                name="comment"
                                class="form-control"
                                rows="4"
                                placeholder="Share your learning experience..."
                                required
                                ${not empty feedback ? "disabled" : ""}>${feedback[2]}</textarea>
                        </div>

                        <!-- BUTTON -->
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

    // enable textarea
    document.getElementById("commentBox").disabled = false;

    // enable rating
    document.querySelectorAll('input[name="rating"]').forEach(el => {
        el.disabled = false;
    });

    // enable button
    document.getElementById("submitBtn").disabled = false;

    // đổi action
    document.querySelector('input[name="action"]').value = "updateFeedback";
}
</script>

</body>
</html>