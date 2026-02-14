<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <div>
        <h1 class="title">Add New User</h1>
        <ul class="breadcrumbs">
            <li><a href="home">Home</a></li>
            <li class="divider">/</li>
            <li><a href="user?action=all">Users</a></li>
            <li class="divider">/</li>
            <li><a href="#" class="active">Add New User</a></li>
        </ul>
    </div>
    <a href="department" class="btn-secondary">
        <i class='bx bx-arrow-back'></i> Back to Users
    </a>
</div>

<div class="form-container">

    <p class="form-title">User Information</p>

    <form action="department?action=add" method="POST" class="form-body">

        <div class="form-row">
            <div class="form-group">
                <label for="departmentCode">Department Code</label>
                <input type="text" name="departmentCode" id="departmentCode" required>
            </div>

            <div class="form-group">
                <label for="departmentName">Department Name</label>
                <input type="text" name="departmentName" id="departmentName" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="departmentHead">Department Head</label>
                <input type="text" id="departmentHead" name="departmentHead">
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="text" id="email" name="email">
            </div>
        </div>

        <div class="form-row">              
            <div class="form-group">
                <label for="phone">Phone</label>
                <input type="text" id="phone" name="phone"> 
            </div>
        </div>

        <div class="form-buttons">
            <a href="department?action=all" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-save'></i> Add New User
            </button>
        </div>

    </form>
</div>

