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
    const menuLinks = Array.from(document.querySelectorAll(".menu-links a"));
    const currentUrl = new URL(window.location.href);
    const existingActiveLinks = menuLinks.filter(link => link.classList.contains("active"));

    if (existingActiveLinks.length > 1) {
        existingActiveLinks.forEach(link => link.classList.remove("active"));
    }

    if (existingActiveLinks.length === 0) {
        let matchedLink = null;

        for (const link of menuLinks) {
            const href = link.getAttribute("href");

            if (!href || href === "#") {
                continue;
            }

            const linkUrl = new URL(href, window.location.origin + window.location.pathname);
            const samePath = linkUrl.pathname === currentUrl.pathname;
            const linkAction = linkUrl.searchParams.get("action");
            const currentAction = currentUrl.searchParams.get("action");

            if (!samePath) {
                continue;
            }

            if (linkAction && linkAction === currentAction) {
                matchedLink = link;
                break;
            }

            if (!linkAction && !currentAction && !matchedLink) {
                matchedLink = link;
            }
        }

        if (matchedLink) {
            matchedLink.classList.add("active");
        }
    }
});
