<%-- 
    Document   : login
    Created on : Jan 27, 2026, 7:55:43 AM
    Author     : Legion
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <link href="login.css" rel="stylesheet" type="text/css"/>
        <link href="bootstrap.min.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="login-container">
            <div class="login-card">
                <div class="login-header">
                    <div class="login-logo">
                        <img src="login-logo.png" alt="logo" />
                    </div>
                    <h2>Welcome back</h2>
                    <p>Please sign in to continue</p>
                </div>

                <form class="login-form">
                    <div class="form-group">
                        <div class="input-wrapper">
                            <input type="email" id="email" required placeholder=" " />
                            <label for="email">Email</label>
                            <span class="input-border"></span>
                        </div>
                    </div>
                    <span class="error-message" id="emailError"></span>

                    <div class="form-group">
                        <div class="input-wrapper password-wrapper">
                            <input type="password" id="password" required placeholder=" "/>
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
    </body>
</html>
