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

// change readyState, so our waits/guards wait until the new page is loaded, when readyState==complete again
// this can happen also in AJAX redirects, also when a AJAX guard is used, as the redirect is executed after all "readystatechange" listeners
window.addEventListener("beforeunload", function() {
    window.pfselenium.navigating = true;
});
window.addEventListener("unload", function() {
    window.pfselenium.navigating = true;
});
