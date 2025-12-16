// home-index.js
// Slider cho section BỘ SƯU TẬP (8 sản phẩm, hiển thị 4)

document.addEventListener('DOMContentLoaded', function () {
    const track = document.querySelector('.nqd-collection-track');
    if (!track) return;

    const cards = track.querySelectorAll('.nqd-collection-card');
    const prevBtn = document.querySelector('.nqd-collection-arrow-prev');
    const nextBtn = document.querySelector('.nqd-collection-arrow-next');

    const visible = 4;   // số card hiển thị 1 lần
    const gap = 24;      // phải trùng với gap trong CSS (.nqd-collection-track)
    let index = 0;
    const total = cards.length;

    // Nếu không đủ 5+ card thì ẩn mũi tên luôn
    if (total <= visible) {
        if (prevBtn) prevBtn.style.display = 'none';
        if (nextBtn) nextBtn.style.display = 'none';
        return;
    }

    function update() {
        const firstCard = cards[0];
        const cardWidth = firstCard.offsetWidth;
        const offset = (cardWidth + gap) * index;
        track.style.transform = 'translateX(' + (-offset) + 'px)';

        if (prevBtn) prevBtn.disabled = index === 0;
        if (nextBtn) nextBtn.disabled = index + visible >= total;
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', function () {
            if (index > 0) {
                index -= visible;
                if (index < 0) index = 0;
                update();
            }
        });
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', function () {
            if (index + visible < total) {
                index += visible;
                if (index + visible > total) index = total - visible;
                update();
            }
        });
    }

    window.addEventListener('resize', update);
    update();
});

// Slider CẢM NHẬN KHÁCH HÀNG (3 slide, dots + arrows)
document.addEventListener('DOMContentLoaded', function () {
    const slides = document.querySelectorAll('.nqd-testimonial-slide');
    if (!slides.length) return;

    const prevBtn = document.querySelector('.nqd-testimonial-arrow-prev');
    const nextBtn = document.querySelector('.nqd-testimonial-arrow-next');
    const dots = document.querySelectorAll('.nqd-testimonial-dots button');

    let index = 0;
    const total = slides.length;

    function update() {
        slides.forEach((s, i) => {
            s.classList.toggle('active', i === index);
        });
        dots.forEach((d, i) => {
            d.classList.toggle('active', i === index);
        });
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', function () {
            index = (index - 1 + total) % total;
            update();
        });
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', function () {
            index = (index + 1) % total;
            update();
        });
    }

    dots.forEach((dot, i) => {
        dot.addEventListener('click', function () {
            index = i;
            update();
        });
    });

    // hiển thị slide đầu tiên
    update();
});
