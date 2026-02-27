function closeToast() {
    var toast = document.getElementById("toastMessage");
    if (toast) {

        toast.style.animation = "fadeOut 0.5s ease forwards";
        setTimeout(function () {
            toast.remove();
        }, 500);
    }
}

function openResetModal(userId, email, fullName) {
    document.getElementById('resetUserName').innerText = fullName;
    document.getElementById('resetUserEmail').innerText = email;
    
    document.getElementById('modalUserId').value = userId;
    document.getElementById('modalUserEmailInput').value = email;
    
    var resetModal = new bootstrap.Modal(document.getElementById('resetPasswordModal'));
    resetModal.show();
}

function showLoadingBtn() {
    var btn = document.getElementById('confirmResetBtn');
    btn.innerHTML = "<i class='bx bx-loader-alt bx-spin'></i> Sending Email...";
    btn.disabled = true; // Khóa nút lại
    btn.style.opacity = "0.7";
}

var resetModalEl = document.getElementById('resetPasswordModal');
if (resetModalEl) {
    document.body.appendChild(resetModalEl);
}
