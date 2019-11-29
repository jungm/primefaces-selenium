window.pfselenium = {
    uid : null,
    navigating : false,
    xhr : null
};

var originalSend = XMLHttpRequest.prototype.send;
XMLHttpRequest.prototype.send = function() {
    window.pfselenium.xhr = this;

    this.addEventListener("load", function() {
        window.pfselenium.xhr = null;
    });
    this.addEventListener("error", function() {
        window.pfselenium.xhr = null;
    });
    this.addEventListener("abort", function() {
        window.pfselenium.xhr = null;
    });

    originalSend.apply(this, arguments);
};

// try to listen on navigation, which can happen inside AJAX requests
window.addEventListener("beforeunload", function() {
    window.pfselenium.navigating = true;
});
window.addEventListener("unload", function() {
    window.pfselenium.navigating = true;
});
