package com.agpf.finance.hub.services.dashboard;

import com.agpf.finance.hub.dtos.dashboard.OutputDashboardDTO;
import com.agpf.finance.hub.dtos.wallet.OutputWalletDTO;
import com.agpf.finance.hub.enums.expense.StatusExpenseType;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.services.expense.ExpenseService;
import com.agpf.finance.hub.services.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final WalletService walletService;
    private final ExpenseService expenseService;

    public OutputDashboardDTO outputExpenses(User user, UUID subdomainId, Month month) {
        var expenses = expenseService.getExpensesByUser(user, subdomainId, month);


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

        var wallet = walletService.byUserAndSubdomain(user, subdomainId);

        BigDecimal remainingAmount = null;

        if (wallet.isPresent())
            remainingAmount = wallet.get().balance().subtract(amountPayableExpenses);

        return OutputDashboardDTO.builder()
                .remainingAmount(remainingAmount)
                .expensesPaid(expensesPaid).totalExpenses(expenses.size()).overduePayments(overduePayments)
                .outstandingExpenses(outstandingExpenses).amountPayableExpenses(amountPayableExpenses).build();
    }

}
