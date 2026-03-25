function toggleExtraFields() {
    const roleId = document.getElementById('roleId').value;
    const employeeFields = document.getElementById('employeeFields');
    const studentFields = document.getElementById('studentFields');

    employeeFields.style.display = 'none';
    studentFields.style.display = 'none';

    if (roleId === '2' || roleId === '3' || roleId === '4') {
        employeeFields.style.display = 'block';
    } else if (roleId === '5') {
        studentFields.style.display = 'block';
    }
}

function toggleAvatarInput() {
    var inputDiv = document.getElementById("avatarInputContainer");
    var visualInput = document.getElementById("avatarVisualInput");
    inputDiv.classList.toggle("active");

    if (inputDiv.classList.contains("active")) {
        visualInput.focus();
    }
}

function updateAvatar(inputElement) {
    var imgPreview = document.getElementById("mainAvatarPreview");
    if (inputElement.files && inputElement.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            imgPreview.src = e.target.result;
        };
        reader.readAsDataURL(inputElement.files[0]);
    }
}

const dobInput = document.getElementById('dobInput');
if (dobInput) {
    const today = new Date().toISOString().split('T')[0];
    dobInput.setAttribute('max', today);
}