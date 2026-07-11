package com.agpf.finance.hub.models.subdomain;

import com.agpf.finance.hub.enums.subdomain.PermissionSubdomainType;
import com.agpf.finance.hub.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "subdomain_member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"subdomain_id", "user_id"})
        }
)
public class SubdomainMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subdomain_id", nullable = false)
    private Subdomain subdomain;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    @NotNull(message = "O tipo de permissão é obrigatório.")
    private PermissionSubdomainType permission;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

}
