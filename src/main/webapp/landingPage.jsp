<%-- 
    Document   : landingPage
    Created on : Jan 31, 2026, 3:51:25 PM
    Author     : Legion
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/landingPage.css" rel="stylesheet" type="text/css"/>
        <link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
        <title>Landing Page</title>
    </head>
    <body>
        <c:if test="${not empty promoVoucher}">
            <div class="promo-popup-backdrop" id="promoVoucherPopup">
                <div class="promo-popup-card">
                    <button type="button" class="promo-popup-close" id="promoPopupClose" aria-label="Close">
                        <i class='bx bx-x'></i>
                    </button>
                    <div class="promo-popup-topline">
                        <i class='bx bx-rocket'></i>
                        <span>Start your English journey</span>
                    </div>
                    <h2 class="promo-popup-title"><span class="promo-script-light">Unlock Your</span><span class="promo-script-gold">Future</span></h2>
                    <p class="promo-popup-subtitle">With English</p>

                    <div class="promo-voucher-panel">
                        <span class="promo-popup-badge">Exclusive Student Voucher</span>
                        <div class="promo-popup-code" id="promoVoucherCode">${promoVoucher.code}</div>
                        <div class="promo-popup-discount-line">
                            Get
                            <strong>
                                <c:choose>
                                    <c:when test="${promoVoucher.discountAmount != null && promoVoucher.discountAmount > 0}">
                                        <fmt:formatNumber value="${promoVoucher.discountAmount}" type="number"/> VND
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${promoVoucher.discountPercent}" type="number"/>%
                                    </c:otherwise>
                                </c:choose>
                            </strong>
                            OFF
                        </div>
                    </div>

                    <div class="promo-popup-meta">
                        <i class='bx bx-time-five'></i>
                        <span>Valid until <strong><fmt:formatDate value="${promoVoucher.validUntil}" pattern="dd/MM/yyyy"/></strong></span>
                    </div>

                    <div class="promo-popup-actions">
                        <a href="#contact" class="promo-primary-btn" id="promoRegisterBtn">
                            <i class='bx bx-rocket'></i>
                            Claim Your Offer Now
                            <i class='bx bx-chevron-right'></i>
                        </a>
                    </div>

                    <div class="promo-popup-links">
                        <button type="button" class="promo-copy-btn" id="promoCopyBtn">Copy Code</button>
                        <button type="button" class="promo-secondary-btn" id="promoPopupHideBtn">Maybe later</button>
                    </div>
                    <div class="promo-popup-footnote">Your future starts here.</div>
                </div>
            </div>
        </c:if>

        <nav>
            <div class="navbar"> 
                <div class="nav-items">
                    <div class="nav-left">
                        <a href="#">
                            <div class="logo-header">
                                <img src="images/logo.png" alt="logo" />
                            </div>

                            <div class="logo-text">
                                <span class="name">LMCS</span>
                                <span class="profession">Language Center</span>
                            </div>
                        </a>

                        <div class="search-field">
                            <i class="bx bx-search"></i>
                            <input type="text" id="courseSearchInput" placeholder="Search for courses..." />
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
                </div>

                <div class="col-6">
                    <div class="consultation-card">
                        <div class="consultation-card-header">
                            <h4>Get Free Consultation</h4>
                            <p class="text-muted small">Placement Test + Roadmap Advice + Free Trial</p>
                        </div>

                        <div class="consultation-card-body p-4">
                            <c:if test="${not empty sessionScope.message}">
                                <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
                                    <div class="toast-icon">
                                        <c:choose>
                                            <c:when test="${sessionScope.messageType == 'success'}">
                                                <i class='bx bx-check-circle'></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class='bx bx-cross-circle'></i>
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

                            <form action="landingPage" method="POST">
                                <input type="hidden" name="action" value="createLead">
                                <div class="mb-3">
                                    <label class="text-white small mb-1">Full Name</label>
                                    <input type="text" name="fullName" class="form-control" placeholder="Nguyen Van A" required>
                                </div>
                                <div class="mb-3">
                                    <label class="text-white small mb-1">Phone Number</label>
                                    <input type="tel" name="phone" class="form-control" placeholder="0812.154.005" required>
                                </div>
                                <div class="mb-3">
                                    <label class="text-white small mb-1">Email Address</label>
                                    <input type="email" name="email" class="form-control" placeholder="email@example.com">
                                </div>
                                <div class="mb-3">
                                    <label class="text-white small mb-1">Interested Course</label>
                                    <select class="form-select" name="interestedCourseID" required>
                                        <option value="" selected disabled>Select a course...</option>
                                        <c:forEach items="${courseList}" var="c">
                                            <option value="${c.courseId}">${c.courseName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="text-white small mb-1">Message</label>
                                    <input type="text" name="note" class="form-control" placeholder="Tell us more about your goals...">
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
                            <input class="form-check-input course-category-filter" type="checkbox" id="category-ielts" value="IELTS">
                            <label class="form-check-label" for="category-ielts">IELTS</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input course-category-filter" type="checkbox" id="category-toeic" value="TOEIC">
                            <label class="form-check-label" for="category-toeic">TOEIC</label>
                        </div>

                        <div class="form-check">
                            <input class="form-check-input course-category-filter" type="checkbox" id="category-toefl" value="TOEFL">
                            <label class="form-check-label" for="category-toefl">TOEFL</label>
                        </div>

                        <h6 class="mt-3">Price</h6>
                        <div class="form-check">
                            <input class="form-check-input course-price-filter" type="radio" name="price" id="price-all" value="all" checked>
                            <label class="form-check-label" for="price-all">All</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input course-price-filter" type="radio" name="price" id="price-free" value="free">
                            <label class="form-check-label" for="price-free">Free</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input course-price-filter" type="radio" name="price" id="price-paid" value="paid">
                            <label class="form-check-label" for="price-paid">Paid</label>
                        </div>

                        <hr>
                        <h6>Level</h6>
                        <div class="form-check">
                            <input class="form-check-input course-level-filter" type="checkbox" id="level-beginner" value="BEGINNER">
                            <label class="form-check-label" for="level-beginner">Beginner</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input course-level-filter" type="checkbox" id="level-intermediate" value="INTERMEDIATE">
                            <label class="form-check-label" for="level-intermediate">Intermediate</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input course-level-filter" type="checkbox" id="level-advanced" value="ADVANCED">
                            <label class="form-check-label" for="level-advanced">Advanced</label>
                        </div>
                    </div>
                </div>

                <div class="col-md-9 explore-course">
                    <h3 class="mb-4 text-center fw-bold">Explore Courses</h3>

                    <div class="row row-cols-md-3 g-4">
                        <c:forEach items="${courseList}" var="c">
                            <c:set var="searchText" value="${fn:toLowerCase(c.courseName)} ${fn:toLowerCase(c.description)}" />
                            <c:set var="courseCategory" value="OTHER" />
                            <c:if test="${fn:contains(searchText, 'ielts')}">
                                <c:set var="courseCategory" value="IELTS" />
                            </c:if>
                            <c:if test="${fn:contains(searchText, 'toeic')}">
                                <c:set var="courseCategory" value="TOEIC" />
                            </c:if>
                            <c:if test="${fn:contains(searchText, 'toefl')}">
                                <c:set var="courseCategory" value="TOEFL" />
                            </c:if>

                            <c:set var="courseLevel" value="OTHER" />
                            <c:if test="${fn:contains(searchText, 'beginner') || fn:contains(searchText, 'foundation') || fn:contains(searchText, 'basic')}">
                                <c:set var="courseLevel" value="BEGINNER" />
                            </c:if>
                            <c:if test="${fn:contains(searchText, 'intermediate') || fn:contains(searchText, 'intensive')}">
                                <c:set var="courseLevel" value="INTERMEDIATE" />
                            </c:if>
                            <c:if test="${fn:contains(searchText, 'advanced') || fn:contains(searchText, 'expert')}">
                                <c:set var="courseLevel" value="ADVANCED" />
                            </c:if>

                            <div class="col course-item"
                                 data-name="${fn:toLowerCase(c.courseName)}"
                                 data-desc="${fn:toLowerCase(c.description)}"
                                 data-fee="${c.tuitionFee}"
                                 data-category="${courseCategory}"
                                 data-level="${courseLevel}">
                                <div class="card course-card">
                                    <img src="images/${c.images}" class="card-img-top" alt="${c.courseName}">

                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title">${c.courseName}</h5>
                                        <p class="card-text text-muted small course-desc">${c.description}</p>

                                        <div class="mt-auto d-flex justify-content-between align-items-center">
                                            <span class="price-tag">
                                                <fmt:formatNumber pattern="#,###" value="${c.tuitionFee}" /> VND                                           
                                            </span>
                                            <div class="courseDetail-btn">
                                                <a href="${pageContext.request.contextPath}/course?action=publicDetails&courseId=${c.courseId}">View Details</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <p id="emptyCourseMessage" class="text-center text-muted mt-4 d-none">No courses match your filters.</p>
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
                                    <i class="fa-solid fa-angle-right"></i>About
                                </a>
                            </li>

                            <li>
                                <a href="#projects" class="footer-icon">
                                    <i class="fa-solid fa-angle-right"></i>Projects
                                </a>
                            </li>
                            <li>
                                <a href="#home" class="footer-icon">
                                    <i class="fa-solid fa-angle-right"></i>Home
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
        <script src="js/landingPage.js" type="text/javascript"></script>
        <c:if test="${not empty promoVoucher}">
            <script>
                (function () {
                    const popup = document.getElementById('promoVoucherPopup');
                    const closeBtn = document.getElementById('promoPopupClose');
                    const laterBtn = document.getElementById('promoPopupHideBtn');
                    const registerBtn = document.getElementById('promoRegisterBtn');
                    const copyBtn = document.getElementById('promoCopyBtn');
                    const codeEl = document.getElementById('promoVoucherCode');
                    const contactSection = document.getElementById('contact');

                    if (!popup) {
                        return;
                    }

                    const closePopup = function () {
                        popup.classList.add('promo-popup-hidden');
                    };

                    closeBtn.addEventListener('click', function () {
                        closePopup();
                    });

                    laterBtn.addEventListener('click', function () {
                        closePopup();
                    });

                    registerBtn.addEventListener('click', function (event) {
                        event.preventDefault();
                        closePopup();

                        if (contactSection) {
                            setTimeout(function () {
                                contactSection.scrollIntoView({behavior: 'smooth', block: 'start'});
                            }, 150);
                        }
                    });

                    popup.addEventListener('click', function (event) {
                        if (event.target === popup) {
                            closePopup();
                        }
                    });

                    copyBtn.addEventListener('click', async function () {
                        try {
                            await navigator.clipboard.writeText(codeEl.textContent.trim());
                            copyBtn.textContent = 'Copied';
                        } catch (error) {
                            copyBtn.textContent = 'Copy Failed';
                        }
                    });
                })();
            </script>
        </c:if>
    </body>
</html>
