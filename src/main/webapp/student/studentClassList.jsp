<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container-fluid student-course-container">

    <!-- ================= HEADER + BREADCRUMB ================= -->
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
                Browse available classes and register
            </p>
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
                        <i class='bx bx-cross-circle'></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="toast-content">
                <span class="toast-title">
                    ${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}
                </span>
                <span class="toast-message">${sessionScope.message}</span>
            </div>
            <button class="toast-close" onclick="closeToast()">
                <i class='bx bx-x'></i>
            </button>
        </div>

        <c:remove var="message" scope="session" />
        <c:remove var="messageType" scope="session" />
    </c:if>

    <!-- ================= SEARCH + FILTER ================= -->
    <form action="class" method="get" class="row mb-4">

        <input type="hidden" name="action" value="availableClass"/>

        <!-- SEARCH -->
        <div class="col-md-4">
            <input type="text" name="keyword" class="form-control"
                   placeholder="Search class..."
                   value="${keyword}">
        </div>

        <!-- STATUS -->
        <!--        <div class="col-md-2">
                    <select name="status" class="form-control">
                        <option value="">All Status</option>
                        <option value="Active" ${status == 'Active' ? 'selected' : ''}>Active</option>
                        <option value="Inactive" ${status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>-->

        <!-- FROM DATE -->
        <div class="col-md-3">
            <input type="date" name="fromDate" class="form-control"
                   value="${fromDate}">
        </div>

        <!-- TO DATE -->
        <div class="col-md-3">
            <input type="date" name="toDate" class="form-control"
                   value="${toDate}">
        </div>

        <!-- BUTTON -->
        <div class="col-md-2">
            <button type="submit"
                    class="btn btn-primary w-100 d-flex justify-content-center align-items-center">
                Filter
            </button>
        </div>

    </form>

    <!-- ================= CLASS LIST (GIỮ NGUYÊN UI) ================= -->
    <div class="row">

        <c:forEach var="c" items="${classList}">

            <div class="col-lg-4 col-md-6 col-sm-12 mb-4">
                <div class="card shadow-sm h-100 border-0" style="border-radius: 8px; padding: 5px;">

                    <div class="card-body">

                        <h5 class="card-title d-flex justify-content-between">
                            Class: ${c[1]}
                            <span class="badge bg-success">Open</span>
                        </h5>

                        <p class="text-muted">Course: ${c[2]}</p>

                        <p>
                            <i class="bx bx-user"></i>
                            Instructor: ${c[3]}
                        </p>

                        <p>
                            <c:choose>
                                <c:when test="${not empty c[8]}">
                                    <i class="bx bx-calendar"></i>
                                    ${c[8]} |
                                    <i class="bx bx-time"></i>
                                    ${c[9]}
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">No schedule</span>
                                </c:otherwise>
                            </c:choose>
                        </p>

                        <p>
                            <fmt:formatDate value="${c[4]}" pattern="dd/MM/yyyy"/>
                            -
                            <fmt:formatDate value="${c[5]}" pattern="dd/MM/yyyy"/>
                        </p>

                        <p>${c[6]} Students</p>

                        <hr>

                        <h5 class="text-primary">
                            <fmt:formatNumber value="${c[7]}" type="number"/> VND
                        </h5>

                        <div class="d-flex justify-content-between mt-3">

                            <a href="class?action=detail&classId=${c[0]}"
                               class="btn btn-secondary">
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

    <!-- ================= PAGINATION (FIX %20 BUG) ================= -->
    <div class="d-flex justify-content-center mt-4">

        <!-- PREVIOUS -->
        <c:if test="${currentPage > 1}">
            <c:url var="prevUrl" value="class">
                <c:param name="action" value="availableClass"/>
                <c:param name="page" value="${currentPage - 1}"/>
                <c:param name="keyword" value="${keyword}"/>
                <c:param name="status" value="${status}"/>
                <c:param name="fromDate" value="${fromDate}"/>
                <c:param name="toDate" value="${toDate}"/>
            </c:url>

            <a href="${prevUrl}" class="btn btn-outline-primary me-2">Previous</a>
        </c:if>

        <span class="align-self-center">Page ${currentPage}</span>

        <!-- NEXT -->
        <c:if test="${classList.size() == 6}">
            <c:url var="nextUrl" value="class">
                <c:param name="action" value="availableClass"/>
                <c:param name="page" value="${currentPage + 1}"/>
                <c:param name="keyword" value="${keyword}"/>
                <c:param name="status" value="${status}"/>
                <c:param name="fromDate" value="${fromDate}"/>
                <c:param name="toDate" value="${toDate}"/>
            </c:url>

            <a href="${nextUrl}" class="btn btn-outline-primary ms-2">Next</a>
        </c:if>

    </div>

</div>  