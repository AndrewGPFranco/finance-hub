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

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeController {

    private static final String SELECTED_DATE = "selectedDate";
    private static final String SELECTED_SUBDOMAIN_ID = "selectedSubdomainId";

    private final SubdomainService subdomainService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Authentication authentication,
                                    @RequestParam(required = false) UUID subdomainId,
                                    @RequestParam(required = false) String dataDeUso, HttpSession session) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser(var user)))
            return;

        var resolvedSubdomainId = subdomainService.
                resolveSelectedSubdomainId(user, subdomainId != null ? subdomainId : getSessionSubdomainId(session));

        if (resolvedSubdomainId != null)
            session.setAttribute(SELECTED_SUBDOMAIN_ID, resolvedSubdomainId.toString());
        else
            session.removeAttribute(SELECTED_SUBDOMAIN_ID);

        var dataSelecionada = parseDataDeUso(dataDeUso);
        dataSelecionada = dataSelecionada != null ? dataSelecionada : getSessionData(session);
        session.setAttribute(SELECTED_DATE, dataSelecionada);

        model.addAttribute("navbarSubdomains", subdomainService.subdomainsByUser(user, resolvedSubdomainId, dataSelecionada));
        model.addAttribute(SELECTED_SUBDOMAIN_ID, resolvedSubdomainId);
        model.addAttribute("canManageSelectedSubdomain", subdomainService.canManage(user, resolvedSubdomainId));
        model.addAttribute(SELECTED_DATE, dataSelecionada);
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

    private LocalDate getSessionData(HttpSession session) {
        var dataSelecionada = session.getAttribute(SELECTED_DATE);

        if (dataSelecionada instanceof LocalDate data)
            return data;

        return DateUtils.getLocalDateAmericaSP().withDayOfMonth(1);
    }

    private LocalDate parseDataDeUso(String dataDeUso) {
        if (dataDeUso == null || dataDeUso.isBlank())
            return null;

        try {
            return dataDeUso.length() == 7 ? YearMonth.parse(dataDeUso).atDay(1) : LocalDate.parse(dataDeUso);
        } catch (RuntimeException _) {
            return null;
        }
    }

}
