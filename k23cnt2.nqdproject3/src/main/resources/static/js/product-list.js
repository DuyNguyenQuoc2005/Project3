document.addEventListener('DOMContentLoaded', function () {
    const priceForm = document.getElementById('priceFilterForm');
    if (!priceForm) return;

    const minInput = document.getElementById('minPrice');
    const maxInput = document.getElementById('maxPrice');
    const minLabel = document.getElementById('minPriceLabel');
    const maxLabel = document.getElementById('maxPriceLabel');
    const categorySelect = document.getElementById('categorySelect');
    const categoryHidden = document.getElementById('categoryIdHidden');

    function formatVnd(value) {
        return new Intl.NumberFormat('vi-VN').format(value) + ' đ';
    }

    function updatePriceLabel() {
        if (minLabel && minInput) {
            minLabel.textContent = formatVnd(minInput.value || 0);
        }
        if (maxLabel && maxInput) {
            maxLabel.textContent = formatVnd(maxInput.value || 0);
        }
    }

    // khi kéo slider giá -> update label + gửi form
    [minInput, maxInput].forEach(input => {
        if (!input) return;
        input.addEventListener('change', function () {
            updatePriceLabel();
            priceForm.submit();
        });
    });

    updatePriceLabel();

    // khi đổi loại bánh -> gán vào hidden + submit
    if (categorySelect && categoryHidden) {
        categorySelect.addEventListener('change', function () {
            categoryHidden.value = this.value; // có thể rỗng = tất cả
            priceForm.submit();
        });
    }
});
