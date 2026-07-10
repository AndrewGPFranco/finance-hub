package com.agpf.finance.hub.controllers.common;

import com.agpf.finance.hub.services.auth.AuthenticatedUser;
import com.agpf.finance.hub.services.subdomain.SubdomainService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeController {

    private static final String SELECTED_SUBDOMAIN_ID = "selectedSubdomainId";

    private final SubdomainService subdomainService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Authentication authentication,
                                    @RequestParam(required = false) UUID subdomainId,
                                    HttpSession session) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser))
            return;

        var user = authenticatedUser.user();
        var resolvedSubdomainId = subdomainService.resolveSelectedSubdomainId(
                user,
                subdomainId != null ? subdomainId : getSessionSubdomainId(session)
        );

        if (resolvedSubdomainId != null)
            session.setAttribute(SELECTED_SUBDOMAIN_ID, resolvedSubdomainId.toString());
        else
            session.removeAttribute(SELECTED_SUBDOMAIN_ID);

        model.addAttribute("navbarSubdomains", subdomainService.subdomainsByUser(user));
        model.addAttribute(SELECTED_SUBDOMAIN_ID, resolvedSubdomainId);
    }

    private UUID getSessionSubdomainId(HttpSession session) {
        var selectedSubdomainId = session.getAttribute(SELECTED_SUBDOMAIN_ID);

        if (!(selectedSubdomainId instanceof String value))
            return null;

        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException _) {
            return null;
        }
    }
}
