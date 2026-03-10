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
                <nav class="top-nav">
                    <div class="nav-inner">
                        <a class="brand-link" href="${pageContext.request.contextPath}/landingPage">
                            <img src="${pageContext.request.contextPath}/images/logo.png" alt="LMCS">
                            <span>LMCS</span>
                        </a>
                        <div class="nav-search">
                            <i class="bx bx-search"></i>
                            <input type="text" placeholder="Search for courses..." readonly>
                        </div>
                        <div class="nav-actions">
                            <a href="${pageContext.request.contextPath}/landingPage#content-2">Categories</a>
                            <a href="${pageContext.request.contextPath}/login">Login</a>
                            <a class="nav-primary" href="${pageContext.request.contextPath}/landingPage#contact">Contact</a>
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
                                <span>
                                    <strong><fmt:formatNumber value="${empty reviewCount ? 0 : averageRating}" minFractionDigits="1" maxFractionDigits="1"/></strong>
                                    <i class='bx bxs-star'></i>
                                    <span>(${reviewCount} reviews)</span>
                                </span>
                                <span><i class='bx bx-user'></i> ${reviewCount} learner reviews</span>
                                <span><i class='bx bx-world'></i> English / Vietnamese</span>
                            </div>
                            <div class="hero-meta muted">
                                <span><i class='bx bx-refresh'></i> Last updated: March 10, 2026</span>
                                <span><i class='bx bx-book-open'></i> ${course.totalSlots} sessions</span>
                            </div>
                        </div>
                    </div>
                </header>

                <main class="container detail-layout">
                    <section class="content-col">
                        <div class="tabs-box">
                            <button class="tab active" type="button" data-target="overview-panel">Overview</button>
                            <button class="tab" type="button" data-target="curriculum-panel">Curriculum</button>
                            <button class="tab" type="button" data-target="instructor-panel">Instructor</button>
                            <button class="tab" type="button" data-target="reviews-panel">Reviews</button>
                        </div>

                        <article class="content-box tab-panel active" id="overview-panel">
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

                        <article class="content-box tab-panel" id="curriculum-panel" hidden>
                            <h2>Curriculum</h2>
                            <p class="section-intro">
                                This section is mapped directly from the course syllabus.
                            </p>

                            <c:choose>
                                <c:when test="${empty syllabusList}">
                                    <div class="empty-content">
                                        <i class='bx bx-book-content'></i>
                                        <p>No syllabus has been published for this course yet.</p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="curriculum-list">
                                        <c:forEach items="${syllabusList}" var="item">
                                            <div class="curriculum-item">
                                                <div class="curriculum-order">Session ${item.orderIndex}</div>
                                                <div class="curriculum-body">
                                                    <h3>${item.topicName}</h3>
                                                    <p>
                                                        <c:choose>
                                                            <c:when test="${not empty item.description}">
                                                                ${item.description}
                                                            </c:when>
                                                            <c:otherwise>
                                                                Detailed content will be shared by the instructor during class.
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </article>

                        <article class="content-box tab-panel" id="instructor-panel" hidden>
                            <h2>Instructor</h2>

                            <c:choose>
                                <c:when test="${empty instructor}">
                                    <div class="empty-content">
                                        <i class='bx bx-user-x'></i>
                                        <p>No instructor has been assigned to this course yet.</p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="instructor-card">
                                        <div class="instructor-header">
                                            <img src="${instructor.avatar}" alt="${instructor.fullName}">
                                            <div>
                                                <h3>${instructor.fullName}</h3>
                                                <p class="instructor-role">Course Instructor</p>
                                                <div class="instructor-meta">
                                                    <span><i class='bx bx-briefcase'></i> ${empty instructorClassCount ? 0 : instructorClassCount} class(es) in this course</span>
                                                    <c:if test="${not empty instructorEmployee && not empty instructorEmployee.education}">
                                                        <span><i class='bx bx-award'></i> ${instructorEmployee.education}</span>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="instructor-grid">
                                            <div class="info-card">
                                                <span>Email</span>
                                                <strong>${empty instructor.email ? 'Updating' : instructor.email}</strong>
                                            </div>
                                            <div class="info-card">
                                                <span>Phone</span>
                                                <strong>${empty instructor.phone ? 'Updating' : instructor.phone}</strong>
                                            </div>
                                            <div class="info-card">
                                                <span>Location</span>
                                                <strong>${empty instructor.address ? 'Updating' : instructor.address}</strong>
                                            </div>
                                            <div class="info-card">
                                                <span>Hire Date</span>
                                                <strong>
                                                    <c:choose>
                                                        <c:when test="${not empty instructorEmployee && not empty instructorEmployee.hireDate}">
                                                            <fmt:formatDate value="${instructorEmployee.hireDate}" pattern="dd/MM/yyyy"/>
                                                        </c:when>
                                                        <c:otherwise>Updating</c:otherwise>
                                                    </c:choose>
                                                </strong>
                                            </div>
                                        </div>

                                        <div class="instructor-bio">
                                            <h3>Experience</h3>
                                            <p>
                                                <c:choose>
                                                    <c:when test="${not empty instructorEmployee && not empty instructorEmployee.experience}">
                                                        ${instructorEmployee.experience}
                                                    </c:when>
                                                    <c:otherwise>
                                                        Instructor profile details are being updated.
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </article>

                        <article class="content-box tab-panel" id="reviews-panel" hidden>
                            <div class="reviews-header">
                                <div>
                                    <h2>Reviews</h2>
                                    <p class="section-intro">Learner feedback collected from classes in this course.</p>
                                </div>
                                <div class="review-summary">
                                    <strong><fmt:formatNumber value="${empty reviewCount ? 0 : averageRating}" minFractionDigits="1" maxFractionDigits="1"/></strong>
                                    <span>Average rating</span>
                                </div>
                            </div>

                            <c:choose>
                                <c:when test="${empty reviewList}">
                                    <div class="empty-content">
                                        <i class='bx bx-message-rounded-x'></i>
                                        <p>No reviews yet for this course.</p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="review-list">
                                        <c:forEach items="${reviewList}" var="review">
                                            <div class="review-item">
                                                <div class="review-top">
                                                    <div>
                                                        <h3>${review[1]}</h3>
                                                        <p>${review[2]}<c:if test="${not empty review[3]}"> - Instructor: ${review[3]}</c:if></p>
                                                    </div>
                                                    <div class="review-rating">${review[4]}.0 <i class='bx bxs-star'></i></div>
                                                </div>
                                                <p class="review-comment">
                                                    <c:choose>
                                                        <c:when test="${not empty review[5]}">
                                                            ${review[5]}
                                                        </c:when>
                                                        <c:otherwise>
                                                            Student left a rating without written feedback.
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                                <div class="review-date">
                                                    <fmt:formatDate value="${review[6]}" pattern="dd/MM/yyyy HH:mm"/>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </article>
                    </section>

                    <aside class="enroll-col">
                        <div class="enroll-card">
                            <div class="preview">
                                <img src="${pageContext.request.contextPath}/images/${course.images}" alt="${course.courseName}">
                                <span>Preview this course</span>
                            </div>
                            <div class="price-block">
                                <h3><fmt:formatNumber type="currency" value="${course.tuitionFee}" /></h3>
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

                <section class="cta-section">
                    <div class="container cta-inner">
                        <h2>Start learning today - Sign up for free</h2>
                        <p>Join thousands of learners and accelerate your learning journey.</p>
                        <div class="cta-actions">
                            <a href="${pageContext.request.contextPath}/landingPage#contact" class="cta-primary">Get Free Consultation</a>
                            <a href="${pageContext.request.contextPath}/landingPage#content-2" class="cta-outline">Browse All Courses</a>
                        </div>
                    </div>
                </section>
            </c:otherwise>
        </c:choose>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const tabs = document.querySelectorAll('.tab');
                const panels = document.querySelectorAll('.tab-panel');

                tabs.forEach(function (tab) {
                    tab.addEventListener('click', function () {
                        const targetId = tab.dataset.target;

                        tabs.forEach(function (button) {
                            button.classList.remove('active');
                        });

                        panels.forEach(function (panel) {
                            const isActive = panel.id === targetId;
                            panel.classList.toggle('active', isActive);
                            panel.hidden = !isActive;
                        });

                        tab.classList.add('active');
                    });
                });
            });
        </script>
    </body>
</html>
