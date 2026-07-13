package com.agpf.finance.hub.models.wallet;

import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "wallets",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_WALLET_SUBDOMAIN", columnNames = {"subdomain_id"})
        },
        indexes = {
                @Index(name = "IDX_WALLET_USER", columnList = "user_id")
        }
)
public class Wallet {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Um usuário deve ser vínculado a carteira.")
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "name", nullable = false, length = 25)
    @NotBlank(message = "É necessário informar um nome a carteira.")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Um subdomínio deve ser vínculado a carteira.")
    @JoinColumn(name = "subdomain_id", nullable = false, unique = true)
    private Subdomain subdomain;

    @PositiveOrZero(message = "A quantidade deve ser 0 ou positivo.")
    @Column(name = "balance", precision = 12, scale = 2, nullable = false)
    private BigDecimal balance;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

}
