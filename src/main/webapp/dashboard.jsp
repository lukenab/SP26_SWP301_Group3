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
        <link href='https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css' rel='stylesheet'>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/dashboard.css" rel="stylesheet" type="text/css"/>
        <link href="css/teacherSchedule.css" rel="stylesheet" type="text/css"/>
        <link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
        <title>Admin Page</title>
    </head>
    <body>
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
                        <span class="profile-img">A</span>
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
                                    <a href="#"
                                       ><i class="bx bx-user"></i><span>Your Profile</span></a
                                    >
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
                    <div class="logo-header">
                        <img src="images/logo.png" alt="logo" />
                    </div>

                    <div class="logo-text">
                        <span class="name">LMCS</span>
                        <span class="profession">Language Center</span>
                    </div>
                </div>
            </header>

            <div class="menu-bar">
                <c:if test="${sessionScope.user.userId == 1}">
                    <ul class="menu-links">
                        <li class="nav-links">
                            <a href="dashboard">
                                <i class="bxr bx-dashboard"></i>
                                <span class="text nav-text">Dashboard</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="user">
                                <i class="bxr bx-user"></i>
                                <span class="text nav-text">User Management</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class="bxr bx-wallet-alt bx-flip-vertical"></i>
                                <span class="text nav-text">Finance</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class="bxr bx-file-report"></i>
                                <span class="text nav-text">Report</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class="bxr bx-cog"></i>
                                <span class="text nav-text">Setting</span>
                            </a>
                        </li>
                    </ul>
                </c:if>

                <c:if test="${sessionScope.user.role.roleId == 4}">
                    <ul class="menu-links">
                        <li class="nav-links">
                            <a href="dashboard">
                                <i class="bxr bx-dashboard"></i>
                                <span class="text nav-text">Dashboard</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="schedule">
                                <i class="bxr bx-calendar-event"></i>
                                <span class="text nav-text">My Schedule</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class="bxr bx-door"></i>
                                <span class="text nav-text">My Classes</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class="bxr bx-group"></i>
                                <span class="text nav-text">Students</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class="bxr bx-cog"></i>
                                <span class="text nav-text">Setting</span>
                            </a>
                        </li>
                    </ul>
                </c:if>
                
                <c:if test="${sessionScope.user.role.roleId == 2}">
                    <ul class="menu-links">
                        <li class="nav-links">
                            <a href="dashboard">
                                <i class="bxr bx-dashboard"></i>
                                <span class="text nav-text">Dashboard</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="schedule">
                                <i class="bxr bx-book"></i>
                                <span class="text nav-text">Course</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class="bxr bx-door"></i>
                                <span class="text nav-text">Classes</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class="bxr bx-group"></i>
                                <span class="text nav-text">Students</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class="bxr bx-cog"></i>
                                <span class="text nav-text">Setting</span>
                            </a>
                        </li>
                    </ul>
                </c:if>
            </div>
        </div>
    </div>

    <main class="home-section" style="height: 88vh">
        <c:if test="${not empty home_view}">
            <c:import url="${home_view}" />
        </c:if>
    </main>
    <script src="dashboard.js">

    </script>
</body>
</html>
