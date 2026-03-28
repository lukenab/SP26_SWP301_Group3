function switchTab(tabId, clickedElement) {
    var contents = document.querySelectorAll('.tab-content');
    contents.forEach(function(content) {
        content.style.display = 'none';
    });
    
    var tabs = document.querySelectorAll('.form-tab');
    tabs.forEach(function(tab) {
        tab.classList.remove('active');
    });
    
    document.getElementById(tabId).style.display = 'block';
    
    clickedElement.classList.add('active');
    
}

function togglePasswordVisibility(inputId, iconElement) {
    const passwordInput = document.getElementById(inputId);
    
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        iconElement.classList.replace('bx-hide', 'bx-show');
    } else {
        passwordInput.type = "password";
        iconElement.classList.replace('bx-show', 'bx-hide');
    }
}