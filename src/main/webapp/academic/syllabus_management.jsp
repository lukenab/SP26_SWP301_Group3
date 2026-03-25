<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/course_list.css" rel="stylesheet" type="text/css"/>
<link href="css/syllabus_management.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body syllabus-page">
    <c:url var="syllabusManageReturnUrl" value="syllabus">
        <c:param name="action" value="manage"/>
        <c:param name="page" value="${currentPage}"/>
    </c:url>
    <c:url var="addSyllabusUrl" value="syllabus">
        <c:param name="action" value="add"/>
        <c:param name="returnUrl" value="${syllabusManageReturnUrl}"/>
    </c:url>
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Manage Syllabus</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Manage Syllabus</h2>
                <p class="text-muted small mb-0">Manage session order, topic, and lesson description by course.</p>
            </div>
            <div class="d-flex gap-2">
                <a href="${addSyllabusUrl}" class="btn btn-add-new">
                    <i class='bx bx-plus'></i> Add Syllabus
                </a>
                <a href="course?action=all" class="btn syllabus-btn-back">
                    <i class='bx bx-book-content'></i> Back to Courses
                </a>
            </div>
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
                        <i class='bx bx-error-circle'></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="toast-content">
                <span class="toast-title">${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}</span>
                <span class="toast-message">${sessionScope.message}</span>
            </div>
            <button class="toast-close" onclick="closeToast()">
                <i class='bx bx-x'></i>
            </button>
        </div>
        <c:remove var="message" scope="session"/>
        <c:remove var="messageType" scope="session"/>
    </c:if>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 6%">#</th>
                        <th style="width: 20%">Course</th>
                        <th style="width: 10%">Session No.</th>
                        <th style="width: 22%">Topic Name</th>
                        <th style="width: 32%">Description</th>
                        <th style="width: 8%">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty syllabusList}">
                        <tr>
                            <td colspan="6" class="text-center text-muted py-4">No syllabus found.</td>
                        </tr>
                    </c:if>

                    <c:forEach items="${syllabusList}" var="s" varStatus="loop">
                        <tr>
                            <c:url var="editSyllabusUrl" value="syllabus">
                                <c:param name="action" value="edit"/>
                                <c:param name="syllabusId" value="${s.syllabusId}"/>
                                <c:param name="returnUrl" value="${syllabusManageReturnUrl}"/>
                            </c:url>
                            <td>${startItem + loop.index}</td>
                            <td class="course-cell">${s.courseName}</td>
                            <td><span class="badge-soft">Session ${s.orderIndex}</span></td>
                            <td class="topic-cell">${s.topicName}</td>
                            <td><div class="learning-path-preview">${s.description}</div></td>
                            <td class="actions-cell">
                                <div class="table-actions">
                                    <a href="${editSyllabusUrl}" class="action-btn" title="Update Syllabus">
                                        <i class='bx bx-edit'></i>
                                    </a>
                                    <form action="syllabus" method="post" class="d-inline" onsubmit="return confirm('Delete this syllabus item?');">
                                        <input type="hidden" name="action" value="delete"/>
                                        <input type="hidden" name="syllabusId" value="${s.syllabusId}"/>
                                        <input type="hidden" name="returnUrl" value="${syllabusManageReturnUrl}"/>
                                        <button type="submit" class="action-btn action-btn-plain delete" title="Delete Syllabus">
                                            <i class='bx bx-trash'></i>
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="d-flex justify-content-between align-items-center p-3 border-top">
            <div class="text-muted small">Showing ${startItem}-${endItem} of ${totalItems} syllabus records</div>
            <div>
                <ul class="pagination pagination-sm mb-0">
                    <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                        <a class="page-link" href="syllabus?action=manage&page=${currentPage - 1}">
                            <i class='bx bx-chevron-left'></i> Previous
                        </a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                        <li class="page-item ${pageNumber == currentPage ? 'active' : ''}">
                            <a class="page-link" href="syllabus?action=manage&page=${pageNumber}">${pageNumber}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="syllabus?action=manage&page=${currentPage + 1}">
                            Next <i class='bx bx-chevron-right'></i>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>
