<%-- 
    Document   : adminDashboard
    Created on : Jan 29, 2026, 12:00:09 AM
    Author     : Legion
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href='https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css' rel='stylesheet'>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/dashboard.css" rel="stylesheet" type="text/css"/>
        <link href="css/teacherSchedule.css" rel="stylesheet" type="text/css"/>
        <link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
        <link href="css/viewUser.css" rel="stylesheet" type="text/css"/>
        <link href="css/form.css" rel="stylesheet" type="text/css"/>
        <link href="css/saleDashboard.css" rel="stylesheet" type="text/css"/>
        <title>Admin Page</title>
    </head>
    <body>
        <c:set var="currentAction" value="${param.action}" />
        <nav class="active">
            <div class="custom-navbar">
                <div class="navbar-left">
                    <div class="sidebarOpen">
                        <i class="bx bx-menu sidebarOpen-icon"></i>
                    </div>

                    <div class="search-field">
                        <i class="bx bx-search"></i>
                        <input type="text" placeholder="Search..." />
                    </div>
                </div>

                <div class="navbar-right">
                    <div class="darkMode">
                        <i class="bx bx-sun sun"></i>
                        <i class="bx bx-moon moon"></i>
                    </div>

                    <div class="profile-item">
                        <span class="profile-img">
                            <img src="${pageContext.request.contextPath}/${sessionScope.user.avatar}">
                        </span>
                        <div class="profile-text">
                            <span class="name">${sessionScope.user.fullName}</span>
                            <span class="email">${sessionScope.user.email}</span>
                        </div>

                        <div class="profile-dropdown">
                            <div class="dropdown-header">
                                <div class="name">${sessionScope.user.fullName}</div>
                                <div class="email">${sessionScope.user.email}</div>
                            </div>

                            <ul class="profile-options-list">
                                <li class="divider"></li>
                                <li>
                                    <a href="dashboard?action=profile">
                                        <i class="bx bx-user"></i><span>Your Profile</span></a>
                                </li>

                                <li>
                                    <a href="#"><i class="bx bx-cog"></i><span>Settings</span></a>
                                </li>

                                <li class="divider"></li>
                                <li>
                                    <a href="logout" class="logout">
                                        <i class="bx bx-arrow-out-right-square-half"></i>
                                        <span>Log out</span></a>

                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </nav>

        <div class="sidebar">
            <header>

                <div class="sidebar-header">
                    <c:choose>
                        <c:when test="${sessionScope.user.role.roleId == 1}">
                            <c:set var="logoUrl" value="${pageContext.request.contextPath}/dashboard?action=admin" />
                        </c:when>
                        <c:when test="${sessionScope.user.role.roleId == 2}">
                            <c:set var="logoUrl" value="${pageContext.request.contextPath}/dashboard?action=academic" />
                        </c:when>
                        <c:when test="${sessionScope.user.role.roleId == 3}">
                            <c:set var="logoUrl" value="${pageContext.request.contextPath}/dashboard" />
                        </c:when>
                        <c:when test="${sessionScope.user.role.roleId == 4}">
                            <c:set var="logoUrl" value="${pageContext.request.contextPath}/dashboard?action=teacher" />
                        </c:when>
                        <c:when test="${sessionScope.user.role.roleId == 5}">
                            <c:set var="logoUrl" value="${pageContext.request.contextPath}/dashboard?action=student" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="logoUrl" value="${pageContext.request.contextPath}/dashboard" />
                        </c:otherwise>
                    </c:choose>

                    <a href="${logoUrl}" class="logo-link">
                        <div class="logo-header">
                            <img src="images/logo.png" alt="logo" />
                        </div>

                        <div class="logo-text">
                            <span class="name">LMCS</span>
                            <span class="profession">Language Center</span>
                        </div>
                    </a>
                </div>
            </header>

<div class="menu-bar">
                <ul class="menu-links">
                    
                    <c:if test="${sessionScope.user.role.roleId == 1}">
                        
                        <li class="menu-header">
                            <span class="text nav-text">MAIN</span>
                        </li>
                        <li class="nav-links">
                            <a href="dashboard?action=admin" class="${currentAction == 'admin' ? 'active' : ''}">
                                <i class="bx bx-dashboard"></i>
                                <span class="text nav-text">Dashboard</span>
                            </a>
                        </li>

                        <li class="menu-header">
                            <span class="text nav-text">MANAGEMENT</span>
                        </li>
                        <li class="nav-links">
                            <a href="user" class="${pageContext.request.servletPath == '/user' ? 'active' : ''}">
                                <i class="bx bx-group"></i>
                                <span class="text nav-text">User Management</span>
                            </a>
                        </li>

                        <li class="menu-header">
                            <span class="text nav-text">SYSTEM</span>
                        </li>
                        <li class="nav-links">
                            <a href="role" class="${pageContext.request.servletPath == '/role' ? 'active' : ''}">
                                <i class="bx bx-shield"></i>
                                <span class="text nav-text">Role & Permission</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="dashboard?action=report" class="${currentAction == 'report' ? 'active' : ''}">
                                <i class="bx bx-file-report"></i>
                                <span class="text nav-text">System Reports</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="setting" class="${pageContext.request.servletPath == '/setting' ? 'active' : ''}">
                                <i class="bx bx-cog"></i>
                                <span class="text nav-text">System Settings</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${sessionScope.user.role.manageFinance}">
                        <li class="menu-header">
                            <span class="text nav-text">SALES & CRM</span>
                        </li>
                        <li class="nav-links">
                            <a href="dashboard?action=sale" class="${currentAction == 'sale' ? 'active' : ''}">
                                <i class="bxr bx-dashboard"></i>
                                <span class="text nav-text">Sales Dashboard</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="lead?action=all" class="${pageContext.request.servletPath == '/lead' && param.action != 'revenueReport' && param.action != 'salesReport' && param.action != 'openClasses' ? 'active' : ''}">
                                <i class="bx bx-user"></i>
                                <span class="text nav-text">Leads Management</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="lead?action=openClasses" class="${param.action == 'openClasses' ? 'active' : ''}">
                                <i class="bxr bx-door-open"></i>
                                <span class="text nav-text">Open Classes</span>
                            </a>
                        </li>

                        <li class="menu-header">
                            <span class="text nav-text">FINANCE & PROMO</span>
                        </li>
                        <li class="nav-links">
                            <a href="payment?action=list" class="${pageContext.request.servletPath == '/payment' ? 'active' : ''}">
                                <i class="bxr bx-check-circle"></i>
                                <span class="text nav-text">Verify Payments</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="voucher?action=all" class="${pageContext.request.servletPath == '/voucher' && param.action != 'report' ? 'active' : ''}">
                                <i class="bxr bx-gift"></i>
                                <span class="text nav-text">Voucher</span>
                            </a>
                        </li>

                        <li class="menu-header">
                            <span class="text nav-text">REPORTS</span>
                        </li>
                        <li class="nav-links">
                            <a href="lead?action=revenueReport" class="${param.action == 'revenueReport' ? 'active' : ''}">
                                <i class="bxr bx-wallet-alt"></i>
                                <span class="text nav-text">Revenue Report</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="lead?action=salesReport" class="${param.action == 'salesReport' ? 'active' : ''}">
                                <i class="bxr bx-chart-line"></i>
                                <span class="text nav-text">Sales Reports</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="voucher?action=report" class="${param.action == 'report' && pageContext.request.servletPath == '/voucher' ? 'active' : ''}">
                                <i class="bx bx-receipt"></i>
                                <span class="text nav-text">Voucher Report</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${sessionScope.user.role.manageCourse}">
                        <li class="menu-header">
                            <span class="text nav-text">ACADEMIC MANAGEMENT</span>
                        </li>
                        <li class="nav-links">
                            <a href="dashboard?action=academic" class="${currentAction == 'academic' ? 'active' : ''}">
                                <i class="bxr bx-dashboard"></i>
                                <span class="text nav-text">Academic Dashboard</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="course" class="${pageContext.request.servletPath == '/course' ? 'active' : ''}">
                                <i class="bxr bx-book"></i>
                                <span class="text nav-text">Courses</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="syllabus?action=manage" class="${pageContext.request.servletPath == '/syllabus' ? 'active' : ''}">
                                <i class="bxr bx-book-content"></i>
                                <span class="text nav-text">Syllabus</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="enrollment?action=classes" class="${pageContext.request.servletPath == '/enrollment' && param.action != 'requests' ? 'active' : ''}">
                                <i class="bxr bx-door"></i>
                                <span class="text nav-text">Classes</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="enrollment?action=requests" class="${pageContext.request.servletPath == '/enrollment' && param.action == 'requests' ? 'active' : ''}">
                                <i class="bxr bx-check-shield"></i>
                                <span class="text nav-text">Enrollments</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="schedule?action=manage" class="${pageContext.request.servletPath == '/schedule' && param.action == 'manage' ? 'active' : ''}">
                                <i class="bxr bx-calendar-event"></i>
                                <span class="text nav-text">Schedules</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="room" class="${pageContext.request.servletPath == '/room' ? 'active' : ''}">
                                <i class="bxr bx-door-open"></i>
                                <span class="text nav-text">Rooms</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${sessionScope.user.role.roleId == 4}">
                        <li class="menu-header">
                            <span class="text nav-text">TEACHING PORTAL</span>
                        </li>
                        <li class="nav-links">
                            <a href="dashboard?action=teacher" class="${currentAction == 'teacher' ? 'active' : ''}">
                                <i class="bxr bx-dashboard"></i>
                                <span class="text nav-text">Dashboard</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="schedule?action=viewTeacherSchedule" class="${pageContext.request.servletPath == '/schedule' ? 'active' : ''}">
                                <i class="bxr bx-calendar-event"></i>
                                <span class="text nav-text">My Schedule</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="class?action=all" class="${pageContext.request.servletPath == '/class' ? 'active' : ''}">
                                <i class="bxr bx-door"></i>
                                <span class="text nav-text">My Classes</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="feedback" class="${pageContext.request.servletPath == '/feedback' ? 'active' : ''}">
                                <i class="bxr bx-poll"></i>
                                <span class="text nav-text">My Feedback</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${sessionScope.user.role.roleId == 5}">
                        <li class="menu-header">
                            <span class="text nav-text">STUDENT PORTAL</span>
                        </li>
                        <li class="nav-links">
                            <a href="dashboard?action=student" class="${currentAction == 'student' ? 'active' : ''}">
                                <i class="bxr bx-dashboard"></i>
                                <span class="text nav-text">Dashboard</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="class?action=availableClass" class="${param.action == 'availableClass' ? 'active' : ''}">
                                <i class="bxr bx-book-open"></i>
                                <span class="text nav-text">Available Classes</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="class?action=myClasses" class="${param.action == 'myClasses' ? 'active' : ''}">
                                <i class="bxr bx-book"></i>
                                <span class="text nav-text">My Classes</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="schedule?action=studentView" class="${pageContext.request.servletPath == '/schedule' ? 'active' : ''}">
                                <i class="bxr bx-calendar-event"></i>
                                <span class="text nav-text">My Schedule</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="attendance?action=studentReport" class="${pageContext.request.servletPath == '/attendance' ? 'active' : ''}">
                                <i class="bxr bx-check-square"></i>
                                <span class="text nav-text">Attendance Report</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="grade?action=student-courses" class="${pageContext.request.servletPath == '/grade' ? 'active' : ''}">
                                <i class="bxr bx-medal"></i>
                                <span class="text nav-text">View Grades</span>
                            </a>
                        </li>
                        <li class="nav-links">
                            <a href="feedback?action=viewStudentCoursesFeedback" class="${pageContext.request.servletPath == '/feedback' ? 'active' : ''}">
                                <i class="bxr bx-poll"></i>
                                <span class="text nav-text">Course Feedback</span>
                            </a>
                        </li>
                    </c:if>

                    <li class="menu-header">
                        <span class="text nav-text">PERSONAL</span>
                    </li>
                    <li class="nav-links">
                        <a href="dashboard?action=profile" class="${currentAction == 'profile' ? 'active' : ''}">
                            <i class="bxr bx-user"></i>
                            <span class="text nav-text">Profile</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>

        <main class="home-section" style="height: 88vh">
            <c:if test="${not empty home_view}">
                <c:import url="${home_view}" />
            </c:if>
        </main>
        <script src="dashboard.js"></script>
        <script src="js/bootstrap.bundle.min.js" type="text/javascript"></script>
    </body>
</html>
