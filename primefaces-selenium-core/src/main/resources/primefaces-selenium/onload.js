window.pfselenium = {
    navigating : false,
    submitting : false,
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

var originalSubmit = HTMLFormElement.prototype.submit;
HTMLFormElement.prototype.submit = function() {
    window.pfselenium.submitting = true;

    originalSubmit.apply(this, arguments);
};


// try to listen on navigation, which can happen inside AJAX requests
window.addEventListener("beforeunload", function() {
    window.pfselenium.submitting = false;
    window.pfselenium.navigating = true;
});
window.addEventListener("unload", function() {
    window.pfselenium.submitting = false;
    window.pfselenium.navigating = true;
});
