document.addEventListener("DOMContentLoaded", function () {
    let subdomainSelect = document.querySelector("[data-global-subdomain-select]");
    let monthSelect = document.querySelector("[data-global-month-select]");

    if (subdomainSelect) {
        subdomainSelect.addEventListener("change", function () {
            let url = new URL(window.location.href);

            if (subdomainSelect.value)
                url.searchParams.set("subdomainId", subdomainSelect.value);
            else
                url.searchParams.delete("subdomainId");

            window.location.assign(url.toString());
        });
    }

    if (monthSelect) {
        monthSelect.addEventListener("change", function () {
            let url = new URL(window.location.href);

            if (monthSelect.value)
                url.searchParams.set("month", monthSelect.value);
            else
                url.searchParams.delete("month");

            window.location.assign(url.toString());
        });
    }

    let previewInput = document.querySelector("[data-image-preview-input]");
    let previewImage = document.querySelector("[data-image-preview]");
    let previewEmpty = document.querySelector("[data-image-preview-empty]");

    if (previewInput && previewImage && previewEmpty) {
        let updatePreview = function () {
            let value = previewInput.value.trim();

            if (!value) {
                previewImage.removeAttribute("src");
                previewImage.hidden = true;
                previewEmpty.hidden = false;
                return;
            }

            previewImage.src = value;
            previewImage.hidden = false;
            previewEmpty.hidden = true;
        };

        previewInput.addEventListener("input", updatePreview);
        previewImage.addEventListener("error", function () {
            previewImage.hidden = true;
            previewEmpty.hidden = false;
        });
        updatePreview();
    }
});
