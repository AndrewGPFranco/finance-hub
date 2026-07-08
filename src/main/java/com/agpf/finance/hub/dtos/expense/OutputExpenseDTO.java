package com.agpf.finance.hub.dtos.expense;

import com.agpf.finance.hub.enums.expense.CategoryExpenseType;
import com.agpf.finance.hub.enums.expense.PaymentMethod;
import com.agpf.finance.hub.enums.expense.StatusExpenseType;
import com.agpf.finance.hub.models.expense.Expense;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public record OutputExpenseDTO(
        UUID id,
        String title,
        BigDecimal amount,
        LocalDate dueDate,
        LocalDate paymentDate,
        StatusExpenseType status,
        CategoryExpenseType category,
        PaymentMethod paymentMethod,
        boolean recurring,
        Integer installmentNumber,
        Integer totalInstallments
) {
    private static final Locale BRAZIL = Locale.of("pt", "BR");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static OutputExpenseDTO fromEntity(Expense expense) {
        return new OutputExpenseDTO(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getDueDate(),
                expense.getPaymentDate(),
                expense.getStatus(),
                expense.getCategory(),
                expense.getPaymentMethod(),
                expense.isRecurring(),
                expense.getInstallmentNumber(),
                expense.getTotalInstallments()
        );
    }

    public static List<OutputExpenseDTO> fromEntities(List<Expense> expenses) {
        return expenses.stream()
                .map(OutputExpenseDTO::fromEntity)
                .toList();
    }

    public String formattedAmount() {
        return amount == null ? "-" : NumberFormat.getCurrencyInstance(BRAZIL).format(amount);
    }

    public String formattedDueDate() {
        return formatDate(dueDate);
    }

    public String formattedPaymentDate() {
        return formatDate(paymentDate);
    }

    public String statusLabel() {
        if (status == null)
            return "-";

        return switch (status) {
            case PAID -> "Pago";
            case PENDING -> "Pendente";
            case OVERDUE -> "Atrasado";
            case CANCELED -> "Cancelado";
        };
    }

    public String categoryLabel() {
        if (category == null)
            return "-";

        return switch (category) {
            case FOOD -> "Alimentação";
            case HOUSING -> "Moradia";
            case TRANSPORT -> "Transporte";
            case HEALTH -> "Saúde";
            case EDUCATION -> "Educação";
            case ENTERTAINMENT -> "Entretenimento";
            case OTHER -> "Outros";
        };
    }

    public String paymentMethodLabel() {
        if (paymentMethod == null)
            return "-";

        return switch (paymentMethod) {
            case PIX -> "PIX";
            case DEBIT_CARD -> "Cartão de débito";
            case CREDIT_CARD -> "Cartão de crédito";
            case TRANSFER -> "Transferência";
            case OTHER -> "Outro";
        };
    }

    public String recurringLabel() {
        return recurring ? "Recorrente" : "Única";
    }

    public String installmentsLabel() {
        if (installmentNumber == null || totalInstallments == null)
            return "-";

        return installmentNumber + "/" + totalInstallments;
    }

    private static String formatDate(LocalDate date) {
        return date == null ? "-" : date.format(DATE_FORMATTER);
    }
}
