const body = document.querySelector("body"),
        nav = document.querySelector("nav"),
        modeToggle = document.querySelector(".darkLight"),
        searchToggle = document.querySelector(".searchToggle"),
        toggle = document.querySelector(".toggle"),
        sidebar = document.querySelector(".sidebar"),
        sidebarOpen = document.querySelector(".sidebarOpen");

modeToggle.addEventListener("click", () => {
    modeToggle.classList.toggle("active");
    body.classList.toggle("dark");
});

searchToggle.addEventListener("click", () => {
    searchToggle.classList.toggle("active");
});

sidebarOpen.addEventListener("click", () => {
    sidebar.classList.toggle("close");
    nav.classList.toggle("active");
});
