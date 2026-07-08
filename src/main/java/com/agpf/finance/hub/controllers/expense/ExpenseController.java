package com.agpf.finance.hub.controllers.expense;

import com.agpf.finance.hub.dtos.expense.ExpenseRegisterDTO;
import com.agpf.finance.hub.enums.expense.CategoryExpenseType;
import com.agpf.finance.hub.enums.expense.PaymentMethod;
import com.agpf.finance.hub.enums.expense.StatusExpenseType;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.services.auth.AuthenticatedUser;
import com.agpf.finance.hub.services.expense.ExpenseService;
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

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private static final String EXPENSE_REGISTER = "expense/register";

    @GetMapping(value = "/register")
    String registerForm(Model model) {
        model.addAttribute("expense", new ExpenseRegisterDTO());
        addRegisterOptions(model);

        return EXPENSE_REGISTER;
    }

    @PostMapping(value = "/register")
    String register(@Valid @ModelAttribute("expense") ExpenseRegisterDTO dto,
                    BindingResult bindingResult, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        addRegisterOptions(model);

        if (bindingResult.hasErrors())
            return EXPENSE_REGISTER;

        try {
            expenseService.register(dto, getUser(authentication));
            redirectAttributes.addFlashAttribute("result", "Despesa cadastrada com sucesso.");
            return "redirect:/expense/register";
        } catch (Exception _) {
            model.addAttribute("registerError", """
                    Ocorreu um erro ao registrar despesa, verifique os dados e tente novamente!
                    """);
            return EXPENSE_REGISTER;
        }
    }

    @GetMapping(value = "/by-user")
    String getExpensesByUser(Model model, Authentication authentication) {
        var user = getUser(authentication);

        try {
            var expenses = expenseService.byUser(user);

            model.addAttribute("expenses", expenses);
        } catch (Exception _) {
            model.addAttribute("expenses", List.of());
            model.addAttribute("listError", """
                    Ocorreu um erro ao carregar as despesas. Tente novamente mais tarde.
                    """);
        }

        return "expense/list";
    }

    private User getUser(Authentication authentication) {
        var principal = (AuthenticatedUser) Objects.requireNonNull(authentication.getPrincipal());
        return principal.user();
    }

    private void addRegisterOptions(Model model) {
        model.addAttribute("status", StatusExpenseType.values());
        model.addAttribute("categories", CategoryExpenseType.values());
        model.addAttribute("paymentMethods", PaymentMethod.values());
    }

}
