<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Set New Password</title>
        <link href="css/login.css" rel="stylesheet" type="text/css"/>
        <link href="css/editUser.css" rel="stylesheet" type="text/css"/>
        <link href="css/forgotPassword.css" rel="stylesheet" type="text/css"/>
        <link href='https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css' rel='stylesheet'>
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

            <div class="login-card" style="height: 450px">
                <div class="login-header">
                    <h2>Reset Password</h2>
                    <p>Please enter your new password below.</p>
                </div>

                <form class="login-form" action="forgotPassword" method="post">
                    <input type="hidden" name="action" value="updatePassword">
                    
                    <div class="form-group">
                        <div class="input-wrapper password-wrapper">
                            <input type="password" id="newPassword" name="newPassword" required placeholder=" " style="height: 44px" />
                            <label for="newPassword">New Password</label>
                            <span class="input-border"></span>
                        </div>
                    </div>

                    <div class="form-group" style="margin-top: 20px;">
                        <div class="input-wrapper password-wrapper">
                            <input type="password" id="confirmPassword" name="confirmPassword" required placeholder=" " style="height: 44px" />
                            <label for="confirmPassword">Confirm New Password</label>
                            <span class="input-border"></span>
                        </div>
                    </div>

                    <div class="form-buttons" style="align-items: center; margin-top: 35px;">
                        <a href="login" class="btn btn-cancel">Cancel</a>
                        <button type="submit" class="btn btn-save" style="height: 41px">
                             Save Password
                        </button>
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
        
        <script src="js/manageUser.js" type="text/javascript"></script>
    </body>
</html>