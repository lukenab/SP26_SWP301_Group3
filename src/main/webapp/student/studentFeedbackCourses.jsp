<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>

    .feedback-container{
        padding:20px 10px;
    }

    .feedback-card{
        border-radius:18px;
        border:none;
        transition:all 0.3s ease;
    }

    .feedback-card:hover{
        transform:translateY(-6px);
        box-shadow:0 15px 30px rgba(0,0,0,0.12);
    }

    .feedback-title{
        font-size:1.2rem;
        font-weight:600;
        margin-bottom:10px;
    }

    .teacher-name{
        font-size:14px;
        color:#6c757d;
    }

    .feedback-btn{
        border-radius:50px;
        font-weight:500;
        padding:8px 0;
    }

    .empty-box{
        padding:40px;
        border-radius:12px;
    }

    /* DARK MODE */

    body.dark .feedback-card{
        background:#1e1e1e;
        box-shadow:0 10px 25px rgba(0,0,0,0.6);
    }

    body.dark .feedback-title{
        color:#fff;
    }

    body.dark .teacher-name{
        color:#b0b3b8;
    }

</style>


<div class="container-fluid feedback-container">

    <div class="mb-4 mt-4">

        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item">
                    <a href="dashboard">
                        <i class="bx bx-home-alt"></i>
                    </a>
                </li>

                <li class="breadcrumb-item active">
                    Course Feedback
                </li>
            </ol>
        </div>

        <h2 class="fw-bold mb-1">Course Feedback</h2>
        <p class="text-muted small">
            Give feedback for the courses you attended.
        </p>

    </div>


    <div class="row g-4">

        <c:choose>

            <c:when test="${not empty classList}">

                <c:forEach var="c" items="${classList}">

                    <div class="col-lg-4 col-md-6 col-sm-12">

                        <div class="card feedback-card shadow-sm h-100">

                            <div class="card-body d-flex flex-column justify-content-between">

                                <div>

                                    <div class="feedback-title">
                                        ${c[1]}
                                    </div>

                                    <div class="mb-2 text-muted">
                                        Course: ${c[2]}
                                    </div>

                                    <div class="teacher-name mb-4">
                                        Teacher: ${c[3]}
                                    </div>

                                </div>


                                <a href="feedback?action=writeFeedback&enrollmentId=${c[0]}"
                                   class="btn btn-outline-primary feedback-btn w-100"
                                   style="display: flex; align-items: center; justify-content: center; text-align: center; background-color: #0d6efd; color: white; border-color: #0d6efd; cursor: default;">
                                    Give Feedback
                                </a>


                            </div>

                        </div>

                    </div>

                </c:forEach>

            </c:when>


            <c:otherwise>

                <div class="col-12">
                    <div class="alert alert-info text-center empty-box shadow-sm">
                        You have no courses available for feedback.
                    </div>
                </div>

            </c:otherwise>

        </c:choose>

    </div>

</div>