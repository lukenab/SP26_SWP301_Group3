<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${course.courseName} - Course Details</title>
        <link href="https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css" rel="stylesheet"/>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/courseDetailPublic.css" rel="stylesheet" type="text/css"/>
        <link href="css/landingPage.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty course}">
                <div class="empty-state">
                    <h2>Course not found</h2>
                    <a href="${pageContext.request.contextPath}/landingPage">Back to landing page</a>
                </div>
            </c:when>
            <c:otherwise>
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
                                    <input type="text" id="courseSearchInput" placeholder="Search for courses..." />
                                </div>
                            </div>

                            <div class="nav-right">
                                <div class="login-btn">
                                    <a type="button" href="login">Login</a>
                                </div>

                                <div class="contact-btn">
                                    <a class="nav-primary" href="${pageContext.request.contextPath}/landingPage#contact">Contact</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </nav>

                <section class="breadcrumb-wrap">
                    <div class="container">
                        <a href="${pageContext.request.contextPath}/landingPage">Home</a>
                        <span><i class='bx bx-chevron-right'></i></span>
                        <span>Courses</span>
                        <span><i class='bx bx-chevron-right'></i></span>
                        <span class="crumb-current">${course.courseName}</span>
                    </div>
                </section>

                <header class="hero">
                    <div class="container hero-inner">
                        <div class="hero-main">
                            <h1>${course.courseName}</h1>
                            <p>
                                ${course.description}
                            </p>
                            <div class="hero-meta">
                                <span><strong>4.8</strong> <i class='bx bxs-star'></i><i class='bx bxs-star'></i><i class='bx bxs-star'></i><i class='bx bxs-star'></i><i class='bx bxs-star'></i> (1,240 ratings)</span>
                                <span><i class='bx bx-user'></i> 850 students enrolled</span>
                                <span><i class='bx bx-world'></i> English / Vietnamese</span>
                            </div>
                            <div class="hero-meta muted">
                                <span><i class='bx bx-refresh'></i> Last updated: March 2026</span>
                                <span><i class='bx bx-book-open'></i> ${course.totalSlots} sessions</span>
                            </div>
                        </div>
                    </div>
                </header>

                <main class="container detail-layout">
                    <section class="content-col">
                        <div class="tabs-box">
                            <button class="tab active" type="button">Overview</button>
                            <button class="tab" type="button">Curriculum</button>
                            <button class="tab" type="button">Instructor</button>
                            <button class="tab" type="button">Reviews</button>
                        </div>

                        <article class="content-box">
                            <h2>What you'll learn</h2>
                            <div class="learn-grid">
                                <div><i class='bx bx-check-shield'></i> Build communication confidence in real contexts</div>
                                <div><i class='bx bx-check-shield'></i> Improve grammar and vocabulary for daily use</div>
                                <div><i class='bx bx-check-shield'></i> Practice listening and speaking with guided tasks</div>
                                <div><i class='bx bx-check-shield'></i> Follow a clear study roadmap for each session</div>
                                <div><i class='bx bx-check-shield'></i> Learn exam and classroom strategies effectively</div>
                                <div><i class='bx bx-check-shield'></i> Track progress with practical feedback</div>
                            </div>

                            <h3>Course Description</h3>
                            <p>${course.description}</p>
                            <p>
                                This program is designed to help learners build a strong English foundation and apply it in real-life communication.
                                Each session combines structured instruction, guided practice, and immediate feedback from instructors.
                            </p>

                            <h3>Requirements</h3>
                            <ul>
                                <li>A device with internet connection</li>
                                <li>Commitment to complete weekly practice</li>
                                <li>Placement level suitable for this course</li>
                            </ul>
                        </article>
                    </section>

                    <aside class="enroll-col">
                        <div class="enroll-card">
                            <div class="preview">
                                <img src="${pageContext.request.contextPath}/images/${course.images}" alt="${course.courseName}">
                                <span>Preview this course</span>
                            </div>
                            <div class="price-block">
                                <h3><fmt:formatNumber pattern="#,###" value="${course.tuitionFee}" /> VND</h3>
                                <p>Limited seats available</p>
                            </div>
                            <a href="${pageContext.request.contextPath}/landingPage#contact" class="btn-enroll">Enroll Now</a>
                            <a href="${pageContext.request.contextPath}/landingPage#contact" class="btn-outline">Get Free Consultation</a>

                            <hr>
                            <h4>This course includes:</h4>
                            <ul class="include-list">
                                <li><i class='bx bx-time-five'></i> ${course.totalSlots} instructor-led sessions</li>
                                <li><i class='bx bx-file'></i> Learning materials included</li>
                                <li><i class='bx bx-certification'></i> Completion confirmation</li>
                                <li><i class='bx bx-support'></i> Ongoing support from consultant team</li>
                            </ul>
                        </div>
                    </aside>
                </main>
            </c:otherwise>
        </c:choose>
    </body>
</html>
