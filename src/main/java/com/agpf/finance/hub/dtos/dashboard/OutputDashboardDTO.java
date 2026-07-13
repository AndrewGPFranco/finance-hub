package com.agpf.finance.hub.dtos.dashboard;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

@Builder
public record OutputDashboardDTO(
        int expensesPaid,
        int totalExpenses,
        int overduePayments,
        int outstandingExpenses,
        BigDecimal remainingAmount,
        BigDecimal amountPayableExpenses
) {
    private static final Locale BRAZIL = Locale.of("pt", "BR");

    public String formattedAmountPayableExpenses() {
        return amountPayableExpenses == null
                ? NumberFormat.getCurrencyInstance(BRAZIL).format(BigDecimal.ZERO)
                : NumberFormat.getCurrencyInstance(BRAZIL).format(amountPayableExpenses);
    }

    public String formattedRemainingAmount() {
        return remainingAmount == null
                ? NumberFormat.getCurrencyInstance(BRAZIL).format(BigDecimal.ZERO)
                : NumberFormat.getCurrencyInstance(BRAZIL).format(remainingAmount);
    }

    public int expensesPaidPercentage() {
        return percentage(expensesPaid, totalExpenses);
    }

    public int outstandingExpensesPercentage() {
        return percentage(outstandingExpenses, totalExpenses);
    }

    public int overduePaymentsPercentage() {
        return percentage(overduePayments, totalExpenses);
    }

    public int amountPayableExpensesPercentage() {
        var amount = valueOrZero(amountPayableExpenses);
        var remaining = positiveValueOrZero(remainingAmount);
        return percentage(amount, amount.add(remaining));
    }

    public int remainingAmountPercentage() {
        var amount = valueOrZero(amountPayableExpenses);
        var remaining = positiveValueOrZero(remainingAmount);
        return percentage(remaining, amount.add(remaining));
    }

    public boolean hasWalletBalance() {
        return remainingAmount != null;
    }

    private int percentage(int value, int total) {
        if (total <= 0)
            return 0;

        return BigDecimal.valueOf(value)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 0, RoundingMode.HALF_UP)
                .intValue();
    }

    private int percentage(BigDecimal value, BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) <= 0)
            return 0;

        return valueOrZero(value)
                .multiply(BigDecimal.valueOf(100))
                .divide(total, 0, RoundingMode.HALF_UP)
                .intValue();
    }

    private BigDecimal valueOrZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private BigDecimal positiveValueOrZero(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0)
            return BigDecimal.ZERO;

        return value;
    }
}
