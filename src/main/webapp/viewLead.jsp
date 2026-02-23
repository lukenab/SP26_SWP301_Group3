<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="countNew" value="0" />

<c:forEach items="${leadList}" var="l">
    <c:if test="${l.status == 'New'}">
        <c:set var="countNew" value="${countNew + 1}" />
    </c:if>
</c:forEach>

<link href="css/leadView.css" rel="stylesheet" type="text/css"/>

<div class="lead-dashboard">
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card text-center">
                <h6>Total Leads</h6>
                <h3>${totalLeads}</h3>
                <small>Active leads in pipeline</small>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card text-center">
                <h6>New Today</h6>
                <h3 class="text-primary">${countNew}</h3>
                <small>Leads added today</small>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card text-center">
                <h6>Conversion Rate</h6>
                <h3 class="text-success">
                    <fmt:formatNumber value="${conversionRate}" type="percent"/>
                </h3>
                <small>Leads to students</small>
            </div>
        </div>
    </div>


    <div class="lead-filter-bar mb-4">
        <div class="filter-left">
            <input type="text" class="filter-input"
                   placeholder="Search name or phone...">

            <select class="filter-select">
                <option>All Status</option>
                <option>New</option>
                <option>Contacted</option>
                <option>Converted</option>
            </select>

            <select class="filter-select">
                <option>All Courses</option>
                <option>IELTS</option>
                <option>TOEIC</option>
            </select>
        </div>

        <a href="#" class="btn-add-lead">+ Add New Lead</a>
    </div>

    
    <table class="table align-middle">
        <thead>
            <tr>
                <th>Lead Name</th>
                <th>Contact Info</th>
                <th>Interested Course</th>
                <th>Create Date</th>
                <th>Status</th>
                <th class="text-center">Actions</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach items="${leadList}" var="l">
                <tr>
                    <td>${l.fullName}</td>

                    <td>
                        <div>${l.phone}</div>
                        <small class="text-muted">${l.email}</small>
                    </td>

                    
                    <td>
                        <c:if test="${l.course != null}">
                            ${l.course.courseName}
                        </c:if>
                    </td>

                    
                    <td>
                            ${l.createDate.toString().substring(0, 10)}
                    </td>

                   
                    <td>
                        <c:choose>
                            <c:when test="${l.status eq 'New'}">
                                <span class="status-new">New</span>
                            </c:when>
                            <c:when test="${l.status eq 'Contacted'}">
                                <span class="status-contacted">Contacted</span>
                            </c:when>
                            <c:when test="${l.status eq 'Converted'}">
                                <span class="status-converted">Converted</span>
                            </c:when>
                            <c:otherwise>
                                <span>${l.status}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

              
                    <td class="text-center">
                        <div class="action-icons">
                            <span title="Call"><i class="bx bx-phone"></i></span>
                            <span title="Edit"><i class="bx bx-edit"></i></span>
                             <span title="Edit"><i class="bx bx-user-check"></i></span>
                        </div>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty lead}">
                <tr>
                    <td colspan="6" class="text-center">
                        No leads found
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>

</div>