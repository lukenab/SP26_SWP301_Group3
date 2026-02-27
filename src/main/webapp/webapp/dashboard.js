document.addEventListener("DOMContentLoaded", () => {
    const nav = document.querySelector("nav");
    const sidebar = document.querySelector(".sidebar");
    const sidebarOpen = document.querySelector(".sidebarOpen");
    const body = document.querySelector("body");
    const modeToggle = document.querySelector(".darkMode");

    if (sidebarOpen) {
        sidebarOpen.addEventListener("click", () => {
            sidebar.classList.toggle("close");
            nav.classList.toggle("active");
        });
    }

    const profileItem = document.querySelector(".profile-item");
    if (profileItem) {
        profileItem.addEventListener("click", (e) => {
            e.stopPropagation();
            profileItem.classList.toggle("active");
        });
    }

    document.addEventListener("click", (e) => {
        if (profileItem && !profileItem.contains(e.target)) {
            profileItem.classList.remove("active");
        }
    });

    if (modeToggle) {
        modeToggle.addEventListener("click", () => {
            modeToggle.classList.toggle("active");
            body.classList.toggle("dark");
        });
    }
    const currentPath = window.location.pathname;

    document.querySelectorAll(".menu-links a").forEach(link => {
        const href = link.getAttribute("href");

        if (!href || href === "#")
            return;

        if (currentPath.endsWith(href)) {
            link.classList.add("active");
        }
    });
});