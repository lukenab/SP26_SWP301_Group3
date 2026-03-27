(function () {
    // manageSchedule.js - client-side filtering for schedule management
    const classSelect = document.getElementById('filterClass');
    const roomSelect = document.getElementById('filterRoom');
    const dateInput = document.getElementById('filterDate');
    const clearBtn = document.getElementById('clearScheduleFilter');

    if (!classSelect && !roomSelect && !dateInput) return;

    const scheduleItems = Array.from(document.querySelectorAll('.schedule-item'));
    const urlParams = new URLSearchParams(window.location.search);

    function applyFilters() {
        const classId = classSelect ? classSelect.value : '';
        const roomId = roomSelect ? roomSelect.value : '';
        const dateVal = dateInput ? dateInput.value : '';

        scheduleItems.forEach(item => {
            let show = true;
            if (classId && classId !== '0') {
                show = show && item.getAttribute('data-class-id') === classId;
            }
            if (roomId && roomId !== '0') {
                show = show && item.getAttribute('data-room-id') === roomId;
            }
            // date filter is handled server-side by generating scheduleList for the selected week,
            // we keep date in URL to preserve state but do not filter items by date here.

            item.style.display = show ? '' : 'none';
        });

        // update URL params so reload preserves state
        if (classSelect) {
            if (classSelect.value && classSelect.value !== '0') urlParams.set('classId', classSelect.value); else urlParams.delete('classId');
        }
        if (roomSelect) {
            if (roomSelect.value && roomSelect.value !== '0') urlParams.set('roomId', roomSelect.value); else urlParams.delete('roomId');
        }
        if (dateInput) {
            if (dateInput.value) urlParams.set('date', dateInput.value); else urlParams.delete('date');
        }
        history.replaceState(null, '', window.location.pathname + '?' + urlParams.toString());
    }

    // auto-apply on change
    classSelect && classSelect.addEventListener('change', applyFilters);
    roomSelect && roomSelect.addEventListener('change', applyFilters);
    dateInput && dateInput.addEventListener('change', function () {
        // For date change, we need to reload because the scheduleList is server-generated per week
        if (dateInput.value) {
            urlParams.set('date', dateInput.value);
        } else {
            urlParams.delete('date');
        }
        // Trigger full page reload to get server-generated scheduleList for the week
        const qs = urlParams.toString();
        window.location.search = qs ? ('?' + qs) : '';
    });

    clearBtn && clearBtn.addEventListener('click', function () {
        if (classSelect) classSelect.value = '0';
        if (roomSelect) roomSelect.value = '0';
        // Do not change date here; keep selected week unless user clears explicitly
        applyFilters();
    });

    // initialize from URL params
    (function initFromURL() {
        const c = urlParams.get('classId');
        const r = urlParams.get('roomId');
        const d = urlParams.get('date');
        if (classSelect && c) classSelect.value = c;
        if (roomSelect && r) roomSelect.value = r;
        if (dateInput && d) dateInput.value = d;
        applyFilters();
    })();
})();

