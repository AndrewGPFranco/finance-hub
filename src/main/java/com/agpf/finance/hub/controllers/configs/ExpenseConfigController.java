package com.agpf.finance.hub.controllers.configs;

import com.agpf.finance.hub.dtos.configs.ExpenseConfigRegisterDTO;
import com.agpf.finance.hub.exceptions.BusinessException;
import com.agpf.finance.hub.services.configs.ExpenseConfigService;
import com.agpf.finance.hub.services.expense.ExpenseService;
import com.agpf.finance.hub.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/config/expense")
public class ExpenseConfigController {

    private final ExpenseService expenseService;
    private final ExpenseConfigService configService;

    @GetMapping(value = "/register")
    String formRegistro(Model model, Authentication authentication,
                        @ModelAttribute("selectedSubdomainId") UUID selectedSubdomainId, RedirectAttributes redirectAttributes) {
        var user = UserUtils.getUser(authentication);

        if (!expenseService.canManageExpenses(user, selectedSubdomainId)) {
            redirectAttributes.addFlashAttribute("listError", "Você não tem permissão para cadastrar despesas neste subdomínio.");
            return "redirect:/dashboard";
        }

        model.addAttribute("config", new ExpenseConfigRegisterDTO(selectedSubdomainId));
        expenseService.addRegisterOptions(model);

        return "config/expense/register";
    }

    @PostMapping(value = "/register")
    String registrarConfig(Authentication authentication, RedirectAttributes redirectAttributes,
                           @Valid @ModelAttribute("config") ExpenseConfigRegisterDTO dto,
                           BindingResult bindingResult, Model model) {
        var user = UserUtils.getUser(authentication);

        if (!expenseService.canManageExpenses(user, dto.idSubdominio())) {
            redirectAttributes.addFlashAttribute("listError", "Você não tem permissão para cadastrar despesas neste subdomínio.");
            return "redirect:/dashboard";
        }

        if (bindingResult.hasErrors()) {
            expenseService.addRegisterOptions(model);
            return "config/expense/register";
        }

        try {
            configService.registrarConfig(dto, user);
            redirectAttributes.addFlashAttribute("result", "Configuração de despesa registrada com sucesso!");
        } catch (BusinessException businessException) {
            redirectAttributes.addFlashAttribute("negativeFeedback", businessException.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("negativeFeedback", "Ocorreu um erro ao realizar o registro da configuração");
        }

        return "redirect:/dashboard";
    }

}
