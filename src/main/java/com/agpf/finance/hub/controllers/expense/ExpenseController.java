package com.agpf.finance.hub.controllers.expense;

import com.agpf.finance.hub.dtos.expense.EditExpenseDTO;
import com.agpf.finance.hub.dtos.expense.ExpenseRegisterDTO;
import com.agpf.finance.hub.enums.expense.FilterListExpenseType;
import com.agpf.finance.hub.services.expense.ExpenseService;
import com.agpf.finance.hub.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Month;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private static final String EXPENSE_REGISTER = "expense/register";

    @GetMapping(value = "/register")
    String registerForm(Model model, @ModelAttribute("selectedSubdomainId") UUID selectedSubdomainId) {
        model.addAttribute("expense", new ExpenseRegisterDTO(selectedSubdomainId));
        expenseService.addRegisterOptions(model);

        return EXPENSE_REGISTER;
    }

    @PostMapping(value = "/register")
    String register(@Valid @ModelAttribute("expense") ExpenseRegisterDTO dto,
                    BindingResult bindingResult, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        var user = UserUtils.getUser(authentication);
        model.addAttribute("selectedSubdomainId", dto.subdomainId());
        expenseService.addRegisterOptions(model);

        if (bindingResult.hasErrors())
            return EXPENSE_REGISTER;

        try {
            expenseService.register(dto, user);
            redirectAttributes.addFlashAttribute("result", "Despesa cadastrada com sucesso.");
            redirectAttributes.addAttribute("subdomainId", dto.subdomainId());
            return "redirect:/expense/register";
        } catch (Exception _) {
            model.addAttribute("registerError", """
                    Ocorreu um erro ao registrar despesa, verifique os dados e tente novamente!
                    """);
            return EXPENSE_REGISTER;
        }
    }

    @GetMapping(value = "/by-user")
    String getExpensesByUser(Model model, Authentication authentication,
                             @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                             @RequestParam(defaultValue = "TITLE") FilterListExpenseType filter,
                             @ModelAttribute("selectedMonth") Month selectedMonth,
                             @ModelAttribute("selectedSubdomainId") UUID selectedSubdomainId) {
        var user = UserUtils.getUser(authentication);

        try {
            var expenses = expenseService.byUser(user, selectedSubdomainId, filter, direction, selectedMonth);

            model.addAttribute("expenses", expenses);
            model.addAttribute("filters", expenseService.getPossibleFilters());
        } catch (Exception _) {
            model.addAttribute("expenses", List.of());
            model.addAttribute("listError", """
                    Ocorreu um erro ao carregar as despesas. Tente novamente mais tarde.
                    """);
            model.addAttribute("filters", expenseService.getPossibleFilters());
        }

        return "expense/list";
    }

    @GetMapping(value = "/{idExpense}/edit")
    String editForm(Model model, Authentication authentication, @PathVariable UUID idExpense) {
        var user = UserUtils.getUser(authentication);
        var expense = expenseService.getExpenseByIdAndUser(idExpense, user);

        model.addAttribute("expense", expense);
        model.addAttribute("idExpense", idExpense);
        expenseService.addRegisterOptions(model);

        return "expense/edit";
    }

    @PutMapping(value = "/{idExpense}")
    String editExpense(Authentication authentication, EditExpenseDTO dto, @PathVariable UUID idExpense) {
        var user = UserUtils.getUser(authentication);

        expenseService.editExpense(dto, idExpense, user);

        return "redirect:/expense/by-user";
    }

    @DeleteMapping(value = "/{idExpense}")
    String deleteExpense(Authentication authentication, @PathVariable UUID idExpense) {
        var user = UserUtils.getUser(authentication);

        expenseService.deleteExpense(idExpense, user);

        return "redirect:/expense/by-user";
    }

}
