document.addEventListener('DOMContentLoaded', function () {
    // Lấy các label hiển thị số
    const countPresentLabel = document.getElementById('countPresent');
    const countAbsentLabel = document.getElementById('countAbsent');

    function updateStats() {
        // Đếm chính xác số lượng radio đang được chọn dựa trên value
        const presentCount = document.querySelectorAll('input[value="Present"]:checked').length;
        const absentCount = document.querySelectorAll('input[value="Absent"]:checked').length;

        countPresentLabel.innerText = presentCount;
        countAbsentLabel.innerText = absentCount;
    }

    // Lắng nghe sự kiện thay đổi trên tất cả các input radio trong form
    document.querySelectorAll('input[type="radio"]').forEach(input => {
        input.addEventListener('change', updateStats);
    });

    // Chạy lần đầu tiên khi trang vừa load để hiển thị số lượng hiện tại (nếu đã có dữ liệu)
    updateStats();
});