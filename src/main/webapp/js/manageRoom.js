(function () {
    // manageRoom.js - client-side filtering + pagination
    const pageSize = 10;
    const tbody = document.getElementById('roomsTbody');
    if (!tbody) return;

    const allRows = Array.from(tbody.querySelectorAll('tr.room-row'));
    const roomsSummary = document.getElementById('roomsSummary');
    const roomsPagination = document.getElementById('roomsPagination');
    // applyFilterBtn removed — filters apply automatically on change
    const clearFilterBtn = document.getElementById('clearFilter');
    const capacitySelect = document.getElementById('filterCapacity');
    const typeSelect = document.getElementById('filterType');
    const statusSelect = document.getElementById('filterStatus');
    const searchInput = document.getElementById('roomSearchInput');

    const urlParams = new URLSearchParams(window.location.search);

    function readFiltersFromURL() {
        const c = urlParams.get('capacity') || '';
        const t = urlParams.get('type') || '';
        const s = urlParams.get('status') || '';
        if (capacitySelect) capacitySelect.value = c;
        if (typeSelect) typeSelect.value = t;
        if (statusSelect) statusSelect.value = s;
    }

    readFiltersFromURL();

    let filteredRows = allRows.slice();
    let currentPage = 1;

    function applyFilters() {
        const cap = capacitySelect ? capacitySelect.value : '';
        const type = typeSelect ? typeSelect.value : '';
        const status = statusSelect ? statusSelect.value : '';
        const search = searchInput ? (searchInput.value || '').trim().toLowerCase() : '';

        filteredRows = allRows.filter(row => {
            const rCap = row.getAttribute('data-capacity') || '';
            const rType = row.getAttribute('data-type') || '';
            const rStatus = (row.getAttribute('data-status') === 'true') ? 'active' : 'disabled';
            const rName = (row.querySelector('.user-name') ? row.querySelector('.user-name').textContent : '').toLowerCase();

            if (cap && cap !== '' && rCap !== cap) return false;
            if (type && type !== '' && rType !== type) return false;
            if (status && status !== '' && rStatus !== status) return false;
            if (search && !rName.includes(search)) return false;
            return true;
        });

        currentPage = 1;

        if (cap) urlParams.set('capacity', cap); else urlParams.delete('capacity');
        if (type) urlParams.set('type', type); else urlParams.delete('type');
        if (status) urlParams.set('status', status); else urlParams.delete('status');
        if (search) urlParams.set('search', search); else urlParams.delete('search');
        urlParams.set('page', currentPage);
        history.replaceState(null, '', window.location.pathname + '?' + urlParams.toString());

        renderPage();
    }

    function clearFilters() {
        if (capacitySelect) capacitySelect.value = '';
        if (typeSelect) typeSelect.value = '';
        if (statusSelect) statusSelect.value = '';
        if (searchInput) searchInput.value = '';
        urlParams.delete('capacity');
        urlParams.delete('type');
        urlParams.delete('status');
        urlParams.delete('search');
        urlParams.set('page', 1);
        history.replaceState(null, '', window.location.pathname + '?' + urlParams.toString());
        applyFilters();
    }

    function buildPagination(totalPages) {
        // clear old
        roomsPagination.innerHTML = '';

        // prev
        const prevLi = document.createElement('li');
        prevLi.className = 'page-item' + (currentPage <= 1 ? ' disabled' : '');
        const prevA = document.createElement('a');
        prevA.className = 'page-link';
        prevA.href = '#';
        prevA.innerHTML = "<i class='bx bx-chevron-left'></i> Previous";
        prevA.addEventListener('click', function (e) {
            e.preventDefault();
            if (currentPage > 1) renderPage(currentPage - 1);
        });
        prevLi.appendChild(prevA);
        roomsPagination.appendChild(prevLi);

        // pages
        for (let i = 1; i <= totalPages; i++) {
            const li = document.createElement('li');
            li.className = 'page-item' + (i === currentPage ? ' active' : '');
            const a = document.createElement('a');
            a.className = 'page-link';
            a.href = '#';
            a.textContent = i;
            (function (page) {
                a.addEventListener('click', function (e) {
                    e.preventDefault();
                    renderPage(page);
                });
            })(i);
            li.appendChild(a);
            roomsPagination.appendChild(li);
        }

        // next
        const nextLi = document.createElement('li');
        nextLi.className = 'page-item' + (currentPage >= totalPages ? ' disabled' : '');
        const nextA = document.createElement('a');
        nextA.className = 'page-link';
        nextA.href = '#';
        nextA.innerHTML = "Next <i class='bx bx-chevron-right'></i>";
        nextA.addEventListener('click', function (e) {
            e.preventDefault();
            if (currentPage < totalPages) renderPage(currentPage + 1);
        });
        nextLi.appendChild(nextA);
        roomsPagination.appendChild(nextLi);
    }

    function renderPage(page) {
        const totalRows = filteredRows.length;
        const totalPages = Math.max(1, Math.ceil(totalRows / pageSize));

        if (typeof page === 'number') currentPage = page;
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        const start = (currentPage - 1) * pageSize;
        const end = start + pageSize;

        allRows.forEach(r => r.style.display = 'none');

        filteredRows.forEach((row, idx) => {
            const absIndex = idx + 1;
            const idxCell = row.querySelector('.room-index');
            if (idx >= start && idx < end) {
                row.style.display = '';
                if (idxCell) idxCell.textContent = absIndex;
            }
        });

        // update summary
        const startItem = totalRows === 0 ? 0 : start + 1;
        const endItem = Math.min(totalRows, end);
        roomsSummary.textContent = `Showing ${startItem}-${endItem} of ${totalRows} rooms`;

        buildPagination(totalPages);

        urlParams.set('page', currentPage);
        history.replaceState(null, '', window.location.pathname + '?' + urlParams.toString());
    }

    // Auto-apply when selects change
    if (capacitySelect) capacitySelect.addEventListener('change', applyFilters);
    if (typeSelect) typeSelect.addEventListener('change', applyFilters);
    if (statusSelect) statusSelect.addEventListener('change', applyFilters);
    clearFilterBtn && clearFilterBtn.addEventListener('click', clearFilters);
    if (searchInput) {
        // optional debounce
        let t;
        searchInput.addEventListener('input', function () {
            clearTimeout(t);
            t = setTimeout(function () {
                applyFilters();
            }, 300);
        });
    }

    // initialize
    (function initFromURL() {
        const pageParam = parseInt(urlParams.get('page'));
        // populate search from URL if present
        const urlSearch = urlParams.get('search');
        if (urlSearch && searchInput) searchInput.value = urlSearch;
        applyFilters();
        if (!isNaN(pageParam) && pageParam >= 1) currentPage = pageParam;
        renderPage(currentPage);
    })();
})();





