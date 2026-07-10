package com.agpf.finance.hub.controllers.dashboard;

import com.agpf.finance.hub.services.dashboard.DashboardService;
import com.agpf.finance.hub.services.subdomain.SubdomainService;
import com.agpf.finance.hub.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final SubdomainService subdomainService;

    @GetMapping
    public String dashboard(Model model, Authentication authentication,
                            @RequestParam(required = false) UUID subdomainId) {
        var user = UserUtils.getUser(authentication);
        var selectedSubdomainId = subdomainService.resolveSelectedSubdomainId(user, subdomainId);

        model.addAttribute("output", dashboardService.outputExpenses(user, selectedSubdomainId));
        model.addAttribute("subdomains", subdomainService.subdomainsByUser(user));
        model.addAttribute("selectedSubdomainId", selectedSubdomainId);
        return "dashboard/index";
    }

}
