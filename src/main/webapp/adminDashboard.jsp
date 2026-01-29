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
                <span class="logo"><a>LCMS</a></span>

                <div class="menu">
                    <ul class="nav-links">
                        <li><a href="#">Home</a></li>
                        <li><a href="#">About</a></li>
                        <li><a href="#">Contact</a></li>
                    </ul>
                </div>

                <div class="darkLight-searchBox">
                    <div class="darkLight">
                        <i class="bx bx-moon moon"></i>
                        <i class='bxr  bx-sun sun'></i> 
                    </div>

                    <div class="searchBox">
                        <div class="searchToggle">
                            <i class='bxr  bx-x cancel'></i> 
                            <i class='bxr  bx-search search'></i> 
                        </div>

                        <div class="search-field">
                            <input type="text" placeholder="Search...">
                            <i class='bxr  bx-search'></i>
                        </div>
                    </div>
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
                        <span class="name text"></span>

                    </div>
                </div>

                <i class='bxr  bx-chevron-right toggle'></i> 
            </header>

            <div class="menu-bar">
                <div class="menu">
                    <ul class="menu-links">
                        <li class="nav-links">
                            <a href="#">
                                <i class='bxr  bx-dashboard'></i> 
                                <span class="text nav-text">Dashboard</span>
                            </a>
                        </li>

                        <span class="divider"></span>

                        <li class="nav-links">
                            <a href="#">
                                <i class='bxr  bx-user'></i> 
                                <span class="text nav-text">User Management</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class='bxr  bx-wallet-alt bx-flip-vertical'></i> 
                                <span class="text nav-text">Finance</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class='bxr  bx-file-report'></i> 
                                <span class="text nav-text">Report</span>
                            </a>
                        </li>

                        <li class="nav-links">
                            <a href="#">
                                <i class='bxr  bx-cog'></i> 
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
