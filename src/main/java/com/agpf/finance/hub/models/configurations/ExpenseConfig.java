package com.agpf.finance.hub.models.configurations;

import com.agpf.finance.hub.enums.expense.CategoryExpenseType;
import com.agpf.finance.hub.enums.expense.PaymentMethod;
import com.agpf.finance.hub.enums.expense.StatusExpenseType;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "expense_configs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_subdomain_date_to_use", columnNames = {"subdomain_id", "date_to_use"})
        },
        indexes = {
                @Index(name = "idx_subdomain_date_to_use", columnList = "subdomain_id, date_to_use")
        }
)
public class ExpenseConfig extends UserScopedConfig {

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusExpenseType status;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private CategoryExpenseType category;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    public ExpenseConfig(LocalDate dataPagamento, BigDecimal valor, LocalDate vencimento, StatusExpenseType status,
                         CategoryExpenseType categoria, PaymentMethod metodoPagamento, Subdomain subdominio, User user, YearMonth uso) {
        this.paymentDate = dataPagamento;
        this.amount = valor;
        this.dueDate = vencimento;
        this.status = status;
        this.category = categoria;
        this.paymentMethod = metodoPagamento;
        this.subdomain = subdominio;
        this.user = user;
        this.dateToUse = uso;
    }
}
