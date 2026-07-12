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

        setSidebarOpen(window.matchMedia("(min-width: 761px)").matches, false);

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
    let previewFileInput = document.querySelector("[data-image-file-input]");
    let previewImage = document.querySelector("[data-image-preview]");
    let previewEmpty = document.querySelector("[data-image-preview-empty]");
    let filePicker = document.querySelector(".file-picker");
    let filePickerLabel = document.querySelector("[data-file-picker-label]");
    let selectedImageObjectUrl = null;

    if (previewInput && previewImage && previewEmpty) {
        let revokeSelectedImageObjectUrl = function () {
            if (selectedImageObjectUrl) {
                URL.revokeObjectURL(selectedImageObjectUrl);
                selectedImageObjectUrl = null;
            }
        };

        let showPreview = function (src) {
            previewImage.src = src;
            previewImage.hidden = false;
            previewEmpty.hidden = true;
        };

        let showEmptyPreview = function () {
            previewImage.removeAttribute("src");
            previewImage.hidden = true;
            previewEmpty.hidden = false;
        };

        let normalizeImageSource = function (value) {
            if (!value)
                return "";

            if (value.startsWith("http://") || value.startsWith("https://") || value.startsWith("/photos/") || value.startsWith("blob:"))
                return value;

            return "/photos/".concat(value.substring(value.lastIndexOf("/") + 1));
        };

        let updatePreviewFromUrl = function () {
            revokeSelectedImageObjectUrl();
            let value = previewInput.value.trim();

            if (!value) {
                showEmptyPreview();
                return;
            }

            showPreview(normalizeImageSource(value));
        };

        let updatePreviewFromFile = function () {
            if (!previewFileInput)
                return;

            revokeSelectedImageObjectUrl();

            let file = previewFileInput.files && previewFileInput.files[0];

            if (!file) {
                if (filePickerLabel)
                    filePickerLabel.textContent = "Selecionar imagem";
                updatePreviewFromUrl();
                return;
            }

            selectedImageObjectUrl = URL.createObjectURL(file);

            if (filePickerLabel)
                filePickerLabel.textContent = file.name;

            showPreview(selectedImageObjectUrl);
        };

        previewInput.addEventListener("input", function () {
            if (previewFileInput && previewInput.value.trim()) {
                previewFileInput.value = "";
                previewFileInput.disabled = true;
                if (filePicker)
                    filePicker.classList.add("disabled");
                if (filePickerLabel)
                    filePickerLabel.textContent = "Arquivo desativado";
            } else if (filePicker) {
                if (previewFileInput)
                    previewFileInput.disabled = false;
                filePicker.classList.remove("disabled");
                if (filePickerLabel)
                    filePickerLabel.textContent = "Selecionar imagem";
            }

            updatePreviewFromUrl();
        });

        if (previewFileInput) {
            previewFileInput.addEventListener("change", function () {
                let hasFile = previewFileInput.files && previewFileInput.files.length > 0;

                previewInput.disabled = hasFile;
                if (hasFile)
                    previewInput.value = "";
                else if (previewFileInput)
                    previewFileInput.disabled = false;

                updatePreviewFromFile();
            });
        }

        previewImage.addEventListener("error", function () {
            showEmptyPreview();
        });
        updatePreviewFromUrl();
    }

    let dashboardInvitations = document.querySelector("[data-dashboard-invitations]");

    if (dashboardInvitations) {
        let invitationCheckUrl = dashboardInvitations.dataset.invitationCheckUrl;
        let invitationPanelSlot = dashboardInvitations.querySelector("[data-invitation-panel-slot]");
        let invitationCount = dashboardInvitations.querySelector("[data-invitation-count]");

        let setInvitationCount = function (count) {
            if (!invitationCount)
                return;

            invitationCount.textContent = String(count);
            invitationCount.hidden = count <= 0;
        };

        let updateInvitationsFromHtml = function (html) {
            let parser = new DOMParser();
            let doc = parser.parseFromString(html, "text/html");
            let nextPanelSlot = doc.querySelector("[data-invitation-panel-slot]");
            let nextInvitationCount = doc.querySelector("[data-invitation-count]");

            if (invitationPanelSlot && nextPanelSlot)
                invitationPanelSlot.innerHTML = nextPanelSlot.innerHTML;

            if (nextInvitationCount) {
                let count = Number.parseInt(nextInvitationCount.textContent.trim(), 10);
                setInvitationCount(Number.isNaN(count) ? 0 : count);
            }
        };

        if (invitationCheckUrl) {
            fetch(invitationCheckUrl, {
                headers: {
                    "X-Requested-With": "XMLHttpRequest"
                }
            })
                .then(function (response) {
                    return response.text();
                })
                .then(updateInvitationsFromHtml)
                .catch(function () {
                    setInvitationCount(0);
                });
        }
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
