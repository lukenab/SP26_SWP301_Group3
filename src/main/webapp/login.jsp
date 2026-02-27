<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <link href="css/login.css" rel="stylesheet" type="text/css"/>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href='https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css' rel='stylesheet'>
    </head>
    <body>



        <div class="login-container">
            <c:if test="${not empty sessionScope.message}">
                <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
                    <div class="toast-icon">
                        <c:choose>
                            <c:when test="${sessionScope.messageType == 'success'}">
                                <i class='bx bx-check-circle'></i>
                            </c:when>
                            <c:otherwise>
                                <i class='bx bx-error-circle'></i>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="toast-content">
                        <span class="toast-title">
                            ${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}
                        </span>
                        <span class="toast-message">${sessionScope.message}</span>
                    </div>
                    <button class="toast-close" onclick="closeToast()">
                        <i class='bx bx-x'></i>
                    </button>
                </div>

                <c:remove var="message" scope="session" />
                <c:remove var="messageType" scope="session" />
            </c:if>
            <div class="login-card">
                <div class="login-header">
                    <div class="login-logo">
                        <img src="images/login-logo.png" alt="logo" />
                    </div>
                    <h2>Welcome back</h2>
                    <p>Please sign in to continue</p>
                </div>

                <c:if test="${not empty sessionScope.loginMessage}">
                    <div class="alert-error">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                        <span>${sessionScope.loginMessage}</span>
                    </div>
                    <c:remove var="message" scope="session"/>
                </c:if>

                <form class="login-form" action="login" method="post">
                    <div class="form-group">
                        <div class="input-wrapper">
                            <input type="email" id="email" name="email" required placeholder=" " />
                            <label for="email">Email</label>
                            <span class="input-border"></span>
                        </div>
                    </div>
                    <span class="error-message" id="emailError"></span>

                    <div class="form-group">
                        <div class="input-wrapper password-wrapper">
                            <input type="password" id="password" name="password" required placeholder=" "/>
                            <label for="password">Password</label>
                            <span class="input-border"></span>
                            <button type="button" class="password-toggle" id="passwordToggle">
                                <span class="toggle-icon">
                                    <span class="toggle-icon">
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z"/>
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
                                        </svg>
                                    </span>
                                </span>
                            </button>
                            <span class="error-message" id="passwordError"></span>
                        </div>
                    </div>

                    <div class="form-options">
                        <div class="remember-wrapper">
                            <input type="checkbox" id="remember">
                            <label for="remember" class="checkbox-label">
                                <span class="checkbox-custom"></span>Keep me signed in
                            </label>
                        </div>
                        <a href="#" class="forgot-password">Reset password </a>        
                    </div>
                    <button type="submit" class="login-btn">
                        <span>Sign In</span>
                    </button>
                </form>

                <div class="footer-links">
                    <a href="#" class="footer-link">Privacy Policy</a>
                    <span class="separator">•</span>
                    <a href="#" class="footer-link">Terms of Service</a>
                    <span class="separator">•</span>
                    <a href="#" class="footer-link">Support</a>
                </div>
            </div>
        </div>
        <script src="js/manageUser.js" type="text/javascript"></script>
    </body>
</html>

