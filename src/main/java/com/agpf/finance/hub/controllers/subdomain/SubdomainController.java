package com.agpf.finance.hub.controllers.subdomain;

import com.agpf.finance.hub.dtos.subdomain.EditSubdomainDTO;
import com.agpf.finance.hub.dtos.subdomain.RegisterSubdomainDTO;
import com.agpf.finance.hub.exceptions.BusinessException;
import com.agpf.finance.hub.services.subdomain.SubdomainService;
import com.agpf.finance.hub.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subdomain")
public class SubdomainController {

    private static final String SUBDOMAIN_REGISTER = "subdomain/register";

    private final SubdomainService subdomainService;

    @GetMapping(value = "/register")
    String registerForm(Model model) {
        model.addAttribute("subdomain", new RegisterSubdomainDTO(null, null, null));
        return SUBDOMAIN_REGISTER;
    }

    @PostMapping(value = "/register")
    String register(@Valid @ModelAttribute("subdomain") RegisterSubdomainDTO dto,
                    BindingResult bindingResult, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors())
            return SUBDOMAIN_REGISTER;

        try {
            subdomainService.register(dto, UserUtils.getUser(authentication));
            redirectAttributes.addFlashAttribute("result", "Subdomínio cadastrado com sucesso.");
            return "redirect:/subdomain/by-user";
        } catch (Exception exception) {
            model.addAttribute("registerError", exception.getMessage());
            return SUBDOMAIN_REGISTER;
        }
    }

    @GetMapping(value = "/by-user")
    String getSubdomainsByUser(Model model, Authentication authentication) {
        var user = UserUtils.getUser(authentication);

        try {
            var subdomains = subdomainService.subdomainsByUser(user);
            model.addAttribute("subdomains", subdomains);
        } catch (Exception _) {
            model.addAttribute("subdomains", List.of());
            model.addAttribute("listError", "Ocorreu um erro ao carregar os subdomínios. Tente novamente mais tarde.");
        }

        return "subdomain/list";
    }

    @GetMapping(value = "/{idSubdomain}/edit")
    String editForm(Model model, Authentication authentication, @PathVariable UUID idSubdomain) {
        var user = UserUtils.getUser(authentication);
        var subdomain = subdomainService.getSubdomainByIdAndUser(idSubdomain, user);

        model.addAttribute("subdomain", subdomain);
        model.addAttribute("idSubdomain", idSubdomain);

        return "subdomain/edit";
    }

    @PutMapping(value = "/{idSubdomain}")
    String editSubdomain(Authentication authentication, EditSubdomainDTO dto, @PathVariable UUID idSubdomain,
                         RedirectAttributes redirectAttributes) {
        var user = UserUtils.getUser(authentication);

        subdomainService.edit(dto, idSubdomain, user);

        redirectAttributes.addFlashAttribute("result", "Subdomínio editado com sucesso.");
        return "redirect:/subdomain/by-user";
    }

    @DeleteMapping(value = "/{idSubdomain}")
    String deleteSubdomain(Authentication authentication, @PathVariable UUID idSubdomain, RedirectAttributes redirectAttributes) {
        var user = UserUtils.getUser(authentication);

        try {
            subdomainService.delete(idSubdomain, user);
            redirectAttributes.addFlashAttribute("result", "Subdomínio deletado com sucesso.");
        } catch (BusinessException businessException) {
            redirectAttributes.addFlashAttribute("negativeFeedback", businessException.getMessage());
        } catch (Exception _) {
            redirectAttributes.addFlashAttribute("negativeFeedback", "Ocorreu um erro ao realizar a deleção do subdomínio");
        }

        return "redirect:/subdomain/by-user";
    }

}
