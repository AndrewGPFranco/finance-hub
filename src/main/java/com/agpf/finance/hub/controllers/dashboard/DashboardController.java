package com.agpf.finance.hub.controllers.dashboard;

import com.agpf.finance.hub.services.dashboard.DashboardService;
import com.agpf.finance.hub.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Month;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model, Authentication authentication,
                            @ModelAttribute("selectedMonth") Month selectedMonth,
                            @ModelAttribute("selectedSubdomainId") UUID selectedSubdomainId) {
        var user = UserUtils.getUser(authentication);

        model.addAttribute("output", dashboardService.outputExpenses(user, selectedSubdomainId, selectedMonth));
        return "dashboard/index";
    }

}
