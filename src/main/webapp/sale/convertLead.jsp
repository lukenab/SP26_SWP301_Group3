<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/editLead.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Convert Lead To Student</h1>
        </div>
        <a href="lead?action=edit&id=${lead.leadId}" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Edit Lead
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="lead?action=all">Lead Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Convert Lead</li>
        </ol>
    </div>
</div>

<c:if test="${not empty sessionScope.message}">
    <div class="alert ${sessionScope.messageType == 'success' ? 'alert-success' : 'alert-danger'}" role="alert">
        ${sessionScope.message}
    </div>
    <c:remove var="message" scope="session"/>
    <c:remove var="messageType" scope="session"/>
</c:if>

<div class="convert-alert">
    Complete missing information, then click confirm to create Student account and mark this lead as Converted.
    System will auto set default password as <strong>123456</strong> and send it to student email.
</div>

<div class="form-container">
    <form action="lead" method="POST" class="form-body">
        <input type="hidden" name="action" value="convert">
        <input type="hidden" name="leadId" value="${lead.leadId}">

        <div class="form-row">
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" value="${lead.fullName}" required>
            </div>
            <div class="form-group">
                <label for="phone">Phone</label>
                <input type="text" id="phone" name="phone" value="${lead.phone}" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" value="${lead.email}" required>
            </div>
            <div class="form-group">
                <label for="address">Address</label>
                <input type="text" id="address" name="address" placeholder="Enter address">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="dob">Date of Birth</label>
                <input type="date" id="dob" name="dob" required>
            </div>
            <div class="form-group">
                <label for="gender">Gender</label>
                <select name="gender" id="gender" required>
                    <option value="" selected disabled>Select gender</option>
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="passwordPreview">Password (Auto)</label>
                <input type="text" id="passwordPreview" value="123456" readonly disabled>
            </div>
            <div class="form-group">
                <label for="confirmPasswordPreview">Confirm Password (Auto)</label>
                <input type="text" id="confirmPasswordPreview" value="123456" readonly disabled>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="enrollmentDate">Enrollment Date</label>
                <input type="date" id="enrollmentDate" name="enrollmentDate" value="${today}">
            </div>
            <div class="form-group">
                <label for="avatar">Avatar URL (optional)</label>
                <input type="text" id="avatar" name="avatar" placeholder="https://...">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group form-group-full">
                <label for="convertNote">Convert Note</label>
                <textarea id="convertNote" name="convertNote" rows="3" placeholder="Internal note for conversion..."></textarea>
            </div>
        </div>

        <div class="form-buttons">
            <a href="lead?action=edit&id=${lead.leadId}" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-convert" onclick="return confirm('Confirm convert this lead to student?');">
                <i class='bx bx-user-check'></i> Confirm Convert
            </button>
        </div>
    </form>
</div>
