<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forgot Password</title>
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

            <div class="login-card" style="height: 340px">
                <div class="login-header">
                    <h2>Forgot Password</h2>
                    <p>Enter your email to receive a new password</p>
                </div>

                <form class="login-form" action="forgotPassword" method="post" style="height: 150px">
                    <div class="form-group">
                        <div class="input-wrapper">
                            <input type="email" id="email" name="email" required placeholder=" " style="height: 44px" />
                            <label for="email">Your Email</label>
                            <span class="input-border"></span>
                        </div>
                    </div>

                    <div class="form-buttons" style="align-items: center;">
                        <a href="login" class="btn btn-cancel">Cancel</a>
                        <button type="submit" class="btn btn-save" style="height: 41px">
                             Send Password
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>