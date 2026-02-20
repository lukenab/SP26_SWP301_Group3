function toggleAvatarInput() {
    var inputDiv = document.getElementById("avatarInputContainer");
    var visualInput = document.getElementById("avatarVisualInput");
    inputDiv.classList.toggle("active");

    if (inputDiv.classList.contains("active")) {
        visualInput.focus();
    }
}
function updateAvatar() {
    var newUrl = document.getElementById("avatarVisualInput").value;

    var imgPreview = document.querySelector(".info-img img");
    if (newUrl.trim() !== "") {
        imgPreview.src = newUrl;
    }
    document.getElementById("uAvatar").value = newUrl;
}
