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