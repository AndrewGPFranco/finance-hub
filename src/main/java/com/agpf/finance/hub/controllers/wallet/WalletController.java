package com.agpf.finance.hub.controllers.wallet;

import com.agpf.finance.hub.dtos.wallet.InputWalletDTO;
import com.agpf.finance.hub.models.wallet.Wallet;
import com.agpf.finance.hub.services.wallet.WalletService;
import com.agpf.finance.hub.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/wallets")
public class WalletController {

    private final WalletService walletService;
    private static final String LIST = "wallet/list";
    private static final String REGISTER = "wallet/register";

    @GetMapping(value = "/{idSubdomain}")
    String byUser(Model model, Authentication authentication, @PathVariable UUID idSubdomain) {
        var user = UserUtils.getUser(authentication);

        model.addAttribute("wallet", walletService.byUserAndSubdomain(user, idSubdomain).orElse(null));
        return LIST;
    }

    @GetMapping(value = "/register")
    String register(Model model) {
        model.addAttribute("wallet", new Wallet(
                null, null, null, null, null, null, null
        ));
        return REGISTER;
    }

    @PostMapping(value = "/register")
    String register(@ModelAttribute("dto") InputWalletDTO input,
                    RedirectAttributes redirectAttributes, Authentication authentication) {
        var user = UserUtils.getUser(authentication);

        try {
            walletService.register(user, input);
            redirectAttributes.addAttribute("positiveFeedback", "Carteira registrada!");
        } catch (Exception _) {
            redirectAttributes.addAttribute("negativeFeedback",
                    """
                            Ocorreu um problema ao registrar a carteira!
                            """);
            redirectAttributes.addAttribute("dto", input);
            return "redirect:/wallets/register";
        }

        return "redirect:/wallets/" + input.idSubdomain();
    }

    @DeleteMapping(value = "/{idWallet}")
    String delete(Authentication authentication, @PathVariable UUID idWallet,
                  @RequestParam UUID idSubdomain, RedirectAttributes redirectAttributes) {
        var user = UserUtils.getUser(authentication);

        try {
            var deletedWalletSubdomainId = walletService.delete(idWallet, user);
            redirectAttributes.addAttribute("positiveFeedback", "Carteira excluída!");
            return "redirect:/wallets/" + deletedWalletSubdomainId;
        } catch (Exception _) {
            redirectAttributes.addAttribute("negativeFeedback", "Ocorreu um problema ao excluir a carteira!");
            return "redirect:/wallets/" + idSubdomain;
        }
    }

}
