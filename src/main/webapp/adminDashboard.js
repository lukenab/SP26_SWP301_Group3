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

const profileItem = document.querySelector(".profile_item");

if (profileItem) {
    profileItem.addEventListener("click", (e) => {
        e.stopPropagation(); 
        profileItem.classList.toggle("active");
    });
}

document.addEventListener("click", (e) => {
    if (!profileItem.contains(e.target)) {
        profileItem.classList.remove("active");
    }
});