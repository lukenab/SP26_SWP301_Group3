<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/createUser.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">

    <div class="content-header">
        <div>
            <h1 class="page-title">Add New User</h1>
        </div>
        <a href="user" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Users
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="user">User Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Add New User</li>
        </ol>
    </div>

</div>

<div class="form-container">

    <p class="form-title">User Information</p>
    <form action="user?action=add" method="POST" class="form-body">

        <div class="form-row">
            <div class="form-group">
                <label for="roleId">Role <span class="text-danger">*</span></label>
                <select class="form-select" name="roleId" id="roleId" required>
                    <option value="" disabled selected>Select Role</option>
                    <c:forEach var="r" items="${roleList}">
                        <option value=${r.roleId}>${r.roleName}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" name="fullName" id="fullName" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="text" id="password" name="password" required> 
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="phone">Phone</label>
                <input type="number" id="phone" name="phone" required>
            </div>

            <div class="form-group">
                <label for="address">Address</label>
                <input type="text" id="address" name="address">
            </div>
        </div>

        <div class="form-row">           
            <div class="form-group">
                <label for="gender" class="form-label">Gender</label>
                <select class="form-select" aria-label="Default select example" name="gender" required>
                    <option value="true">Male</option>
                    <option value="false">Female</option>
                </select>
            </div>

            <div class="form-group">
                <label for="dob">Date of Birth</label>
                <input type="date" id="dob" name="dob">
            </div>
        </div>

        <div class="form-row">           
            <div class="form-group">
                <label for="avatar" class="form-label">Avatar</label>
                <input type="text" id="avatar" name="avatar" placeholder="Paste your image's link here (eg,. https://...)">
            </div>

            <div class="form-group">
                <label for="status">Status</label>
                <select class="form-select" aria-label="Default select example" name="status" required>
                    <option value="true">Active</option>
                    <option value="false">Inactive</option>
                </select>
            </div>
        </div>

        <div class="form-buttons">
            <a href="user?action=all" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-save'></i> Add New User
            </button>
        </div>
    </form>
</div>

