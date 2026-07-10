package com.agpf.finance.hub.controllers.dashboard;

import com.agpf.finance.hub.services.dashboard.DashboardService;
import com.agpf.finance.hub.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model, Authentication authentication) {
        var outputUser = dashboardService.outputExpenses(UserUtils.getUser(authentication));

        model.addAttribute("output", outputUser);
        return "dashboard/index";
    }

}
