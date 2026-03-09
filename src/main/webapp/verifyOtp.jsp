<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Verify OTP</title>
        <link href="css/login.css" rel="stylesheet" type="text/css"/>
        <link href="css/editUser.css" rel="stylesheet" type="text/css"/>
        <link href="css/forgotPassword.css" rel="stylesheet" type="text/css"/>
        <link href='https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css' rel='stylesheet'>
        <style>
            #otp {
                letter-spacing: 1em;
                text-align: center;
                font-size: 1.2rem;
                font-weight: bold;
                padding-left: 20px;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <c:if test="${not empty sessionScope.message}">
                <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
                    <div class="toast-content">
                        <span class="toast-title">${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}</span>
                        <span class="toast-message">${sessionScope.message}</span>
                    </div>
                </div>
                <c:remove var="message" scope="session" />
                <c:remove var="messageType" scope="session" />
            </c:if>

            <div class="login-card" style="height: 380px">
                <div class="login-header">
                    <h2>Verify OTP</h2>
                    <p>Enter the 6-digit code sent to <strong>${sessionScope.resetEmail}</strong></p>
                </div>

                <form class="login-form" action="forgotPassword" method="post">
                    <input type="hidden" name="action" value="verifyOTP">
                    
                    <div class="form-group">
                        <div class="input-wrapper">
                            <input type="text" id="otp" name="otp" required placeholder=" " maxlength="6" style="height: 50px" autocomplete="off" />
                            <label for="otp">6-digit OTP</label>
                            <span class="input-border"></span>
                        </div>
                    </div>

                    <div class="form-buttons" style="align-items: center; margin-top: 30px;">
                        <a href="login" class="btn btn-cancel">Cancel</a>
                        <button type="submit" class="btn btn-save" style="height: 41px">
                             Verify
                        </button>
                    </div>
                    
                    <div style="text-align: center; margin-top: 15px; font-size: 0.9rem;">
                        <span class="text-muted">Didn't receive code? </span>
                        <a href="forgotPassword.jsp" style="color: #4361ee; text-decoration: none; font-weight: 500;">Try again</a>
                    </div>
                </form>
            </div>
        </div>
        
        <script>
            setTimeout(function() {
                var toast = document.getElementById('toastMessage');
                if(toast) toast.style.display = 'none';
            }, 5000);
        </script>
    </body>
</html>