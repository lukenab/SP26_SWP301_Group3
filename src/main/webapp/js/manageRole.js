function openEditRoleModal(id, name, canUser, canCourse, canFinance) {
    document.getElementById('modalRoleId').value = id;
    document.getElementById('modalRoleName').innerText = name;
    
    let chkUser = document.getElementById('chkUser');
    let chkCourse = document.getElementById('chkCourse');
    let chkFinance = document.getElementById('chkFinance');

    chkUser.checked = canUser;
    chkCourse.checked = canCourse;
    chkFinance.checked = canFinance;
    
    if(id === 1) {
        chkUser.onclick = function() { return false; };
        chkCourse.onclick = function() { return false; };
        chkFinance.onclick = function() { return false; };
        chkUser.style.opacity = "0.5";
        chkCourse.style.opacity = "0.5";
        chkFinance.style.opacity = "0.5";
    } else {
        chkUser.onclick = null;
        chkCourse.onclick = null;
        chkFinance.onclick = null;
        chkUser.style.opacity = "1";
        chkCourse.style.opacity = "1";
        chkFinance.style.opacity = "1";
    }
    
    var myModalEl = document.getElementById('editRoleModal');
    var myModal = bootstrap.Modal.getOrCreateInstance(myModalEl);
    myModal.show();
}