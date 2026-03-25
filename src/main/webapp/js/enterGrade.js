document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('gradeForm');
    if (!form) {
        return;
    }

    form.addEventListener('submit', function (e) {
        let firstInvalid = null;
        const scoreInputs = form.querySelectorAll('input[name^="score_"]');

        scoreInputs.forEach(function (input) {
            const raw = input.value.trim();
            input.setCustomValidity('');

            if (!raw) {
                return;
            }

            const val = Number(raw);
            if (Number.isNaN(val) || val < 0 || val > 10) {
                input.setCustomValidity('Score must be a number from 0 to 10.');
                if (!firstInvalid) {
                    firstInvalid = input;
                }
            }
        });

        if (firstInvalid) {
            e.preventDefault();
            firstInvalid.reportValidity();
            firstInvalid.focus();
        }
    });
});

