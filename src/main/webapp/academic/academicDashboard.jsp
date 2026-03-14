<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/adminDashboard.css" rel="stylesheet" type="text/css"/>
<link href="css/academicDashboard.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="dashboard?action=academic"><i class="bx bx-home-alt"></i></a></li>
            <li class="breadcrumb-item active" aria-current="page">Academic Dashboard</li>
        </ol>
    </div>
    <div class="content-header">
        <div>
            <h2 class="page-title">Academic Overview</h2>
            <p class="text-muted small mb-0">Track rooms, classes, courses, and this week's teaching schedule.</p>
        </div>
    </div>
</div>

<div class="stat-card-grid">
    <div class="stat-card">
        <div class="stat-info">
            <p>Total Rooms</p>
            <h3>${totalRooms}</h3>
        </div>
        <div class="icon-wrapper cyan">
            <i class='bx bx-door-open'></i>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-info">
            <p>Active Rooms</p>
            <h3>${activeRooms}</h3>
        </div>
        <div class="icon-wrapper green">
            <i class='bx bx-check-circle'></i>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-info">
            <p>Classes (Active/Total)</p>
            <h3>${activeClasses}/${totalClasses}</h3>
        </div>
        <div class="icon-wrapper blue">
            <i class='bx bx-book-open'></i>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-info">
            <p>Courses (Active/Total)</p>
            <h3>${activeCourses}/${totalCourses}</h3>
        </div>
        <div class="icon-wrapper red">
            <i class='bx bx-book'></i>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-info">
            <p>This Week Schedules</p>
            <h3>${weeklySchedules}</h3>
        </div>
        <div class="icon-wrapper green">
            <i class='bx bx-calendar-event'></i>
        </div>
    </div>
</div>

<div class="quick-action-grid">
    <a class="quick-action-card" href="room">
        <i class="bx bx-door-open"></i>
        <span>Manage Rooms</span>
    </a>
    <a class="quick-action-card" href="enrollment?action=classes">
        <i class="bx bx-group"></i>
        <span>Manage Classes</span>
    </a>
    <a class="quick-action-card" href="schedule?action=manage">
        <i class="bx bx-calendar-event"></i>
        <span>Manage Schedule</span>
    </a>
    <a class="quick-action-card" href="course">
        <i class="bx bx-book"></i>
        <span>Manage Courses</span>
    </a>
    <a class="quick-action-card" href="syllabus?action=manage">
        <i class="bx bx-book-content"></i>
        <span>Manage Syllabus</span>
    </a>
</div>

