package com.agpf.finance.hub.dtos.configs;

import com.agpf.finance.hub.enums.expense.CategoryExpenseType;
import com.agpf.finance.hub.enums.expense.PaymentMethod;
import com.agpf.finance.hub.enums.expense.StatusExpenseType;
import com.agpf.finance.hub.models.configurations.ExpenseConfig;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

@Builder
public record ExpenseConfigRegisterDTO(
        UUID idSubdominio,
        @NotNull(message = "O mês e ano são obrigatórios.")
        @DateTimeFormat(pattern = "yyyy-MM")
        YearMonth dataDeUso,
        LocalDate dataDoPagamento,
        BigDecimal valor,
        LocalDate dataDeVencimento,
        StatusExpenseType status,
        CategoryExpenseType categoria,
        PaymentMethod metodoDoPagamento
) {

    public ExpenseConfig toEntity(User user, Subdomain subdominio) {
        return new ExpenseConfig(this.dataDoPagamento, this.valor, this.dataDeVencimento, this.status,
                this.categoria, this.metodoDoPagamento, subdominio, user, this.dataDeUso);
    }

    public ExpenseConfigRegisterDTO(UUID idSubdominio) {
        this(idSubdominio, null, null, null,
                null, null, null, null);
    }

}
