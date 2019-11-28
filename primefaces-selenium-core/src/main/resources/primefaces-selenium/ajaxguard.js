window.pfselenium = {
    uid : null,
    navigating : false,
    ajaxReadyState : null
};

var originalOpen = XMLHttpRequest.prototype.open;
XMLHttpRequest.prototype.open = function() {
    this.addEventListener('readystatechange', function() {
        if (this.readyState === 4) {
            // TODO our listener isn't the last, add some timeout to set the finished status
            // as the response will be evaluated within a readystatechange callback, too
            setTimeout(function() {
                window.pfselenium.ajaxReadyState = 4;
            }, 500);
        }
        else {
            window.pfselenium.ajaxReadyState = this.readyState;
        }
    });
    originalOpen.apply(this, arguments);
};

// change readyState, so our waits/guards wait until the new page is loaded, when readyState==complete again
// this can happen also in AJAX redirects, also when a AJAX guard is used, as the redirect is executed after all "readystatechange" listeners
window.addEventListener("beforeunload", function() {
    window.pfselenium.navigating = true;
});
window.addEventListener("unload", function() {
    window.pfselenium.navigating = true;
});
