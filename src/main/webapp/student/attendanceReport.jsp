<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container-fluid student-course-container">

    <!-- ================= HEADER ================= -->
    <div class="mb-4 mt-4">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item">
                <a href="dashboard"><i class="bx bx-home-alt"></i></a>
            </li>
            <li class="breadcrumb-item active">Attendance Report</li>
        </ol>

        <h2 class="fw-bold">Attendance Report</h2>
    </div>

    <!-- ================= SEARCH + FILTER ================= -->
    <form action="attendance" method="get" class="row mb-4">
        <input type="hidden" name="action" value="studentReport"/>
        <input type="hidden" name="page" value="1"/>
        <input type="hidden" name="view" value="${view}"/>

        <div class="col-md-4">
            <input type="text" name="keyword" class="form-control"
                   placeholder="Search class or course..."
                   value="${keyword != null ? keyword : ''}">
        </div>

        <div class="col-md-3">
            <input type="date" name="fromDate" class="form-control"
                   value="${fromStr != null ? fromStr : ''}">
        </div>

        <div class="col-md-3">
            <input type="date" name="toDate" class="form-control"
                   value="${toStr != null ? toStr : ''}">
        </div>

        <div class="col-md-2">
            <button type="submit"
                    class="btn btn-primary w-100 d-flex justify-content-center align-items-center">
                Search
            </button>
        </div>
    </form>

    <!-- ================= TABLE ================= -->
    <div id="tableView">

        <table class="table table-bordered table-hover">
            <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Class</th>
                    <th>Course</th>
                    <th>Start</th>
                    <th>End</th>
                    <th>Attended</th>
                    <th>Total</th>
                    <th>Rate</th>
                </tr>
            </thead>

            <tbody>

                <!-- NO DATA -->
                <c:if test="${empty attendanceReport}">
                    <tr>
                        <td colspan="8" class="text-center">No data found</td>
                    </tr>
                </c:if>

                <!-- DATA -->
                <c:forEach var="r" items="${attendanceReport}" varStatus="loop">
                    <tr>
                        <!-- FIX INDEX: pageSize = 6 -->
                        <td>
                            ${(currentPage - 1) * 6 + loop.index + 1}
                        </td>

                        <td>${r[2]}</td>
                        <td>${r[1]}</td>

                        <td>
                            <fmt:formatDate value="${r[3]}" pattern="dd/MM/yyyy"/>
                        </td>

                        <td>
                            <fmt:formatDate value="${r[4]}" pattern="dd/MM/yyyy"/>
                        </td>

                        <td>${r[6]}</td>
                        <td>${r[5]}</td>

                        <!-- FIX RATE: tránh chia 0 -->
                        <td>
                            <c:choose>
                                <c:when test="${r[5] > 0}">
                                    <fmt:formatNumber value="${(r[6] * 100) / r[5]}" 
                                                      pattern="#0.00" />%
                                </c:when>
                                <c:otherwise>
                                    0%
                                </c:otherwise>
                            </c:choose>
                        </td>   
                    </tr>
                </c:forEach>

            </tbody>
        </table>

    </div>

    <!-- ================= PAGINATION ================= -->

    <!-- PREVIOUS -->
    <c:url var="pageUrlPrev" value="attendance">
        <c:param name="action" value="studentReport"/>
        <c:param name="page" value="${currentPage - 1}"/>
        <c:param name="view" value="${view}"/>
        <c:param name="keyword" value="${keyword}"/>
        <c:param name="fromDate" value="${fromStr}"/>
        <c:param name="toDate" value="${toStr}"/>
    </c:url>

    <!-- NEXT -->
    <c:url var="pageUrlNext" value="attendance">
        <c:param name="action" value="studentReport"/>
        <c:param name="page" value="${currentPage + 1}"/>
        <c:param name="view" value="${view}"/>
        <c:param name="keyword" value="${keyword}"/>
        <c:param name="fromDate" value="${fromStr}"/>
        <c:param name="toDate" value="${toStr}"/>
    </c:url>

    <div class="d-flex justify-content-center mt-4">

        <c:if test="${currentPage > 1}">
            <a href="${pageUrlPrev}" class="btn btn-outline-primary me-2">
                Previous
            </a>
        </c:if>

        <span class="align-self-center">
            Page ${currentPage} / ${totalPage}
        </span>

        <c:if test="${currentPage < totalPage}">
            <a href="${pageUrlNext}" class="btn btn-outline-primary ms-2">
                Next
            </a>
        </c:if>

    </div>

</div>