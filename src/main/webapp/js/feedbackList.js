document.addEventListener('DOMContentLoaded', function () {
    const container = document.getElementById('feedback-page-container');
    const classFilter = document.getElementById('classFilter');
    const ratingButtons = document.querySelectorAll('#ratingFilter .btn');
    const feedbackItems = document.querySelectorAll('.feedback-item');
    const noDataMessage = document.getElementById('noDataMessage');


    function applyFilters() {
        const selectedClass = classFilter.value;
        const activeBtn = document.querySelector('#ratingFilter .btn-primary');
        const selectedRating = activeBtn ? activeBtn.getAttribute('data-rating') : 'all';
        let hasVisibleItem = false;

        feedbackItems.forEach(item => {
            const itemClass = item.getAttribute('data-class');
            const itemRating = item.getAttribute('data-rating');

            const matchClass = (selectedClass === 'all' || itemClass === selectedClass);
            const matchRating = (selectedRating === 'all' || itemRating === selectedRating);

            if (matchClass && matchRating) {
                item.style.display = 'block';
                hasVisibleItem = true;
            } else {
                item.style.display = 'none';
            }
        });
        if (hasVisibleItem) {
            noDataMessage.classList.add('d-none');
        } else {
            noDataMessage.classList.remove('d-none');
        }
    }

    classFilter.addEventListener('change', applyFilters);

    ratingButtons.forEach(btn => {
        btn.addEventListener('click', function () {
            ratingButtons.forEach(b => {
                b.classList.remove('btn-primary', 'active');
                b.classList.add('btn-outline-primary');
            });
            this.classList.remove('btn-outline-primary');
            this.classList.add('btn-primary', 'active');
            applyFilters();
        });
    });

    const preSelectedClass = container.getAttribute('data-pre-class');

    if (preSelectedClass && preSelectedClass !== "" && preSelectedClass !== "null") {
        classFilter.value = preSelectedClass;
    } else {
        classFilter.value = "all";
    }
    
    applyFilters();
});