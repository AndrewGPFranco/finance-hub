package com.agpf.finance.hub.services.expense;

import com.agpf.finance.hub.dtos.expense.EditExpenseDTO;
import com.agpf.finance.hub.dtos.expense.ExpenseRegisterDTO;
import com.agpf.finance.hub.enums.expense.FilterListExpenseType;
import com.agpf.finance.hub.dtos.expense.OutputExpenseDTO;
import com.agpf.finance.hub.enums.expense.CategoryExpenseType;
import com.agpf.finance.hub.enums.expense.PaymentMethod;
import com.agpf.finance.hub.enums.expense.StatusExpenseType;
import com.agpf.finance.hub.models.expense.Expense;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.expense.ExpenseRepository;
import com.agpf.finance.hub.services.auth.AuthenticatedUser;
import com.agpf.finance.hub.utils.UtilsCrud;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public void register(ExpenseRegisterDTO dto, User user) {
        var entity = ExpenseRegisterDTO.toEntity(dto, user);

        expenseRepository.save(entity);
    }

    public List<OutputExpenseDTO> byUser(User user, FilterListExpenseType filter, Sort.Direction direction) {
        return expenseRepository.findByUser(user, Sort.by(direction, filter.getFieldName()));
    }

    public Map<FilterListExpenseType, String> getPossibleFilters() {
        return Arrays.stream(FilterListExpenseType.values())
                .collect(Collectors.toMap(Function.identity(), f -> f.getDescription()));
    }

    public User getUser(Authentication authentication) {
        var principal = (AuthenticatedUser) Objects.requireNonNull(authentication.getPrincipal());
        return principal.user();
    }

    public void addRegisterOptions(Model model) {
        model.addAttribute("status", StatusExpenseType.values());
        model.addAttribute("categories", CategoryExpenseType.values());
        model.addAttribute("paymentMethods", PaymentMethod.values());
    }

    public OutputExpenseDTO getExpenseByIdAndUser(UUID idExpense, User user) {
        return OutputExpenseDTO.fromEntity(expenseRepository.findByIdAndUser(idExpense, user));
    }

    public void editExpense(EditExpenseDTO dto, UUID idExpense, User user) {
        var expense = expenseRepository.findByIdAndUser(idExpense, user);

        UtilsCrud.updateField(dto.title(), expense::setTitle);
        UtilsCrud.updateField(dto.amount(), expense::setAmount);
        UtilsCrud.updateField(dto.status(), expense::setStatus);
        UtilsCrud.updateField(dto.dueDate(), expense::setDueDate);
        UtilsCrud.updateField(dto.category(), expense::setCategory);
        UtilsCrud.updateField(dto.recurring(), expense::setRecurring);
        UtilsCrud.updateField(dto.paymentDate(), expense::setPaymentDate);
        UtilsCrud.updateField(dto.paymentMethod(), expense::setPaymentMethod);
        UtilsCrud.updateField(dto.totalInstallments(), expense::setTotalInstallments);
        UtilsCrud.updateField(dto.installmentNumber(), expense::setInstallmentNumber);

        expenseRepository.save(expense);
    }
}
