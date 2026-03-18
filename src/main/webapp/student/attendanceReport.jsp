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
                    Attendance Report
                </li>
            </ol>
        </div>

        <div>
            <h2 class="fw-bold mb-1">Attendance Report</h2>
            <p class="text-muted small mb-0">
                Track your attendance progress for each class.
            </p>
        </div>
    </div>

    <!-- CLASS LIST -->
    <div class="row">

        <c:forEach var="r" items="${attendanceReport}">

            <div class="col-lg-4 col-md-6 col-sm-12 mb-4">

                <div class="card shadow-sm h-100 border-0" 
                     style="border-radius:8px; padding:5px;">

                    <div class="card-body">

                        <!-- CLASS TITLE -->
                        <h5 class="card-title d-flex justify-content-between">
                            ${r[2]}
                            <span class="badge bg-info">
                                ${r[1]}
                            </span>
                        </h5>

                        <!-- COURSE -->
                        <p class="text-muted">
                            Course: ${r[1]}
                        </p>

                        <!-- DATE -->
                        <p>
                            <i class="bx bx-calendar"></i>

                            <fmt:formatDate value="${r[3]}" pattern="dd MMM yyyy"/>

                            -

                            <fmt:formatDate value="${r[4]}" pattern="dd MMM yyyy"/>
                        </p>

                        <!-- ATTENDANCE -->
                        <p>
                            <i class="bx bx-check-circle"></i>

                            Attended: 
                            <strong>${r[6]}/${r[5]}</strong> slots
                        </p>

                        <hr>

                        <!-- RATE -->
                        <h5 class="text-primary">

                            ${r[7]} %

                            <span class="text-muted small">
                                Attendance Rate
                            </span>

                        </h5>

                        <!-- PROGRESS BAR -->
                        <div class="progress mt-2">

                            <div class="progress-bar bg-success"
                                 role="progressbar"
                                 style="width:${r[7]}%">

                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </c:forEach>

    </div>

</div>