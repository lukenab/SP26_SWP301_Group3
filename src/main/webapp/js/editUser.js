function toggleAvatarInput() {
    var inputDiv = document.getElementById("avatarInputContainer");
    var visualInput = document.getElementById("avatarVisualInput");
    inputDiv.classList.toggle("active");

    if (inputDiv.classList.contains("active")) {
        visualInput.focus();
    }
}
function updateAvatar(inputElement) {
    var imgPreview = document.querySelector(".info-img img");

    if (inputElement.files && inputElement.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            imgPreview.src = e.target.result;
        };
        reader.readAsDataURL(inputElement.files[0]);
    }
}