<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container-fluid student-course-container">

    <!-- BREADCRUMB -->
    <div class="mb-4 mt-4">

        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item">
                    <a href="dashboard">
                        <i class="bx bx-home-alt"></i>
                    </a>
                </li>
                <li class="breadcrumb-item active">
                    Register Class
                </li>
            </ol>
        </div>

        <div>
            <h2 class="fw-bold mb-1">Available Classes</h2>
            <p class="text-muted small mb-0">
                Browse available classes and register for the course you want.
            </p>
        </div>

    </div>


    <!-- CLASS CARDS -->
    <div class="row">

        <c:forEach var="c" items="${classList}">

            <div class="col-lg-4 col-md-6 col-sm-12 mb-4">

                <div class="card shadow-sm h-100">

                    <div class="card-body">

                        <!-- STATUS -->
                        <span class="badge bg-success mb-2">
                            Open for Enrollment
                        </span>

                        <!-- CLASS NAME -->
                        <h5 class="card-title">
                            Class: ${c[1]}
                        </h5>

                        <!-- COURSE -->
                        <p class="text-muted">
                            Course: ${c[2]}
                        </p>

                        <!-- TEACHER -->
                        <p>
                            <i class="bx bx-user"></i>
                            ${c[3]}
                        </p>

                        <!-- SCHEDULE -->
                        <p>
                            <i class="bx bx-calendar"></i>
                            ${c[8]}
                            |
                            <i class="bx bx-time"></i>
                            ${c[9]}
                        </p>

                        <!-- COURSE DATE -->
                        <!--                        <p>
                                                    <i class="bx bx-calendar-event"></i>
                        <fmt:formatDate value="${c[4]}" pattern="dd MMM yyyy"/>
                        -
                        <fmt:formatDate value="${c[5]}" pattern="dd MMM yyyy"/>
                    </p>-->

                        <!-- STUDENT COUNT -->
                        <p>
                            ${c[6]} Students
                        </p>

                        <hr>

                        <!-- PRICE -->
                        <h5 class="text-primary">
                            <fmt:formatNumber value="${c[7]}" type="number"/> VND
                        </h5>

                        <!-- BUTTON -->
                        <div class="d-flex justify-content-between mt-3">

                            <a href="class?action=viewDetail&classId=${c[0]}"
                               class="btn btn-outline-secondary"
                               style="background-color: #6c757d; color: white; border-color: #6c757d; pointer-events: none; cursor: default;">
                                View Details
                            </a>

                            <a href="payment?action=review&classId=${c[0]}&className=${c[1]}&amount=${c[7]}" class="btn btn-primary">
                                Register Now
                            </a>

                        </div>

                    </div>

                </div>

            </div>

        </c:forEach>

    </div>

</div>