package com.agpf.finance.hub.controllers.dashboard;

import com.agpf.finance.hub.services.dashboard.DashboardService;
import com.agpf.finance.hub.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model, Authentication authentication, @RequestParam("month") Month month,
                            @ModelAttribute("selectedSubdomainId") UUID selectedSubdomainId) {
        var user = UserUtils.getUser(authentication);

        model.addAttribute("output", dashboardService.outputExpenses(user, selectedSubdomainId, month));
        return "dashboard/index";
    }

}
