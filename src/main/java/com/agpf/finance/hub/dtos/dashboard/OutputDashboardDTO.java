package com.agpf.finance.hub.dtos.dashboard;

import lombok.Builder;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Builder
public record OutputDashboardDTO(
        int expensesPaid,
        int totalExpenses,
        int overduePayments,
        int outstandingExpenses,
        BigDecimal amountPayableExpenses
) {
    private static final Locale BRAZIL = Locale.of("pt", "BR");

    public String formattedAmountPayableExpenses() {
        return amountPayableExpenses == null
                ? NumberFormat.getCurrencyInstance(BRAZIL).format(BigDecimal.ZERO)
                : NumberFormat.getCurrencyInstance(BRAZIL).format(amountPayableExpenses);
    }
}
