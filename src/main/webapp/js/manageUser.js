function closeToast() {
    var toast = document.getElementById("toastMessage");
    if (toast) {

        toast.style.animation = "fadeOut 0.5s ease forwards";
        setTimeout(function () {
            toast.remove();
        }, 500);
    }
}
