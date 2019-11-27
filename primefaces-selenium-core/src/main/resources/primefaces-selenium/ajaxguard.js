var originalOpen = XMLHttpRequest.prototype.open;
XMLHttpRequest.prototype.open = function() {
    this.addEventListener('readystatechange', function() {
        window.primeselenium_ars = this.readyState;
    });
    originalOpen.apply(this, arguments);
};
