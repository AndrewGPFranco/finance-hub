package com.agpf.finance.hub.controllers.subdomain;

import com.agpf.finance.hub.dtos.subdomain.invite.RegisterSubdomainInviteDTO;
import com.agpf.finance.hub.exceptions.NotFoundException;
import com.agpf.finance.hub.services.subdomain.SubdomainInviteService;
import com.agpf.finance.hub.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subdomain/invite")
public class SubdomainInviteController {

    private final SubdomainInviteService inviteService;

    @GetMapping(value = "/check-invitations")
    String checkInvitations(RedirectAttributes redirectAttributes, Authentication authentication) {
        var invitations = inviteService.checkInvitations(UserUtils.getUser(authentication));

        redirectAttributes.addFlashAttribute("invitations", invitations);

        return "redirect:/dashboard";
    }

    @PostMapping
    String invite(RedirectAttributes redirectAttributes,
                  @Valid @ModelAttribute("registerDTO") RegisterSubdomainInviteDTO dto, Authentication authentication) {
        var user = UserUtils.getUser(authentication);

        try {
            inviteService.invite(dto, user);
            redirectAttributes.addFlashAttribute("positiveFeedback", "O convite foi enviado com sucesso!");
        } catch (NotFoundException nfe) {
            redirectAttributes.addFlashAttribute("negativeFeedback", nfe.getMessage());
        } catch (Exception _) {
            redirectAttributes.addFlashAttribute("negativeFeedback", "Ocorreu um erro ao enviar o convite!");
        }

        return "redirect:/subdomain/by-user";
    }

}
