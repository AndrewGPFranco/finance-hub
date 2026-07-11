package com.agpf.finance.hub.controllers.common;

import com.agpf.finance.hub.services.auth.AuthenticatedUser;
import com.agpf.finance.hub.services.subdomain.SubdomainService;
import com.agpf.finance.hub.utils.DateUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Month;
import java.util.Arrays;
import java.util.UUID;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeController {

    private static final String SELECTED_SUBDOMAIN_ID = "selectedSubdomainId";
    private static final String SELECTED_MONTH = "selectedMonth";

    private final SubdomainService subdomainService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Authentication authentication,
                                    @RequestParam(required = false) UUID subdomainId,
                                    @RequestParam(required = false) Month month, HttpSession session) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser(var user)))
            return;

        var resolvedSubdomainId = subdomainService.resolveSelectedSubdomainId(user,
                subdomainId != null ? subdomainId : getSessionSubdomainId(session));

        if (resolvedSubdomainId != null)
            session.setAttribute(SELECTED_SUBDOMAIN_ID, resolvedSubdomainId.toString());
        else
            session.removeAttribute(SELECTED_SUBDOMAIN_ID);

        var selectedMonth = month != null ? month : getSessionMonth(session);
        session.setAttribute(SELECTED_MONTH, selectedMonth.name());

        model.addAttribute("navbarSubdomains", subdomainService.subdomainsByUser(user));
        model.addAttribute(SELECTED_SUBDOMAIN_ID, resolvedSubdomainId);
        model.addAttribute("monthOptions", Arrays.asList(Month.values()));
        model.addAttribute(SELECTED_MONTH, selectedMonth);
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

    private Month getSessionMonth(HttpSession session) {
        var selectedMonth = session.getAttribute(SELECTED_MONTH);

        if (!(selectedMonth instanceof String value))
            return DateUtils.getLocalDateTimeAmericaSP().getMonth();

        try {
            return Month.valueOf(value);
        } catch (IllegalArgumentException _) {
            return DateUtils.getLocalDateTimeAmericaSP().getMonth();
        }
    }
}
