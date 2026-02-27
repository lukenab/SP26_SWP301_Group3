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
        <link href="css/viewUser.css" rel="stylesheet" type="text/css"/>
        <link href="css/form.css" rel="stylesheet" type="text/css"/>
        <link href="css/viewGrade.css" rel="stylesheet" type="text/css"/>
        <title>Dashboard</title>
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
                        <span class="profile-img">
                            <img src="${sessionScope.user.avatar}">
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
                                        <i class="bx bx-user"></i><span>Your Profile</span>
                                    </a>
                                </li>

                                <li>
                                    <a href="#"><i class="bx bx-cog"></i><span>Settings</span></a>
                                </li>

                                <li class="divider"></li>
                                <li>
                                    <a href="logout" class="logout">
                                        <i class="bx bx-arrow-out-right-square-half"></i>
                                        <span>Log out</span>
                                    </a>
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

                <!-- ================= ADMIN ================= -->
                <c:if test="${sessionScope.user.role.roleId == 1}">
                    <ul class="menu-links">
                        <li class="nav-links">
                            <a href="dashboard?action=admin">
                                <i class="bxr bx-dashboard"></i>
                                <span class="text nav-text">Dashboard</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="user">
                                <i class="bxr bx-group"></i>
                                <span class="text nav-text">User Management</span>
                            </a>
                        </li>
                    </ul>
                </c:if>

                <!-- ================= TEACHER ================= -->
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
                    </ul>
                </c:if>

                <!-- ================= STUDENT ================= -->
                <c:if test="${sessionScope.user.role.roleId == 5}">
                    <ul class="menu-links">

                        <li class="nav-links">
                            <a href="dashboard">
                                <i class="bxr bx-dashboard"></i>
                                <span class="text nav-text">Dashboard</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="grade">
                                <i class="bxr bx-medal"></i>
                                <span class="text nav-text">View Grades</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="schedule">
                                <i class="bxr bx-calendar-event"></i>
                                <span class="text nav-text">My Schedule</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="dashboard?action=profile">
                                <i class="bxr bx-user"></i>
                                <span class="text nav-text">Profile</span>
                            </a>
                        </li>

                    </ul>
                </c:if>

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