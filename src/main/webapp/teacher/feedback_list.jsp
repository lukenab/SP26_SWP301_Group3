<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet"/>

<div class="container-fluid px-4" id="feedback-page-container" data-pre-class="${classId}">
    <div aria-label="breadcrumb" class="mt-3">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="dashboard" class="text-decoration-none">Dashboard</a></li>

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

    <div class="row g-3" id="feedbackContainer">
        <c:forEach var="f" items="${feedbackList}">
            <div class="col-12 feedback-item" data-class="${f.enrollment.classes.className}" data-rating="${f.rating}">
                <div class="card border-0 shadow-sm border-start border-4 border-primary p-3 feedback-card">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <h6 class="fw-bold text-dark mb-1">${studentNameMap[f.feedbackId]}</h6>
                            <span class="badge bg-light text-primary border mb-2" style="font-size: 0.7rem;">
                                Class: ${f.enrollment.classes.className}
                            </span>
                        </div>
                        <div class="text-warning">
                            <c:forEach begin="1" end="${f.rating}"><i class='bx bxs-star'></i></c:forEach>
                            </div>
                        </div>
                        <div class="p-3 bg-light rounded-3 mt-1">
                            <p class="mb-0 text-secondary fst-italic" style="font-size: 0.9rem;">
                                "${f.comment}"
                        </p>
                    </div>
                    <div class="text-end mt-2">
                        <small class="text-muted" style="font-size: 0.75rem;">
                            <i class='bx bx-calendar'></i> ${f.sentDate.toLocalDate()}
                        </small>
                    </div>
                </div>
            </div>
        </c:forEach>

        <div id="noDataMessage" class="col-12 text-center py-5 d-none">
            <i class='bx bx-search-alt fs-1 text-muted'></i>
            <p class="text-muted mt-2">No feedbacks match your filters.</p>
        </div>
    </div>
</div>

<script src="js/feedbackList.js" type="text/javascript"></script>