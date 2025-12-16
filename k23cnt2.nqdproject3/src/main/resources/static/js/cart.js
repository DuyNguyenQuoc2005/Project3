// /js/cart.js
document.addEventListener('DOMContentLoaded', function () {

    // ===== Cập nhật badge trên header =====
    function updateCartBadge(delta, animate) {
        const badge = document.querySelector('.nqd-icon-badge');
        if (!badge) return;

        let current = parseInt(badge.textContent || '0', 10);
        if (isNaN(current) || current < 0) current = 0;

        // delta là số muốn cộng thêm (ví dụ +1, +2,...)
        if (typeof delta === 'number') {
            current += delta;
            if (current < 0) current = 0;
        }

        badge.textContent = current;

        if (animate) {
            badge.classList.add('nqd-badge-bounce');
            setTimeout(function () {
                badge.classList.remove('nqd-badge-bounce');
            }, 400);
        }
    }

    // GỌI 1 LẦN KHI LOAD TRANG (không đổi số, chỉ để chắc chắn không rỗng)
    updateCartBadge(0, false);

    // ===== Gọi backend /cart/add bằng fetch =====
    function addToCart(productId, quantity) {
        if (!productId) return;
        if (!quantity || quantity < 1) quantity = 1;

        const body = new URLSearchParams();
        body.append('productId', productId);
        body.append('quantity', quantity);

        fetch('/cart/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: body.toString()
        })
            .then(function (resp) {
                if (!resp.ok) {
                    throw new Error('Response not OK');
                }

                // Tăng số trên badge đúng bằng quantity vừa thêm
                updateCartBadge(quantity, true);

                // Thông báo nhỏ
                showToast('Đã thêm sản phẩm vào giỏ hàng!');
            })
            .catch(function (err) {
                console.error('Lỗi thêm giỏ hàng:', err);
                showToast('Thêm giỏ hàng thất bại, thử lại sau!', true);
            });
    }

    // ===== Toast thông báo =====
    function showToast(message, isError) {
        let toast = document.querySelector('.nqd-toast');
        if (!toast) {
            toast = document.createElement('div');
            toast.className = 'nqd-toast';
            document.body.appendChild(toast);
        }
        toast.textContent = message;
        toast.classList.remove('nqd-toast--error');
        if (isError) {
            toast.classList.add('nqd-toast--error');
        }
        toast.classList.add('nqd-toast--show');
        setTimeout(function () {
            toast.classList.remove('nqd-toast--show');
        }, 2000);
    }

    // ===== DETAIL PAGE: chặn submit form =====
    const detailForm = document.querySelector('.nqd-detail-form');
    if (detailForm) {
        detailForm.addEventListener('submit', function (e) {
            e.preventDefault(); // không chuyển trang

            const productIdInput = detailForm.querySelector('input[name="productId"]');
            const qtyInput = detailForm.querySelector('input[name="quantity"]');

            const productId = productIdInput ? productIdInput.value : null;
            const quantity = qtyInput ? parseInt(qtyInput.value || '1', 10) : 1;

            addToCart(productId, quantity);
        });
    }

    // ===== LIST PAGE: nút "Thêm vào giỏ" =====
    const listButtons = document.querySelectorAll('.js-add-to-cart-list');
    listButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            const productId = this.getAttribute('data-product-id');
            const quantity = 1; // list: default 1
            addToCart(productId, quantity);
        });
    });
});
