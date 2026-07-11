package com.agpf.finance.hub.services.expense;

import com.agpf.finance.hub.dtos.expense.EditExpenseDTO;
import com.agpf.finance.hub.dtos.expense.ExpenseRegisterDTO;
import com.agpf.finance.hub.enums.expense.FilterListExpenseType;
import com.agpf.finance.hub.dtos.expense.OutputExpenseDTO;
import com.agpf.finance.hub.enums.expense.CategoryExpenseType;
import com.agpf.finance.hub.enums.expense.PaymentMethod;
import com.agpf.finance.hub.enums.expense.StatusExpenseType;
import com.agpf.finance.hub.enums.subdomain.PermissionSubdomainType;
import com.agpf.finance.hub.exceptions.NotFoundException;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.expense.ExpenseRepository;
import com.agpf.finance.hub.repositories.subdomains.SubdomainRepository;
import com.agpf.finance.hub.services.subdomain.SubdomainService;
import com.agpf.finance.hub.utils.CrudUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final SubdomainRepository subdomainRepository;
    private final SubdomainService subdomainService;

    @Transactional
    public void register(ExpenseRegisterDTO dto, User user) {
        var subdomain = resolveManageableSubdomain(dto.subdomainId(), user);

        var entity = ExpenseRegisterDTO.toEntity(dto, user, subdomain);

        expenseRepository.save(entity);
    }

    public List<OutputExpenseDTO> byUser(User user, UUID subdomainId, FilterListExpenseType filter,
                                         Sort.Direction direction, Month month) {
        if (subdomainId == null)
            return List.of();

        return expenseRepository.findByUserAndSubdomainId(user, subdomainId,
                Sort.by(direction, filter.getFieldName()), month);
    }

    public Map<FilterListExpenseType, String> getPossibleFilters() {
        return Arrays.stream(FilterListExpenseType.values())
                .collect(Collectors.toMap(Function.identity(), f -> f.getDescription()));
    }

    public void addRegisterOptions(Model model) {
        model.addAttribute("status", StatusExpenseType.values());
        model.addAttribute("categories", CategoryExpenseType.values());
        model.addAttribute("paymentMethods", PaymentMethod.values());
    }

    public boolean canManageExpenses(User user, UUID subdomainId) {
        return subdomainService.canManage(user, subdomainId);
    }

    public OutputExpenseDTO getExpenseByIdAndUser(UUID idExpense, User user) {
        return OutputExpenseDTO.fromEntity(expenseRepository
                .findManageableByIdAndUser(idExpense, user, PermissionSubdomainType.EDITOR)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada!")));
    }

    @Transactional
    public void editExpense(EditExpenseDTO dto, UUID idExpense, User user) {
        var expense = expenseRepository.findManageableByIdAndUser(idExpense, user, PermissionSubdomainType.EDITOR)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada!"));

        CrudUtils.updateField(dto.title(), expense::setTitle);
        CrudUtils.updateField(dto.amount(), expense::setAmount);
        CrudUtils.updateField(dto.status(), expense::setStatus);
        CrudUtils.updateField(dto.dueDate(), expense::setDueDate);
        CrudUtils.updateField(dto.category(), expense::setCategory);
        CrudUtils.updateField(dto.recurring() != null && dto.recurring(), expense::setRecurring);
        CrudUtils.updateField(dto.paymentDate(), expense::setPaymentDate);
        CrudUtils.updateField(dto.paymentMethod(), expense::setPaymentMethod);
        CrudUtils.updateField(dto.totalInstallments(), expense::setTotalInstallments);
        CrudUtils.updateField(dto.installmentNumber(), expense::setInstallmentNumber);

        expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(UUID idExpense, User user) {
        var expense = expenseRepository.findManageableByIdAndUser(idExpense, user, PermissionSubdomainType.EDITOR)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada!"));

        expenseRepository.delete(expense);
    }

    public List<OutputExpenseDTO> getExpensesByUser(User user, UUID subdomainId, Month month) {
        if (subdomainId == null) return List.of();

        return expenseRepository.findByUserAndSubdomainId(user, subdomainId, month);
    }

    @Transactional
    public void repeatExpenseForNextMonth(List<UUID> idsExpense, UUID idSubdomain, User user) {
        var subdomain = resolveManageableSubdomain(idSubdomain, user);

        var expenses = expenseRepository.findAllManageableByIdAndSubdomainIdAndUser(
                idsExpense, idSubdomain, user, PermissionSubdomainType.EDITOR);

        var listNewEntities = expenses.stream().map(expense -> {
            var dto = ExpenseRegisterDTO.cloneNextMonthFromEntity(expense);
            return ExpenseRegisterDTO.toEntity(dto, user, subdomain);
        }).toList();

        expenseRepository.saveAll(listNewEntities);
    }

    private Subdomain resolveManageableSubdomain(UUID subdomainId, User user) {
        if (!canManageExpenses(user, subdomainId))
            throw new NotFoundException("Subdomínio não encontrado!");

        return subdomainRepository.findByIdAndUser(subdomainId, user)
                .orElseThrow(() -> new NotFoundException("Subdomínio não encontrado!"));
    }
}
