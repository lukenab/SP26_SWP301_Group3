function toggleExtraFields() {
    const roleId = document.getElementById('roleId').value;
    const dateContainer = document.getElementById('dynamicDateContainer');
    const dateLabel = document.getElementById('dateLabel');
    const dateInput = document.getElementById('dynamicDateInput');
    const employeeRow = document.getElementById('employeeExtraRow');

    if (!roleId)
        return;

    dateContainer.style.display = 'none';
    employeeRow.style.display = 'none';

    if (roleId === '2' || roleId === '3' || roleId === '4') {
        dateContainer.style.display = 'block';
        employeeRow.style.display = 'grid';
        dateLabel.innerText = "Hire Date";
        dateInput.name = "hireDate";
    } else if (roleId === '5') {
        dateContainer.style.display = 'block';
        dateLabel.innerText = "Enrollment Date";
        dateInput.name = "enrollmentDate";
    }
}
function toggleAvatarInput() {
    const inputDiv = document.getElementById("avatarInputContainer");
    const visualInput = document.getElementById("avatarVisualInput");

    if (inputDiv) {
        inputDiv.classList.toggle("active");
        if (inputDiv.classList.contains("active") && visualInput) {
            visualInput.focus();
        }
    }
}

function updateAvatar(inputElement) {
    const imgPreview = document.getElementById("mainAvatarPreview");
    if (inputElement.files && inputElement.files[0] && imgPreview) {
        const reader = new FileReader();
        reader.onload = function (e) {
            imgPreview.src = e.target.result;
        };
        reader.readAsDataURL(inputElement.files[0]);
    }
}

document.addEventListener('DOMContentLoaded', function () {
    toggleExtraFields();
    const dobInput = document.getElementById('dobInput');
    if (dobInput) {
        const today = new Date().toISOString().split('T')[0];
        dobInput.setAttribute('max', today);
    }
});