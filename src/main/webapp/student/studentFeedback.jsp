<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>
    <head>

        <title>Write Feedback</title>

        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

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

            /* STAR RATING */

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
                transition:.2s;
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
                font-weight: 500 !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
                transition: none !important;  
                border: 1px solid transparent !important;
            }

            /* BUTTON CANCEL - Màu xám */
            .btn-cancel-gray {
                background-color: #6c757d !important;
                color: white !important;
                border-color: #6c757d !important;
            }

            /* BUTTON SUBMIT - Màu xanh primary */
            .btn-submit-blue {
                background-color: #0d6efd !important;
                color: white !important;
                border-color: #0d6efd !important;
            }

            /* Giữ nguyên màu khi hover */
            .btn-cancel-gray:hover {
                background-color: #6c757d !important;
                color: white !important;
                border-color: #6c757d !important;
                transform: none !important;
            }
            
            .btn-submit-blue:hover {
                background-color: #0d6efd !important;
                color: white !important;
                border-color: #0d6efd !important;
                transform: none !important;
            }

            /* DARK MODE */

            body.dark .feedback-card{
                background:#1e1e1e;
                color:white;
            }

            body.dark textarea{
                background:#2a2a2a;
                color:white;
                border:1px solid #444;
            }

            body.dark .course-meta{
                color:#b0b3b8;
            }

        </style>

    </head>

    <body>

        <div class="container-fluid feedback-container">

            <!-- HEADER + BREADCRUMB -->

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

                <h2 class="fw-bold mb-1">
                    Write Feedback
                </h2>

                <p class="text-muted small">
                    Share your learning experience with this teacher.
                </p>

            </div>


            <!-- MESSAGE -->

            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success">
                    ${sessionScope.success}
                </div>
                <c:remove var="success" scope="session"/>
            </c:if>

            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger">
                    ${sessionScope.error}
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>


            <!-- MAIN CONTENT -->

            <div class="row g-4">

                <!-- COURSE INFO -->

                <div class="col-lg-4">

                    <div class="card feedback-card shadow-sm h-100">

                        <div class="card-body">

                            <div class="course-title mb-3">
                                ${classInfo[1]}
                            </div>

                            <div class="course-meta mb-2">
                                Course: ${classInfo[2]}
                            </div>

                            <div class="course-meta">
                                Teacher: ${classInfo[3]}
                            </div>

                        </div>

                    </div>

                </div>


                <!-- FEEDBACK FORM -->

                <div class="col-lg-8">

                    <div class="card feedback-card shadow-sm">

                        <div class="card-body p-4">

                            <form action="feedback" method="post">

                                <input type="hidden" name="action" value="studentFeedback">
                                <input type="hidden" name="enrollmentId" value="${enrollmentId}"/>

                                <!-- RATING -->

                                <div class="mb-4 text-center">

                                    <label class="form-label fw-semibold mb-3">
                                        Rate this teacher
                                    </label>

                                    <div class="rating-stars">

                                        <input type="radio" name="rating" id="star5" value="5" required>
                                        <label for="star5">★</label>

                                        <input type="radio" name="rating" id="star4" value="4">
                                        <label for="star4">★</label>

                                        <input type="radio" name="rating" id="star3" value="3">
                                        <label for="star3">★</label>

                                        <input type="radio" name="rating" id="star2" value="2">
                                        <label for="star2">★</label>

                                        <input type="radio" name="rating" id="star1" value="1">
                                        <label for="star1">★</label>

                                    </div>

                                </div>


                                <!-- COMMENT -->

                                <div class="mb-4">

                                    <label class="form-label fw-semibold">
                                        Your Feedback
                                    </label>

                                    <textarea
                                        name="comment"
                                        class="form-control"
                                        rows="4"
                                        placeholder="Share your learning experience..."
                                        required></textarea>

                                </div>


                                <!-- BUTTONS -->
                                <div class="d-flex gap-3">

                                    <a href="feedback?action=viewStudentCoursesFeedback"
                                       class="btn btn-custom btn-cancel-gray w-50">
                                        Cancel
                                    </a>

                                    <button type="submit"
                                            class="btn btn-custom btn-submit-blue w-50">
                                        Submit Feedback
                                    </button>

                                </div>

                            </form>

                        </div>

                    </div>

                </div>

            </div>
        </div>

    </body>
</html>