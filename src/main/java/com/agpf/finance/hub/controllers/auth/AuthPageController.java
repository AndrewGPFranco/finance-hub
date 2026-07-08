package com.agpf.finance.hub.controllers.auth;

import com.agpf.finance.hub.dtos.auth.RegisterRequestDTO;
import com.agpf.finance.hub.services.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/auth")
public class AuthPageController {

    private final AuthService authService;
    private static final String AUTH_REGISTER = "auth/register";
    private static final String REDIRECT_DASHBOARD = "redirect:/dashboard";

    public AuthPageController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated())
            return REDIRECT_DASHBOARD;

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated())
            return REDIRECT_DASHBOARD;

        model.addAttribute("form",
                new RegisterRequestDTO("", "", "", "", ""));
        return AUTH_REGISTER;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegisterRequestDTO form, BindingResult bindingResult, Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated())
            return REDIRECT_DASHBOARD;

        if (bindingResult.hasErrors())
            return AUTH_REGISTER;

        try {
            authService.register(form);
            return "redirect:/auth/login?registered";
        } catch (ResponseStatusException exception) {
            model.addAttribute("registerError", exception.getReason());
            return AUTH_REGISTER;
        }
    }
}
