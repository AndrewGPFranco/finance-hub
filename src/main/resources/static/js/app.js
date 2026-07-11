document.addEventListener("DOMContentLoaded", function () {
    let sidebar = document.querySelector("[data-sidebar]");
    let sidebarToggleButtons = document.querySelectorAll("[data-sidebar-toggle]");
    let sidebarCloseButtons = document.querySelectorAll("[data-sidebar-close]");
    let sidebarStorageKey = "FINANCE_SIDEBAR_OPEN";

    if (sidebar) {
        let setSidebarOpen = function (open, persist) {
            document.body.classList.toggle("sidebar-open", open);

            sidebarToggleButtons.forEach(function (button) {
                button.setAttribute("aria-expanded", open ? "true" : "false");
                button.setAttribute("aria-label", open ? "Fechar menu" : "Abrir menu");
            });

            if (persist && window.matchMedia("(min-width: 761px)").matches)
                localStorage.setItem(sidebarStorageKey, open ? "true" : "false");
        };

        setSidebarOpen(window.matchMedia("(min-width: 761px)").matches &&
            localStorage.getItem(sidebarStorageKey) === "true", false);

        sidebarToggleButtons.forEach(function (button) {
            button.addEventListener("click", function () {
                setSidebarOpen(!document.body.classList.contains("sidebar-open"), true);
            });
        });

        sidebarCloseButtons.forEach(function (button) {
            button.addEventListener("click", function () {
                setSidebarOpen(false, true);
            });
        });

        sidebar.querySelectorAll("a").forEach(function (link) {
            link.addEventListener("click", function () {
                if (window.matchMedia("(max-width: 760px)").matches)
                    setSidebarOpen(false, false);
            });
        });

        window.addEventListener("keydown", function (event) {
            if (event.key === "Escape")
                setSidebarOpen(false, true);
        });
    }

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

    let inviteModal = document.querySelector("[data-invite-modal]");

    if (inviteModal) {
        let inviteForm = inviteModal.querySelector("form");
        let inviteSubdomainId = inviteModal.querySelector("[data-invite-subdomain-id]");
        let inviteSubdomainName = inviteModal.querySelector("[data-invite-subdomain-name]");
        let inviteEmail = inviteModal.querySelector("#emailGuest");

        let closeInviteModal = function () {
            inviteModal.hidden = true;
            if (inviteForm)
                inviteForm.reset();
        };

        document.querySelectorAll("[data-subdomain-id]").forEach(function (button) {
            button.addEventListener("click", function () {
                inviteSubdomainId.value = button.dataset.subdomainId;
                inviteSubdomainName.textContent = button.dataset.subdomainName || "este subdomínio";
                inviteModal.hidden = false;
                inviteEmail.focus();
            });
        });

        inviteModal.querySelectorAll("[data-invite-close]").forEach(function (button) {
            button.addEventListener("click", closeInviteModal);
        });

        inviteModal.addEventListener("click", function (event) {
            if (event.target === inviteModal)
                closeInviteModal();
        });

        window.addEventListener("keydown", function (event) {
            if (event.key === "Escape" && !inviteModal.hidden)
                closeInviteModal();
        });
    }
});
