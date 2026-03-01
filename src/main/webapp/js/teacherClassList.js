document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const statusFilter = document.getElementById('statusFilter');
    const tableRows = document.querySelectorAll('.class-row');
    const noResultRow = document.getElementById('noResultRow');

    function filterTable() {
        const searchText = searchInput.value.toLowerCase().trim();
        const selectedStatus = statusFilter.value.toLowerCase();
        let visibleCount = 0;

        tableRows.forEach(row => {
            const className = row.querySelector('.user-name').textContent.toLowerCase();
            const status = row.querySelector('.status-text').textContent.trim().toLowerCase();

           
            const matchesSearch = className.includes(searchText);
            const matchesStatus = selectedStatus === 'all' || status === selectedStatus;

            if (matchesSearch && matchesStatus) {
                row.style.display = ''; 
                visibleCount++;
            } else {
                row.style.display = 'none'; 
            }
        });


        noResultRow.style.display = visibleCount === 0 ? '' : 'none';
    }


    searchInput.addEventListener('input', filterTable);
    statusFilter.addEventListener('change', filterTable);
});
