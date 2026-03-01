document.addEventListener("DOMContentLoaded", function () {
    const toast = document.getElementById("toastMessage");
    if (toast) {
        setTimeout(() => {
            toast.style.opacity = "0";
            setTimeout(() => toast.remove(), 400);
        }, 3000);
    }
});