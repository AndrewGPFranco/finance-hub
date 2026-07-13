package com.agpf.finance.hub.controllers;

import com.agpf.finance.hub.services.auth.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser)
            return "redirect:/dashboard";

        return "redirect:/auth/login";
    }
}
