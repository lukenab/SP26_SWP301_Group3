<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid px-4">

    <div class="d-flex justify-content-between mt-4 mb-3">
        <h3 class="fw-bold text-primary">
            Student List
        </h3>

        <a href="class" class="btn btn-secondary btn-sm">
            Back to Classes
        </a>
    </div>

    <div class="card shadow-sm border-0">
        <div class="card-body">

            <table class="table table-bordered table-hover align-middle">
                <thead class="table-light">
                    <tr>
                        <th>#</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Score</th>
                        <th>Action</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="s"
                               items="${studentList}"
                               varStatus="loop">

                        <tr>
                            <td>${loop.count}</td>
                            <td>${s.fullName}</td>
                            <td>${s.email}</td>
                            <td>${s.phone}</td>


                            <td>
                                <c:set var="score"
                                       value="${gradeMap[s.userId]}" />

                                <c:choose>
                                    <c:when test="${score != null}">
                                        <span class="badge 
                                              ${score < 5 ? 'bg-danger' : 'bg-success'}">
                                            ${score}
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">
                                            Not graded
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>


                            <td>

                                <c:choose>


                                    <c:when test="${score == null}">
                                        <a href="grade?action=enter&studentId=${s.userId}&classId=${classId}"
                                           class="btn btn-sm btn-primary">
                                            Enter
                                        </a>
                                    </c:when>


                                    <c:otherwise>

                                        <a href="grade?action=edit&studentId=${s.userId}&classId=${classId}"
                                           class="btn btn-sm btn-warning">
                                            Edit
                                        </a>

                                        <form action="grade" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="delete"/>
                                            <input type="hidden" name="studentId" value="${s.userId}"/>
                                            <input type="hidden" name="classId" value="${classId}"/>

                                            <button type="submit"
                                                    class="btn btn-sm btn-danger"
                                                    onclick="return confirm('Delete this grade?')">
                                                Delete
                                            </button>
                                        </form>

                                    </c:otherwise>

                                </c:choose>

                            </td>

                        </tr>

                    </c:forEach>
                </tbody>

            </table>

        </div>
    </div>
</div>