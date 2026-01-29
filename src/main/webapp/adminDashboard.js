document.addEventListener("DOMContentLoaded", () => {
    const body = document.querySelector("body");
    const nav = document.querySelector("nav");
    const modeToggle = document.querySelector(".darkLight");
    const sidebar = document.querySelector(".sidebar");
    const sidebarOpen = document.querySelector(".sidebarOpen");

    modeToggle.addEventListener("click", () => {
        modeToggle.classList.toggle("active");
        body.classList.toggle("dark");
    });

    sidebarOpen.addEventListener("click", () => {
        sidebar.classList.toggle("close");
        nav.classList.toggle("active");
    });
});
