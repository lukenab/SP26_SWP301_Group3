 document.addEventListener("DOMContentLoaded", function () {
        const toast = document.getElementById("toastMessage");
        if (toast) {
            setTimeout(() => {
                toast.style.opacity = "0";
                setTimeout(() => toast.remove(), 400);
            }, 3000);
        }

        const searchInput = document.getElementById("studentSearch");
        if (searchInput) {
            searchInput.addEventListener("keyup", function () {
                const query = this.value.toLowerCase().trim();
                const rows = document.querySelectorAll(".student-row");

                rows.forEach(row => {
                    const name = row.querySelector(".student-name").innerText.toLowerCase();
                    const email = row.querySelector(".student-email").innerText.toLowerCase();
                    if (name.includes(query) || email.includes(query)) {
                        row.style.display = "";
                    } else {
                        row.style.display = "none";
                    }
                });
            });
        }
    });