<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet"/>


<c:if test="${not empty sessionScope.message}">
    <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
        <div class="toast-content">
            <span class="toast-title">
                ${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}
            </span>
            <span class="toast-message">
                ${sessionScope.message}
            </span>
        </div>
    </div>

    <c:remove var="message" scope="session"/>
    <c:remove var="messageType" scope="session"/>
</c:if>

<div class="container-fluid px-4 content-body">


    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item">
                    <a href="student?action=viewByClass&classId=${classId}">
                        Student List
                    </a>
                </li>
                <li class="breadcrumb-item active">Grade Management</li>
            </ol>
        </div>

        <div class="content-header">
            <div>
                <h2 class="page-title">
                    ${scoreMap == null ? "Enter Grade" : "Edit Grade"}
                </h2>
                <p class="text-muted small mb-0">
                    Manage student skill scores
                </p>
            </div>

            <a href="student?action=viewByClass&classId=${classId}"
               class="btn btn-add-new">
                Back
            </a>
        </div>
    </div>


    <div class="card user-table-card border-0 bg-white">
        <div class="card-body p-5">

            <form method="post" action="grade">

                <input type="hidden" name="action" value="save"/>
                <input type="hidden" name="studentId" value="${studentId}"/>
                <input type="hidden" name="classId" value="${classId}"/>

                <!-- STUDENT INFO -->
                <div class="row mb-4">
                    <div class="col-md-6">
                        <label class="form-label small text-muted">
                            Student Name
                        </label>
                        <input class="form-control bg-light"
                               value="${studentName}" readonly/>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label small text-muted">
                            Class Name
                        </label>
                        <input class="form-control bg-light"
                               value="${className}" readonly/>
                    </div>
                </div>

                <hr/>


                <div class="row g-4 mt-2">

                    <div class="col-md-3">
                        <label class="form-label fw-semibold">Reading</label>
                        <input type="number"
                               name="reading"
                               class="form-control"
                               step="0.1"
                               min="0"
                               max="10"
                               value="${scoreMap['Reading']}"
                               required/>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-semibold">Writing</label>
                        <input type="number"
                               name="writing"
                               class="form-control"
                               step="0.1"
                               min="0"
                               max="10"
                               value="${scoreMap['Writing']}"
                               required/>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-semibold">Speaking</label>
                        <input type="number"
                               name="speaking"
                               class="form-control"
                               step="0.1"
                               min="0"
                               max="10"
                               value="${scoreMap['Speaking']}"
                               required/>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-semibold">Listening</label>
                        <input type="number"
                               name="listening"
                               class="form-control"
                               step="0.1"
                               min="0"
                               max="10"
                               value="${scoreMap['Listening']}"
                               required/>
                    </div>

                </div>

                <hr class="my-4"/>


                <c:if test="${average != null}">
                    <div class="alert alert-info text-center fw-bold fs-5">
                        Final Average: ${average}
                    </div>
                </c:if>


                <div class="d-flex justify-content-between align-items-center mt-4">
                    <button type="submit" class="btn btn-save px-4">
                        ${scoreMap == null ? "Save Grade" : "Update Grade"}
                    </button>

                    <button type="reset" class="btn btn-cancel px-4">
                        Reset
                    </button>

                </div>

            </form>

        </div>
    </div>

                    
</div>
<script src="js/enterGrade.js" type="text/javascript"></script>
