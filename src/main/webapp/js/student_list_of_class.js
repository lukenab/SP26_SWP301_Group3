document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("studentSearch");

    // 1. Gọi lần đầu tiên khi load trang để tính toán ngay lập tức
    updateGradeStats();

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
                noDataRow.style.display = (query !== "" && !hasMatch) ? "" : "none";
            }

            // 2. Gọi lại mỗi khi tìm kiếm để cập nhật thống kê theo kết quả hiển thị
            updateGradeStats();
        });
    }
});

function updateGradeStats() {
    const totalRows = document.querySelectorAll('.student-row');
    const countPassedLabel = document.getElementById('countPassed');
    const countFailedLabel = document.getElementById('countFailed');
    const totalLabel = document.getElementById('totalStudents');

    if (!countPassedLabel || !countFailedLabel) return;

    let passed = 0;
    let failed = 0;
    let totalVisible = 0;

    totalRows.forEach(row => {
        // Chỉ tính toán trên những dòng đang hiển thị (không bị ẩn bởi search)
        if (row.style.display !== "none") {
            totalVisible++;
            const scoreBadge = row.querySelector('.final-score-value');

            if (scoreBadge) {
                // Lấy nội dung, xóa khoảng trắng và đổi dấu phẩy thành dấu chấm để parseFloat chuẩn
                const scoreText = scoreBadge.innerText.trim().replace(',', '.');
                const score = parseFloat(scoreText);

                if (!isNaN(score)) {
                    if (score >= 5) {
                        passed++;
                    } else {
                        failed++;
                    }
                } else {
                    // Trường hợp có badge nhưng nội dung không phải số (ví dụ: "N/A")
                    failed++;
                }
            } else {
                // Trường hợp không có thẻ .final-score-value (chưa nhập điểm)
                failed++;
            }
        }
    });

    // Cập nhật lên giao diện
    countPassedLabel.innerText = passed;
    countFailedLabel.innerText = failed;
    if (totalLabel) totalLabel.innerText = totalVisible;
}