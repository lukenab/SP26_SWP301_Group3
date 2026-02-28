<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <h3 class="text-primary fw-bold text-uppercase">
            ${score == null ? "Enter Grade" : "Edit Grade"}
        </h3>

        <a href="student?action=viewByClass&classId=${classId}"
           class="btn btn-secondary btn-sm">
            ← Back to Student List
        </a>
    </div>

    <!-- CARD -->
    <div class="card shadow-sm border-0 col-md-6">
        <div class="card-body">

            <form method="post" action="grade">

                <!-- hidden -->
                <input type="hidden" name="action" value="save"/>
                <input type="hidden" name="studentId" value="${studentId}"/>
                <input type="hidden" name="classId" value="${classId}"/>

                <!-- Student Info -->
                <div class="mb-3">
                    <label class="form-label fw-bold">Student ID</label>
                    <input type="text"
                           class="form-control"
                           value="${studentId}"
                           readonly/>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Class ID</label>
                    <input type="text"
                           class="form-control"
                           value="${classId}"
                           readonly/>
                </div>

                <!-- Score -->
                <div class="mb-3">
                    <label class="form-label fw-bold">
                        Score (0 - 10)
                    </label>

                    <input type="number"
                           name="score"
                           class="form-control"
                           step="0.1"
                           min="0"
                           max="10"
                           value="${score}"
                           required/>
                </div>

                <!-- Buttons -->
                <div class="d-flex gap-2">

                    <button type="submit"
                            class="btn btn-primary">
                        ${score == null ? "Save" : "Update"}
                    </button>

                    <c:if test="${score != null}">
                        <a href="grade?action=delete
                           &studentId=${studentId}
                           &classId=${classId}"
                           class="btn btn-danger"
                           onclick="return confirm('Are you sure?')">
                            Delete
                        </a>
                    </c:if>

                </div>

            </form>

        </div>
    </div>

</div>