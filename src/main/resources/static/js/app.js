document.addEventListener("DOMContentLoaded", function () {
    let themeStorageKey = "FINANCE_THEME";
    let themeToggleButtons = document.querySelectorAll("[data-theme-toggle]");
    let preferredThemeMedia = window.matchMedia ? window.matchMedia("(prefers-color-scheme: dark)") : null;

    let getStoredTheme = function () {
        try {
            return localStorage.getItem(themeStorageKey);
        } catch (error) {
            return null;
        }
    };

    let storeTheme = function (theme) {
        try {
            localStorage.setItem(themeStorageKey, theme);
        } catch (error) {
            return;
        }
    };

    let getCurrentTheme = function () {
        let theme = document.documentElement.dataset.theme;

        if (theme === "dark" || theme === "light")
            return theme;

        return preferredThemeMedia && preferredThemeMedia.matches ? "dark" : "light";
    };

    let updateThemeToggleButtons = function (theme) {
        document.querySelectorAll("[data-theme-toggle]").forEach(function (button) {
            let isDark = theme === "dark";
            let label = isDark ? "Modo claro" : "Modo escuro";
            let icon = isDark ? "☀" : "☾";
            let labelElement = button.querySelector("[data-theme-toggle-label]");
            let iconElement = button.querySelector("[data-theme-toggle-icon]");

            button.setAttribute("aria-label", label);
            button.setAttribute("title", label);

            if (labelElement)
                labelElement.textContent = label;

            if (iconElement)
                iconElement.textContent = icon;
        });
    };

    let applyTheme = function (theme, persist) {
        document.documentElement.dataset.theme = theme;
        document.documentElement.classList.toggle("app-dark", theme === "dark");
        updateThemeToggleButtons(theme);

        if (persist)
            storeTheme(theme);
    };

    let bindThemeToggleButtons = function () {
        document.querySelectorAll("[data-theme-toggle]").forEach(function (button) {
            button.addEventListener("click", function () {
                applyTheme(getCurrentTheme() === "dark" ? "light" : "dark", true);
            });
        });
    };

    if (themeToggleButtons.length === 0 && document.body.classList.contains("auth-page")) {
        let authPanel = document.querySelector(".auth-panel");
        let authThemeToggle = document.createElement("button");
        authThemeToggle.className = "theme-toggle auth-theme-toggle";
        authThemeToggle.type = "button";
        authThemeToggle.dataset.themeToggle = "";
        authThemeToggle.innerHTML = "<span class=\"theme-toggle-icon\" aria-hidden=\"true\" data-theme-toggle-icon></span><span class=\"sidebar-text\" data-theme-toggle-label></span>";

        if (authPanel)
            authPanel.appendChild(authThemeToggle);
        else
            document.body.appendChild(authThemeToggle);
    }

    bindThemeToggleButtons();
    applyTheme(getCurrentTheme(), false);

    if (preferredThemeMedia) {
        let syncThemeWithPreference = function (event) {
            if (!getStoredTheme())
                applyTheme(event.matches ? "dark" : "light", false);
        };

        if (preferredThemeMedia.addEventListener)
            preferredThemeMedia.addEventListener("change", syncThemeWithPreference);
        else if (preferredThemeMedia.addListener)
            preferredThemeMedia.addListener(syncThemeWithPreference);
    }

    document.querySelectorAll("[data-password-toggle]").forEach(function (button) {
        let passwordInput = document.getElementById(button.dataset.passwordToggle);

        if (!passwordInput)
            return;

        button.addEventListener("click", function () {
            let shouldShow = passwordInput.type === "password";

            passwordInput.type = shouldShow ? "text" : "password";
            button.setAttribute("aria-label", shouldShow ? "Ocultar senha" : "Mostrar senha");
            button.setAttribute("title", shouldShow ? "Ocultar senha" : "Mostrar senha");
        });
    });

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
