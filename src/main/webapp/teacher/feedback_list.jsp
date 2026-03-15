<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet"/>
<link href="css/feedback_list.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4" id="feedback-page-container" data-pre-class="${classId}">

    <div aria-label="breadcrumb" class="mt-3">

        <ol class="breadcrumb mb-1">

            <li class="breadcrumb-item"><a href="dashboard?action=teacher" class="text-decoration-none">Dashboard</a></li>

            <c:choose>            

                <c:when test="${not empty classId and from == 'myClasses'}">

                    <li class="breadcrumb-item">

                        <a href="student?action=viewByClass&classId=${classId}" class="text-decoration-none">

                            My Classes

                        </a>

                    </li>

                    <li class="breadcrumb-item active">Student Feedbacks</li>

                </c:when>

                <c:otherwise>

                    <li class="breadcrumb-item active">My Feedback</li>

                </c:otherwise>

            </c:choose>

        </ol>

    </div>

    <div class="mb-4">

        <h2 class="fw-bold text-dark mb-1">Student Feedbacks</h2>

        <p class="text-muted small">Direct insights from your classroom performance</p>

    </div>


    <div class="card shadow-sm border-0 mb-4 bg-white">

        <div class="card-body p-3">

            <div class="d-flex align-items-center gap-3">

                <span class="text-secondary small fw-bold"><i class='bx bx-filter-alt'></i> Filter by:</span>



                <select id="classFilter" class="form-select form-select-sm" style="width: 180px;">

                    <option value="all">All Classes</option>

                    <c:forEach var="c" items="${classList}">

                        <option value="${c.className}">${c.className}</option>

                    </c:forEach>

                </select>



                <div class="btn-group shadow-sm" id="ratingFilter">

                    <button class="btn btn-sm btn-primary active" data-rating="all">All</button>

                    <button class="btn btn-sm btn-outline-primary" data-rating="5">5 Stars</button>

                    <button class="btn btn-sm btn-outline-primary" data-rating="4">4 Stars</button>

                    <button class="btn btn-sm btn-outline-primary" data-rating="3">3 Stars</button>

                    <button class="btn btn-sm btn-outline-primary" data-rating="2">2 Stars</button>

                    <button class="btn btn-sm btn-outline-primary" data-rating="1">1 Star</button>

                </div>

            </div>

        </div>

    </div>



    <div class="card user-table-card border-0 shadow-sm bg-white rounded-3">
        <div class="table-responsive">
            <table class="table mb-0 align-middle table-hover">
                <thead class="bg-light text-muted small text-uppercase">
                    <tr>
                        <th class="text-center" style="width:60px">#</th>
                        <th>Student Information</th>
                        <th class="text-center">Class</th>
                        <th class="text-center">Rating</th>
                        <th>Comment</th>
                        <th class="text-center" style="width:130px">Date</th>
                    </tr>
                </thead>

                <tbody id="feedbackContainer">
                    <c:forEach var="f" items="${feedbackList}" varStatus="loop">
                        <tr class="feedback-item" data-class="${f.enrollment.classes.className}" data-rating="${f.rating}">
                            <td class="text-center text-muted small">${loop.count}</td>

                            <td style="width: 220px">
                                <div class="d-flex align-items-center" >
                                    <div class="avatar-container me-3">
                                        <c:choose>
                                            <c:when test="${not empty avatarMap[f.feedbackId]}">
                                                <img src="${pageContext.request.contextPath}/${avatarMap[f.feedbackId]}" 
                                                     class="avatar-img" 
                                                     alt="Student"
                                                     onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">

                                                <div class="avatar-placeholder" style="display: none;">
                                                    <i class='bx bx-user'></i>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="avatar-placeholder">
                                                    <i class='bx bx-user'></i>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div>
                                        <div class="fw-bold text-dark">${studentNameMap[f.feedbackId]}</div>
                                        <!--                                        <div class="small text-muted" style="font-size: 0.7rem;">
                                                                                    ID: ${f.enrollment.student.userId}
                                                                                </div>-->
                                    </div>
                                </div>
                            </td>

                            <td>
                                <span class="badge bg-soft-primary text-primary border px-2 py-1">
                                    ${f.enrollment.classes.className}
                                </span>
                            </td>

                            <td class="text-warning" style="white-space: nowrap;">
                                <c:forEach begin="1" end="${f.rating}">
                                    <i class='bx bxs-star'></i>
                                </c:forEach>
                            </td>

                            <td>
                                <p class="mb-0 text-secondary fst-italic small">
                                    "${f.comment}"
                                </p>
                            </td>

                            <td class="text-center text-muted small">
                                <i class='bx bx-calendar'></i> ${f.sentDate.toLocalDate()}
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div id="noDataMessage" class="text-center py-5 d-none">
            <i class='bx bx-search-alt fs-1 text-muted'></i>
            <p class="text-muted mt-2">No feedbacks match your filters.</p>
        </div>
    </div>

</div>

<script src="js/feedbackList.js" type="text/javascript"></script>