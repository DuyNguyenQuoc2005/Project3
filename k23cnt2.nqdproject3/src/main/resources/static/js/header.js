// /js/header.js
document.addEventListener('DOMContentLoaded', function () {
    const header = document.querySelector('.nqd-header');
    if (!header) return;

    const THRESHOLD = 60; // cuộn xuống bao nhiêu px thì đổi style

    function onScroll() {
        if (window.scrollY > THRESHOLD) {
            header.classList.add('nqd-header--scrolled');
        } else {
            header.classList.remove('nqd-header--scrolled');
        }
    }

    window.addEventListener('scroll', onScroll, { passive: true });
    onScroll();
});
