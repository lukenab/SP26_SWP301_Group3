<%-- 
    Document   : adminDashboard
    Created on : Jan 29, 2026, 12:00:09 AM
    Author     : Legion
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css' rel='stylesheet'>
        <link href="adminDashboard.css" rel="stylesheet" type="text/css"/>
        <title>Admin Page</title>
    </head>
    <body>
        <nav>
            <div class="navbar">

                <div class="logo_item">
                    <i class='bx bx-menu sidebarOpen'></i>
                    <div class="nav_search">
                        <i class='bx bx-search'></i>
                        <input type="text" placeholder="Search...">
                    </div>
                </div>

                <div class="darkLight">
                    <i class="bx bx-moon moon"></i>
                    <i class='bx  bx-sun sun'></i> 
                </div>

                <div class="profile_item">
                    <img src="" alt="profile" class="profile_image">

                    <div class="profile_text">
                        <span class="name">Admin User</span>
                        <span class="email">admin@example.com</span>
                    </div>

                    <div class="profile_dropdown">
                        <div class="dropdown_header">
                            <div class="name">Admin User</div>
                            <div class="email">admin@example.com</div>
                        </div>

                        <ul class="dropdown_menu">
                            <li>
                                <a href="#">
                                    <i class='bx bx-user'></i>
                                    <span>Your Profile</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class='bx bx-cog'></i>
                                    <span>Settings</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class='bx bx-credit-card'></i>
                                    <span>Billing</span>
                                </a>
                            </li>

                            <li class="divider"></li> <li>
                                <a href="#" class="logout">
                                    <i class='bx bx-log-out'></i>
                                    <span>Sign out</span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
        </nav>

        <div class="sidebar close">
            <header>
                <div class="img-text">
                    <span class="logo-img">
                        <img src="logo.png" alt="logo"> 
                    </span>

                    <div class="header-text">
                        <span class="name">LCMS</span>
                        <span class="profession">Language Center</span>
                    </div>
                </div>
            </header>

            <div class="menu-bar">
                <div class="menu">
                    <ul class="menu-links">
                        <li class="nav-links">
                            <a href="#">
                                <i class='bx  bx-dashboard'></i> 
                                <span class="text nav-text">Dashboard</span>
                            </a>
                        </li>

                        <span class="divider"></span>

                        <li class="nav-links">
                            <a href="#">
                                <i class='bx  bx-user'></i> 
                                <span class="text nav-text">User Management</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class='bx bx-wallet-alt bx-flip-vertical'></i> 
                                <span class="text nav-text">Finance</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class='bx bx-file-report'></i> 
                                <span class="text nav-text">Report</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class='bx bx-cog'></i> 
                                <span class="text nav-text">Setting</span>
                            </a>
                        </li>
                </div>
            </div>
        </div>
        <script src="adminDashboard.js">

        </script>
    </body>
</html>
