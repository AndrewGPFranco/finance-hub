package com.agpf.finance.hub.dtos.expense;

import com.agpf.finance.hub.enums.expense.CategoryExpenseType;
import com.agpf.finance.hub.enums.expense.PaymentMethod;
import com.agpf.finance.hub.enums.expense.StatusExpenseType;
import com.agpf.finance.hub.models.expense.Expense;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.agpf.finance.hub.utils.DateUtils.getLocalDateTimeAmericaSP;

public record ExpenseRegisterDTO(
        @NotBlank(message = "O título é obrigatório!")
        @Size(max = 120, message = "O título deve ter no máximo 120 caracteres.")
        String title,

        LocalDate paymentDate,

        @NotNull(message = "É necessário informar o valor!")
        @Positive(message = "O valor deve ser maior que zero.")
        BigDecimal amount,

        @NotNull(message = "A data de vencimento é obrigatória.")
        LocalDate dueDate,

        @NotNull(message = "O status é obrigatório.")
        StatusExpenseType status,

        @NotNull(message = "A categoria é obrigatória.")
        CategoryExpenseType category,

        @NotNull(message = "A forma de pagamento é obrigatória.")
        PaymentMethod paymentMethod,

        boolean recurring,

        Integer installmentNumber,

        Integer totalInstallments,

        @NotNull(message = "Um subdomínio precisa estar vínculado a despesa.")
        UUID subdomainId
) {
    public ExpenseRegisterDTO() {
        this(null, null, null,
                null, null, null, null,
                false, null, null, null);
    }

    public ExpenseRegisterDTO(UUID subdomainId) {
        this(null, null, null,
                null, null, null, null,
                false, null, null, subdomainId);
    }

    public static Expense toEntity(ExpenseRegisterDTO dto, User user, Subdomain subdomain) {
        return Expense.builder()
                .title(dto.title()).paymentDate(dto.paymentDate()).amount(dto.amount())
                .paymentMethod(dto.paymentMethod()).user(user).dueDate(dto.dueDate())
                .createdAt(getLocalDateTimeAmericaSP()).status(dto.status()).category(dto.category())
                .recurring(dto.recurring()).installmentNumber(dto.installmentNumber()).subdomain(subdomain)
                .totalInstallments(dto.totalInstallments()).build();
    }
}
