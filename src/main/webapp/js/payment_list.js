// Auto show toast if exists
window.addEventListener('DOMContentLoaded', function () {
    const toast = document.getElementById('toastMessage');
    if (toast) {
        toast.style.display = 'flex';
        setTimeout(() => {
            toast.classList.add('show');
        }, 100);
        setTimeout(() => {
            closeToast();
        }, 5000);
    }
});

function closeToast() {
    const toast = document.getElementById('toastMessage');
    if (toast) {
        toast.classList.remove('show');
        setTimeout(() => {
            toast.style.display = 'none';
        }, 300);
    }
}

function viewEvidence(imageUrl, studentName) {
    document.getElementById('evidenceStudentName').textContent = studentName;
    document.getElementById('evidenceImage').src = imageUrl;
    const modal = new bootstrap.Modal(document.getElementById('evidenceModal'), {
        backdrop: false,
        keyboard: true
    });
    modal.show();
}

function approvePayment(paymentId, studentName) {
    document.getElementById('approvePaymentId').value = paymentId;
    document.getElementById('approveStudentName').textContent = studentName;
    const modal = new bootstrap.Modal(document.getElementById('approveModal'), {
        backdrop: false,
        keyboard: true
    });
    modal.show();
}

function rejectPayment(paymentId, studentName) {
    document.getElementById('rejectPaymentId').value = paymentId;
    document.getElementById('rejectStudentName').textContent = studentName;
    const modal = new bootstrap.Modal(document.getElementById('rejectModal'), {
        backdrop: false,
        keyboard: true
    });
    modal.show();
}