<%-- 
    Document   : landingPage
    Created on : Jan 31, 2026, 3:51:25 PM
    Author     : Legion
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/landingPage.css" rel="stylesheet" type="text/css"/>
        <title>Landing Page</title>
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
                            <a href="#contact" type="button">Contact</a>
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
                        <img src="images/carousel1.png" class="d-block w-100" alt="Slide 1">
                    </div>
                    <div class="carousel-item">
                        <img src="images/carousel2.png" class="d-block w-100" alt="Slide 2">
                    </div>
                    <div class="carousel-item">
                        <img src="images/carousel3.png" class="d-block w-100" alt="Slide 3">
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

        <div class="consultation-content container" id="contact">
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

        <div class="container-fluid mt-5 px-4 px-lg-5" id="content-2">
            <div class="row">
                <div class="col-md-3" id="content-2-left">
                    <div class="course-filter">
                        <h5>Filters</h5>
                        <hr>
                        <h6>Category</h6>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="cat1">
                            <label class="form-check-label" for="cat1">IELTS</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="cat2">
                            <label class="form-check-label" for="cat2">TOEIC</label>
                        </div>

                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="cat2">
                            <label class="form-check-label" for="cat2">TOEFL</label>
                        </div>

                        <h6 class="mt-3">Price</h6>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="price" checked>
                            <label class="form-check-label">All</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="price">
                            <label class="form-check-label">Free</label>
                        </div>

                        <hr>
                        <h6>Level</h6>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="cat1">
                            <label class="form-check-label" for="cat1">Beginner</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="cat2">
                            <label class="form-check-label" for="cat2">Immediate</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="cat2">
                            <label class="form-check-label" for="cat2">Advanced</label>
                        </div>
                    </div>
                </div>

                <div class="col-md-9 explore-course">
                    <h3 class="mb-4 text-center fw-bold">Explore Courses</h3>

                    <div class="row row-cols-md-3 g-4">
                        <c:forEach items="${courseList}" var="c">
                            <div class="col">
                                <div class="card course-card">
                                    <img src="images/${c.images}" class="card-img-top" alt="${c.courseName}">

                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title">${c.courseName}</h5>
                                        <p class="card-text text-muted small course-desc">${c.description}</p>

                                        <div class="mt-auto d-flex justify-content-between align-items-center">
                                            <span class="price-tag">
                                                <fmt:formatNumber type="currency" value="${c.tuitionFee}" />                                           
                                            </span>
                                            <div class="courseDetail-btn">
                                                <a href="courseDetail?id=${c.courseId}">View Details</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <footer class="footer-sec">
            <div class="container">
                <div class="row footer-content">
                    <div class="col-md footer-1">
                        <ul>
                            <li class="footer-header"><strong>Quick links</strong></li>

                            <li>
                                <a href="#about" class="footer-icon">
                                    <i class="fa-solid fa-angle-right"></i>
                                    About
                                </a>
                            </li>


                            <li>
                                <a href="#projects" class="footer-icon">
                                    <i class="fa-solid fa-angle-right"></i>
                                    Projects
                                </a>
                            </li>


                            <li>
                                <a href="#home" class="footer-icon">
                                    <i class="fa-solid fa-angle-right"></i>
                                    Home
                                </a>
                            </li>


                        </ul>
                    </div>

                    <div class="col-md footer-2">
                        <ul>
                            <li class="footer-header"><strong>Contact</strong></li>
                            <li>
                                <i class="fa-regular fa-envelope"></i>
                               contact@lmcs.edu.vn
                            </li>
                            <li>
                                <i class="fa-solid fa-phone"></i>
                                0812154005
                            </li>
                            <li>
                                <i class="fa-regular fa-map"></i>
                                Can Tho, Viet Nam
                            </li>
                        </ul>
                    </div>

                    <div class="col-md footer-3">
                        <ul>
                            <li class="footer-header"><strong>Social Media</strong></li>
                            <li>
                                <a class="footer-icon" href="https://www.facebook.com/lukenab116" target="_blank"><i class="fa-brands fa-facebook social-icon"></i></a>
                                <a class="footer-icon" href="https://www.instagram.com/luke.nab/?fbclid=IwY2xjawO196FleHRuA2FlbQIxMABicmlkETFWQ3hQNTg3cjRqNktBbURLc3J0YwZhcHBfaWQQMjIyMDM5MTc4ODIwMDg5MgABHqhy8qa5ggH1oJDBH3FtcxXiMa22tNvSY6S6fKQ4iVwu5mRRATFqq27tPNFU_aem_91gByxpp21J25guID7ySoQ#" target="_blank"><i class="fa-brands fa-instagram social-icon"></i></a>
                                <i class="fa-brands fa-twitter social-icon"></i>
                            </li>
                            <li></li>
                        </ul>
                    </div>
                </div>
                <div class="copyright">
                    <p class="copyright">
                        Copyright &copy;2026 LMCS Language Center. All rights reserved 
                    </p>
                </div>
            </div>   
        </footer>

        <script src="js/bootstrap.bundle.min.js" type="text/javascript"></script>
    </body>
</html>
