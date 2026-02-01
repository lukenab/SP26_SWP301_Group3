<%-- 
    Document   : landingPage
    Created on : Jan 31, 2026, 3:51:25 PM
    Author     : Legion
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css" rel="stylesheet"/>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/landingPage.css" rel="stylesheet" type="text/css"/>
        <title>JSP Page</title>
    </head>
    <body>
        <nav>
            <div class="navbar"> 
                <div class="nav-items">
                    <div class="nav-left">
                        <div class="logo-header">
                            <img src="images/logo.png" alt="logo" />
                        </div>

                        <div class="logo-text">
                            <span class="name">LMCS</span>
                            <span class="profession">Language Center</span>
                        </div>

                        <div class="search-field">
                            <i class="bx bx-search"></i>
                            <input type="text" placeholder="Search for courses..." />
                        </div>
                    </div>

                    <div class="nav-right">
                        <div class="login-btn">
                            <a type="button" href="login">Login</a>
                        </div>

                        <div class="contact-btn">
                            <a type="button">Contact</a>
                        </div>
                    </div>
                </div>
            </div>
        </nav>

        <div class="main-content">
            <div id="demoCarousel" class="carousel slide" data-bs-ride="carousel">
                <div class="carousel-indicators">
                    <button type="button" data-bs-target="#demoCarousel" data-bs-slide-to="0" class="active"></button>
                    <button type="button" data-bs-target="#demoCarousel" data-bs-slide-to="1"></button>
                    <button type="button" data-bs-target="#demoCarousel" data-bs-slide-to="2"></button>
                </div>

                <div class="carousel-inner">
                    <div class="carousel-item active">
                        <img src="images/carousel1.jpg" class="d-block w-100" alt="Slide 1">
                    </div>
                    <div class="carousel-item">
                        <img src="images/carousel2.jpg" class="d-block w-100" alt="Slide 2">
                    </div>
                    <div class="carousel-item">
                        <img src="images/carousel3.jpg" class="d-block w-100" alt="Slide 3">
                    </div>
                </div>

                <button class="carousel-control-prev" type="button" data-bs-target="#demoCarousel" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon"></span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#demoCarousel" data-bs-slide="next">
                    <span class="carousel-control-next-icon"></span>
                </button>
            </div>
        </div>

        <div class="consultation-content container">
            <div class="row align-items-center">
                <div class="col-6 text-content">
                    <h1>Vietnam's Leading <br> English Training <br> System</h1>
                    <ul class="list-unstyled feature-list">
                        <li><i class='bx bxs-check-circle'></i> Proven and effective learning methodology</li>
                        <li><i class='bx bxs-check-circle'></i> Experienced Native and Vietnamese instructors</li>
                        <li><i class='bx bxs-check-circle'></i> Output guarantee & lifetime learning support</li>
                        <li><i class='bx bxs-check-circle'></i> Official Partner of Cambridge & IDP (IELTS Venue)</li>
                        <li><i class='bx bxs-check-circle'></i> Free trial class - 0% interest installment support</li>
                    </ul>

                    <button class="join-free-btn">
                        <i class='bx bx-book-open me-2'></i>Join Free Trial Class
                    </button>
                </div>

                <div class="col-6">
                    <div class="consultation-card">
                        <div class="consultation-card-header">
                            <h4>Get Free Consultation</h4>
                            <p class="text-muted small">Placement Test + Roadmap Advice + Free Trial</p>
                        </div>

                        <div class="consultation-card-body p-4">
                            <form>
                                <div class="mb-3">
                                    <label class="text-white small mb-1">Full Name</label>
                                    <input type="text" class="form-control" placeholder="Nguyen Van A" required>
                                </div>
                                <div class="mb-3">
                                    <label class="text-white small mb-1">Phone Number</label>
                                    <input type="tel" class="form-control" placeholder="0812.154.005" required>
                                </div>
                                <div class="mb-3">
                                    <label class="text-white small mb-1">Email Address</label>
                                    <input type="email" class="form-control" placeholder="email@example.com">
                                </div>
                                <div class="mb-3">
                                    <label class="text-white small mb-1">Interested Course</label>
                                    <select class="form-select">
                                        <option selected disabled>Select a course...</option>
                                        <option value="#"></option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="text-white small mb-1">Message</label>
                                    <input type="text" class="form-control" placeholder="Tell us more about your goals..."></input>
                                </div>

                                <button type="submit" class="register-btn">
                                    <i class='bx bx-paper-plane me-2'></i>Register Now - Get 30% Off
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="bootstrap.bundle.min.js">
            
        </script>
    </body>
</html>
