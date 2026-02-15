<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/createUser.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">

    <div class="content-header">
        <div>
            <h1 class="page-title">Lock User</h1>
        </div>
        <a href="user" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Users
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="user">User Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Lock User</li>
        </ol>
    </div>
</div>

<div class="form-container">

    <p class="form-title">User Information</p>


    <c:choose>
        <c:when test="${uDelete.status}">
            <form action="user?action=inActivate" method="POST" class="form-body">

                <input type="hidden" name="id" value="${uDelete.userId}">

                <div class="form-row user-img">
                    <img src="${uDelete.avatar}" class="rounded-circle mb-3">
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="roleId">Role <span class="text-danger">*</span></label>
                        <input type="text"  value="${uDelete.role.roleName}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="fullName">Full Name</label>
                        <input type="text" name="fullName" id="fullName" value="${uDelete.fullName}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" value="${uDelete.email}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="number" id="phone" name="phone" value="${uDelete.phone}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="address">Address</label>
                        <input type="text" id="address" name="address" value="${uDelete.address}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="gender" class="form-label">Gender</label>
                        <input type="text" value="${uDelete.gender ? 'Male' : 'Female'}" readonly>
                    </div>
                </div>

                <div class="form-row">           
                    <div class="form-group">
                        <label for="dob">Date of Birth</label>
                        <input type="date" id="dob" name="dob" value="${uDelete.dob}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="status">Status</label>
                        <input type="text" id="status" value="${uDelete.status  ? 'Active' : 'Inactive'}" readonly>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="user" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-lock">
                        <i class='bx bx-lock'></i> Inactivate User
                    </button>
                </div>
            </c:when>

            <c:otherwise>
                <form action="user?action=activate" method="POST" class="form-body">

                    <input type="hidden" name="id" value="${uDelete.userId}">

                    <div class="form-row user-img">
                        <img src="${uDelete.avatar}" class="rounded-circle mb-3">
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="roleId">Role <span class="text-danger">*</span></label>
                            <input type="text"  value="${uDelete.role.roleName}" readonly>
                        </div>

                        <div class="form-group">
                            <label for="fullName">Full Name</label>
                            <input type="text" name="fullName" id="fullName" value="${uDelete.fullName}" readonly>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" value="${uDelete.email}" readonly>
                        </div>

                        <div class="form-group">
                            <label for="phone">Phone</label>
                            <input type="number" id="phone" name="phone" value="${uDelete.phone}" readonly>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="address">Address</label>
                            <input type="text" id="address" name="address" value="${uDelete.address}" readonly>
                        </div>
                        <div class="form-group">
                            <label for="gender" class="form-label">Gender</label>
                            <input type="text" value="${uDelete.gender ? 'Male' : 'Female'}" readonly>
                        </div>
                    </div>

                    <div class="form-row">           
                        <div class="form-group">
                            <label for="dob">Date of Birth</label>
                            <input type="date" id="dob" name="dob" value="${uDelete.dob}" readonly>
                        </div>
                        <div class="form-group">
                            <label for="status">Status</label>
                            <input type="text" id="status" value="${uDelete.status  ? 'Active' : 'Inactive'}" readonly>
                        </div>
                    </div>
                    <div class="form-buttons">
                        <a href="user" class="btn btn-cancel">Cancel</a>
                        <button type="submit" class="btn btn-unlock">
                            <i class='bx bx-lock-open'></i> Activate User
                        </button>
                    </div>
                </c:otherwise>
            </c:choose>
        </form>
</div>

