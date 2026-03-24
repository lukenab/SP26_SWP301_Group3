document.addEventListener("DOMContentLoaded", function() {
    var lockModalEl = document.getElementById('lockUserModal');
    if (lockModalEl) {
        document.body.appendChild(lockModalEl);
    }
    
    var resetModalEl = document.getElementById('resetPasswordModal');
    if (resetModalEl) {
        document.body.appendChild(resetModalEl);
    }
});

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
    
    var resetModalEl = document.getElementById('resetPasswordModal');
    var resetModal = bootstrap.Modal.getOrCreateInstance(resetModalEl);
    resetModal.show();
}

function showLoadingBtn() {
    var btn = document.getElementById('confirmResetBtn');
    btn.innerHTML = "<i class='bx bx-loader-alt bx-spin'></i> Sending Email...";
    btn.disabled = true; // Khóa nút lại
    btn.style.opacity = "0.7";
}

function openLockModal(userId, fullName, isLocking) {
    document.getElementById('lockModalUserId').value = userId;
    document.getElementById('lockModalVal').value = isLocking;
    
    document.getElementById('lockUserName').innerText = fullName;
    
    var header = document.getElementById('lockModalHeader'); 
    var titleText = document.getElementById('lockModalTitleText');
    var actionText = document.getElementById('lockActionText');
    var warningDiv = document.getElementById('lockWarningMessage');
    var warningText = document.getElementById('lockWarningText');
    var icon = document.getElementById('lockModalIcon');
    var confirmBtn = document.getElementById('confirmLockBtn');

    if (isLocking) { 
        //Lock State
        header.style.backgroundColor = "#FEE2E2"; 
        header.style.borderBottom = "1px solid #FCA5A5";
        
        titleText.innerText = " Lock Account";
        titleText.style.color = "#DC2626"; 
        
        actionText.innerText = "LOCK";
        actionText.style.color = "#DC2626"; 
        
        icon.className = "bx bxs-lock";
        icon.style.color = "#DC2626";
        
        warningDiv.style.backgroundColor = "#FEF2F2";
        warningDiv.style.color = "#991B1B";
        warningDiv.style.borderLeftColor = "#DC2626";
        warningText.innerText = "This user will immediately lose access and cannot log into the system until you unlock them.";
        
        confirmBtn.innerText = "Lock User";
        confirmBtn.className = "btn btn-lock"; 
    } else { 
        //Unlock State
        header.style.backgroundColor = "#D1FAE5"; 
        header.style.borderBottom = "1px solid #6EE7B7";
        
        titleText.innerText = " Unlock Account";
        titleText.style.color = "#059669"; 
        
        actionText.innerText = "UNLOCK";
        actionText.style.color = "#059669"; 
        
        icon.className = "bx bx-lock-open-alt";
        icon.style.color = "#059669";
        
        warningDiv.style.backgroundColor = "#ECFDF5";
        warningDiv.style.color = "#065F46";
        warningDiv.style.borderLeftColor = "#059669";
        warningText.innerText = "This user's login access will be restored immediately.";
        
        confirmBtn.innerText = "Unlock User";
        confirmBtn.className = "btn btn-unlock"; 
    }
    var lockModalEl = document.getElementById('lockUserModal');
    var lockModal = bootstrap.Modal.getOrCreateInstance(lockModalEl);
    lockModal.show();
}

document.addEventListener('DOMContentLoaded', function() {
    const passwordInput = document.getElementById('password');
    const passwordToggle = document.getElementById('passwordToggle');

    if (passwordToggle && passwordInput) {
        passwordToggle.addEventListener('click', function() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);

            this.classList.toggle('is-visible');
            
            const iconContainer = this.querySelector('.toggle-icon');
            if (type === 'text') {
                iconContainer.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M3.98 8.223A10.477 10.477 0 0 0 1.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.451 10.451 0 0 1 12 4.5c4.756 0 8.773 3.162 10.065 7.498a10.522 10.522 0 0 1-4.293 5.774M6.228 6.228 3 3m3.228 3.228 3.65 3.65m7.822 7.822L21 21m-2.228-2.228-3.65-3.65m0 0a3 3 0 1 0-4.243-4.243m4.242 4.242L9.88 9.88" /></svg>`;
            } else {
                iconContainer.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z"/><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" /></svg>`;
            }
        });
    }
});