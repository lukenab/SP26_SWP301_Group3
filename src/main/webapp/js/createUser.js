function toggleExtraFields() {
    var roleId = document.getElementById("roleId").value;
    
    var empFields = document.getElementById("employeeFields");
    var stuFields = document.getElementById("studentFields");

    empFields.style.display = "none";
    stuFields.style.display = "none";

    if (roleId === "2" || roleId === "3" || roleId === "4") {
        empFields.style.display = "block";
    } 
    else if (roleId === "5") {
        stuFields.style.display = "block";
    }
}
