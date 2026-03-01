function openEditRoleModal(id, name, canUser, canCourse, canFinance) {
    document.getElementById('modalRoleId').value = id;
    document.getElementById('modalRoleName').innerText = name;
    
    document.getElementById('chkUser').checked = canUser;
    document.getElementById('chkCourse').checked = canCourse;
    document.getElementById('chkFinance').checked = canFinance;
    
    if(id === 1) {
        document.getElementById('chkUser').disabled = true;
        document.getElementById('chkCourse').disabled = true;
        document.getElementById('chkFinance').disabled = true;
    } else {
        document.getElementById('chkUser').disabled = false;
        document.getElementById('chkCourse').disabled = false;
        document.getElementById('chkFinance').disabled = false;
    }
    
    var myModalEl = document.getElementById('editRoleModal');
    document.body.appendChild(myModalEl); 
    var myModal = bootstrap.Modal.getOrCreateInstance(myModalEl);
    myModal.show();
}