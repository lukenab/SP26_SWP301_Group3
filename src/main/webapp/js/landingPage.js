
(function () {
    const searchInput = document.getElementById("courseSearchInput");
    const categoryFilters = Array.from(document.querySelectorAll(".course-category-filter"));
    const levelFilters = Array.from(document.querySelectorAll(".course-level-filter"));
    const priceFilters = Array.from(document.querySelectorAll(".course-price-filter"));
    const courseItems = Array.from(document.querySelectorAll(".course-item"));
    const emptyMessage = document.getElementById("emptyCourseMessage");

    if (!courseItems.length) {
        return;
    }

    function getCheckedValues(filters) {
        return filters.filter(function (f) {
            return f.checked;
        }).map(function (f) {
            return f.value;
        });
    }

    function getSelectedPrice() {
        const selected = priceFilters.find(function (f) {
            return f.checked;
        });
        return selected ? selected.value : "all";
    }

    function applyCourseFilter() {
        const keyword = (searchInput && searchInput.value ? searchInput.value : "").trim().toLowerCase();
        const selectedCategories = getCheckedValues(categoryFilters);
        const selectedLevels = getCheckedValues(levelFilters);
        const selectedPrice = getSelectedPrice();
        let visibleCount = 0;

        courseItems.forEach(function (item) {
            const name = (item.dataset.name || "").toLowerCase();
            const desc = (item.dataset.desc || "").toLowerCase();
            const category = item.dataset.category || "";
            const level = item.dataset.level || "";
            const fee = parseFloat(item.dataset.fee || "0");

            const matchKeyword = !keyword || name.includes(keyword) || desc.includes(keyword);
            const matchCategory = selectedCategories.length === 0 || selectedCategories.includes(category);
            const matchLevel = selectedLevels.length === 0 || selectedLevels.includes(level);

            let matchPrice = true;
            if (selectedPrice === "free") {
                matchPrice = fee === 0;
            } else if (selectedPrice === "paid") {
                matchPrice = fee > 0;
            }

            const isVisible = matchKeyword && matchCategory && matchLevel && matchPrice;
            item.classList.toggle("d-none", !isVisible);
            if (isVisible) {
                visibleCount++;
            }
        });

        if (emptyMessage) {
            emptyMessage.classList.toggle("d-none", visibleCount > 0);
        }
    }

    if (searchInput) {
        searchInput.addEventListener("input", applyCourseFilter);
    }
    categoryFilters.forEach(function (f) {
        f.addEventListener("change", applyCourseFilter);
    });
    levelFilters.forEach(function (f) {
        f.addEventListener("change", applyCourseFilter);
    });
    priceFilters.forEach(function (f) {
        f.addEventListener("change", applyCourseFilter);
    });
})();
 