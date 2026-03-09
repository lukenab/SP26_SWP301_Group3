document.addEventListener("DOMContentLoaded", function () {
    // 1. Tự động ẩn Toast thông báo
    const toast = document.getElementById("toastMessage");
    if (toast) {
        setTimeout(() => {
            toast.style.opacity = "0";
            setTimeout(() => toast.remove(), 400);
        }, 3000);
    }

    // 2. Xử lý Search và cập nhật số liệu ngay lập tức
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

            // Gọi lại hàm đếm sau khi lọc (để số Passed/Failed nhảy theo kết quả search)
            updateGradeStats();
        });
    }

    // 3. QUAN TRỌNG: Gọi hàm này để nó đếm ngay khi vừa load trang xong
    updateGradeStats();
});
function updateGradeStats() {
    const totalRows = document.querySelectorAll('.student-row');
    const countPassedLabel = document.getElementById('countPassed');
    const countFailedLabel = document.getElementById('countFailed');
    const totalLabel = document.getElementById('totalStudents');

    // Thoát nếu không tìm thấy các thẻ ID trên trang
    if (!countPassedLabel || !countFailedLabel)
        return;

    let passed = 0;
    let failed = 0;
    let totalVisible = 0;

    totalRows.forEach(row => {

        // Chỉ đếm những hàng đang hiển thị
        if (row.style.display !== "none") {
            totalVisible++;

            const scoreBadge = row.querySelector('.final-score-value');

            if (scoreBadge) {
                const scoreText = scoreBadge.innerText.trim().replace(',', '.');
                const score = parseFloat(scoreText);

                console.log("Đang kiểm tra điểm:", scoreText); // đặt ở đây

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

    // Cập nhật kết quả lên giao diện
    countPassedLabel.innerText = passed;
    countFailedLabel.innerText = failed;
    if (totalLabel)
        totalLabel.innerText = totalVisible;
}