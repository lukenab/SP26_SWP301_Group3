document.addEventListener("DOMContentLoaded", function () {
    const ctx = document.getElementById('revenueChart').getContext('2d');
    const revenueChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Sep', 'Oct', 'Nov', 'Dec', 'Jan', 'Feb'],
            datasets: [{
                    label: 'Revenue',
                    data: [42000, 51000, 47000, 58000, 65000, 72450],
                    borderColor: '#1d4ed8', // Màu xanh dương đậm
                    backgroundColor: '#1d4ed8',
                    borderWidth: 3,
                    pointBackgroundColor: '#1d4ed8',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 6,
                    pointHoverRadius: 8,
                    tension: 0.4 // Làm cong đường nét
                }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {display: false} // Ẩn chú thích
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: 80000,
                    ticks: {
                        callback: function (value) {
                            return '$' + (value / 1000) + 'k'; // Format $20k, $40k
                        },
                        color: '#94a3b8',
                        stepSize: 20000
                    },
                    grid: {
                        color: '#f1f5f9',
                        borderDash: [5, 5] // Kẻ vạch đứt
                    },
                    border: {display: false}
                },
                x: {
                    ticks: {color: '#94a3b8'},
                    grid: {display: false},
                    border: {color: '#e2e8f0'}
                }
            }
        }
    });
});