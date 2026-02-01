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
        <link href="css/adminDashboard.css" rel="stylesheet" type="text/css"/>
        <title>Admin Page</title>
    </head>
    <body>
        <nav>
      <div class="navbar">
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
              <span class="name">Admin</span>
              <span class="email">admin@fpt.edu.vn</span>
            </div>

            <div class="profile-dropdown">
              <div class="dropdown-header">
                <div class="name">Admin User</div>
                <div class="email">admin@fpt.edu.vn</div>
              </div>

              <ul class="dropdown-menu">
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
                  <a href="logout" class="logout"
                    ><i class="bx bx-arrow-out-right-square-half"></i
                    ><span>Log out</span></a
                  >
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <div class="sidebar close">
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
          <ul class="menu-links">
            <li class="nav-links">
              <a href="#">
                <i class="bxr bx-dashboard"></i>
                <span class="text nav-text">Dashboard</span>
              </a>
            </li>

            <li class="nav-links">
              <a href="#">
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
        </div>
      </div>
    </div>

        <script src="adminDashboard.js">

        </script>
    </body>
</html>
