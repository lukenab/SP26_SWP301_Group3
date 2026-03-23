
const searchInput = document.getElementById("studentSearch");

if (searchInput) {
    searchInput.addEventListener("keyup", function () {
        const query = this.value.toLowerCase().trim();
        const rows = document.querySelectorAll(".student-row");
        
        const noDataRow = document.getElementById("noDataRow"); 
        let hasMatch = false;

        rows.forEach(row => {
            const name = row.querySelector(".student-name").innerText.toLowerCase();
            const email = row.querySelector(".student-email").innerText.toLowerCase();
            
            if (name.includes(query) || email.includes(query)) {
                row.style.display = ""; 
                hasMatch = true; 
            } else {
                row.style.display = "none"; 
            }
        });

    
        if (noDataRow) {
            if (query === "") {
  
                noDataRow.style.display = "none";
            } else {
        
                noDataRow.style.display = hasMatch ? "none" : ""; 
            }
        }

        updateGradeStats();
    });
}
function updateGradeStats() {
    const totalRows = document.querySelectorAll('.student-row');
    const countPassedLabel = document.getElementById('countPassed');
    const countFailedLabel = document.getElementById('countFailed');
    const totalLabel = document.getElementById('totalStudents');

    if (!countPassedLabel || !countFailedLabel)
        return;

    let passed = 0;
    let failed = 0;
    let totalVisible = 0;

    totalRows.forEach(row => {

        if (row.style.display !== "none") {
            totalVisible++;

            const scoreBadge = row.querySelector('.final-score-value');

            if (scoreBadge) {
                const scoreText = scoreBadge.innerText.trim().replace(',', '.');
                const score = parseFloat(scoreText);

                console.log("Đang kiểm tra điểm:", scoreText); 

                if (!isNaN(score)) {
                    if (score >= 5) {
                        passed++;
                    } else {
                        failed++;
                    }
                } else {
                    failed++;
                }
            } else {
                failed++;
            }
        }
    });


    countPassedLabel.innerText = passed;
    countFailedLabel.innerText = failed;
    if (totalLabel)
        totalLabel.innerText = totalVisible;
}