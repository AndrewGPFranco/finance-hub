package com.agpf.finance.hub.dtos.expense;

import com.agpf.finance.hub.enums.expense.CategoryExpenseType;
import com.agpf.finance.hub.enums.expense.PaymentMethod;
import com.agpf.finance.hub.enums.expense.StatusExpenseType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EditExpenseDTO(
        String title,
        BigDecimal amount,
        LocalDate dueDate,
        Boolean recurring,
        LocalDate paymentDate,
        StatusExpenseType status,
        Integer totalInstallments,
        Integer installmentNumber,
        CategoryExpenseType category,
        PaymentMethod paymentMethod
) {
}
