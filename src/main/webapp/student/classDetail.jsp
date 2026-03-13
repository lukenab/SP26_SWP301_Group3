<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>

    .page-title{
        font-weight:700;
    }

    .card-custom{
        border-radius:14px;
        border:none;
        box-shadow:0 4px 12px rgba(0,0,0,0.08);
    }

    .status-badge{
        background:#d4edda;
        color:#198754;
        padding:6px 14px;
        border-radius:20px;
        font-size:13px;
    }

    .info-box{
        display:flex;
        align-items:center;
        gap:10px;
    }

    .info-icon{
        width:40px;
        height:40px;
        border-radius:10px;
        display:flex;
        align-items:center;
        justify-content:center;
        background:#eef2ff;
        font-weight:bold;
    }

    .teacher-avatar{
        width:60px;
        height:60px;
        border-radius:50%;
        object-fit:cover;
    }

</style>

<div class="container-fluid">

    <!-- BREADCRUMB -->
    <div class="mb-4 mt-4">

        <ol class="breadcrumb mb-1">

            <li class="breadcrumb-item">
                <a href="dashboard">
                    <i class="bx bx-home-alt"></i>
                </a>
            </li>

            <li class="breadcrumb-item">
                <a href="class?action=${sourcePage}">
                    <c:choose>
                        <c:when test="${sourcePage == 'myClasses'}">
                            My Classes
                        </c:when>
                        <c:otherwise>
                            Register Class
                        </c:otherwise>
                    </c:choose>
                </a>
            </li>

            <li class="breadcrumb-item active">
                Class Details
            </li>

        </ol>

        <h2 class="fw-bold mb-1">Class Details</h2>
        <p class="text-muted small mb-0">
            View detailed information about this class before registering.
        </p>

    </div>


    <div class="row g-4">

        <!-- LEFT COLUMN -->
        <div class="col-lg-8">

            <!-- CLASS OVERVIEW -->
            <div class="card card-custom mb-4">

                <div class="card-body">

                    <div class="d-flex justify-content-between align-items-center">

                        <div>

                            <h4 class="fw-bold">
                                ${classDetail.className}
                            </h4>

                            <p class="text-muted mb-1">
                                ${classDetail.course.courseName}
                            </p>

                            <p class="text-muted">
                                ${classDetail.course.description}
                            </p>

                        </div>

                        <span class="status-badge">
                            Open for Enrollment
                        </span>

                    </div>

                    <div class="row mt-4">

                        <!-- PRICE -->
                        <div class="col-md-6">

                            <div class="info-box">

                                <div class="info-icon">
                                    $
                                </div>

                                <div>

                                    <small class="text-muted">Price</small>

                                    <div class="fw-bold">
                                        <fmt:formatNumber
                                            value="${classDetail.course.tuitionFee}"
                                            type="number"/> VND
                                    </div>

                                </div>

                            </div>

                        </div>

                        <!-- ROOM -->
                        <div class="col-md-6">

                            <div class="info-box">

                                <div class="info-icon">
                                    🏫
                                </div>

                                <div>

                                    <small class="text-muted">Room</small>

                                    <div class="fw-bold">
                                        <c:choose>
                                            <c:when test="${not empty roomName}">
                                                ${roomName}
                                            </c:when>
                                            <c:otherwise>
                                                Not assigned
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                </div>

                            </div>

                        </div>

                    </div>

                </div>

            </div>


            <!-- TEACHER INFO -->
            <div class="card card-custom mb-4">

                <div class="card-body">

                    <h5 class="mb-3">
                        Teacher Information
                    </h5>

                    <div class="d-flex align-items-center">

                        <img src="images/teacher-avatar.png"
                             class="teacher-avatar me-3"/>

                        <div>

                            <h6 class="mb-1">
                                ${teacherName}
                            </h6>

                            <c:if test="${not empty classDetail.employee}">

                                <p class="text-muted mb-1">
                                    ${classDetail.employee.education}
                                </p>

                                <small class="text-muted">
                                    ${classDetail.employee.experience}
                                </small>

                            </c:if>

                        </div>

                    </div>

                </div>

            </div>


            <!-- CLASS SCHEDULE -->
            <div class="card card-custom mb-4">

                <div class="card-body">

                    <h5 class="mb-3">
                        Class Schedule
                    </h5>

                    <table class="table align-middle">

                        <thead class="table-light">

                            <tr>
                                <th>Date</th>
                                <th>Slot</th>
                                <th>Time</th>
                            </tr>

                        </thead>

                        <tbody>

                            <c:if test="${not empty scheduleList}">
                                <c:forEach var="s" items="${scheduleList}">

                                    <tr>

                                        <td>
                                            <fmt:formatDate
                                                value="${s.learningDate}"
                                                pattern="dd/MM/yyyy"/>
                                        </td>

                                        <td>
                                            ${s.slot.slotID}
                                        </td>

                                        <td>
                                            ${s.slot.startTime} - ${s.slot.endTime}
                                        </td>



                                    </tr>

                                </c:forEach>
                            </c:if>

                            <c:if test="${empty scheduleList}">
                                <tr>
                                    <td colspan="4" class="text-center text-muted">
                                        No schedule available
                                    </td>
                                </tr>
                            </c:if>

                        </tbody>

                    </table>

                </div>

            </div>


            <!-- COURSE DESCRIPTION -->
            <div class="card card-custom">

                <div class="card-body">

                    <h5>
                        Course Description
                    </h5>

                    <p class="text-muted">
                        ${classDetail.course.description}
                    </p>

                </div>

            </div>

        </div>


        <!-- RIGHT COLUMN -->
        <div class="col-lg-4">

            <div class="card card-custom">

                <div class="card-body">

                    <h5 class="mb-3">
                        Enrollment
                    </h5>

                    <a href="payment?action=review&classId=${classDetail.classid}&className=${classDetail.className}&amount=${classDetail.course.tuitionFee}"
                       class="btn btn-primary w-100 mb-2">
                        Register Now
                    </a>

                    <a href="class?action=availableClass"
                       class="btn btn-light w-100">
                        Back to Classes
                    </a>

                </div>

            </div>

        </div>

    </div>

</div>