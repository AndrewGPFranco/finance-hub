package com.agpf.finance.hub.services.dashboard;

import com.agpf.finance.hub.dtos.dashboard.OutputDashboardDTO;
import com.agpf.finance.hub.enums.expense.StatusExpenseType;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.services.expense.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExpenseService expenseService;

    public OutputDashboardDTO outputExpenses(User user, UUID subdomainId) {
        var expenses = expenseService.getExpensesByUser(user, subdomainId);

        var expensesPaid = (int) expenses.stream()
                .filter(expense -> expense.status() == StatusExpenseType.PAID).count();
        var overduePayments = (int) expenses.stream()
                .filter(expense -> expense.status() == StatusExpenseType.OVERDUE).count();
        var outstandingExpenses = (int) expenses.stream()
                .filter(expense -> expense.status() == StatusExpenseType.PENDING
                        || expense.status() == StatusExpenseType.OVERDUE).count();

        var amountPayableExpenses = expenses.stream()
                .map(expense -> expense.amount() == null ? BigDecimal.ZERO : expense.amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OutputDashboardDTO.builder()
                .expensesPaid(expensesPaid).totalExpenses(expenses.size()).overduePayments(overduePayments)
                .outstandingExpenses(outstandingExpenses).amountPayableExpenses(amountPayableExpenses).build();
    }

}
