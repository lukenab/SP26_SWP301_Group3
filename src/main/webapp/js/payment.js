function copyText(elementId) {
    var textToCopy = document.getElementById(elementId).innerText;
    // Loại bỏ dấu phẩy/chữ VNĐ nếu copy số tiền
    if (elementId === 'transferAmount') {
        textToCopy = textToCopy.replace(/[^0-9]/g, '');
    }

    navigator.clipboard.writeText(textToCopy).then(function () {
        alert("Copied to clipboard: " + textToCopy);
    }, function (err) {
        console.error('Could not copy text: ', err);
    });
}

// Simple Countdown Timer (15 minutes)
let time = 15 * 60;
const countdownEl = document.getElementById('countdown');
setInterval(updateCountdown, 1000);

function updateCountdown() {
    const minutes = Math.floor(time / 60);
    let seconds = time % 60;
    seconds = seconds < 10 ? '0' + seconds : seconds;
    countdownEl.innerHTML = minutes + ':' + seconds;
    if (time > 0)
        time--;
}