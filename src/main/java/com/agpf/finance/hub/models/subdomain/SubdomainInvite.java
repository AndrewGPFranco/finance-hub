package com.agpf.finance.hub.models.subdomain;

import com.agpf.finance.hub.enums.subdomain.PermissionSubdomainType;
import com.agpf.finance.hub.enums.subdomain.StatusInviteSubdomain;
import com.agpf.finance.hub.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "subdomain_invite",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"subdomain_id", "email_guest", "status"})
        }
)
public class SubdomainInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @JoinColumn(name = "invited_by_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User invitedBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subdomain_id", nullable = false, updatable = false)
    private Subdomain subdomain;

    @Email(message = "É necessário um email válido.")
    @NotBlank(message = "O email do convidado é obrigatório.")
    @Column(name = "email_guest", nullable = false, updatable = false)
    private String emailGuest;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(message = "O status é obrigatório.")
    private StatusInviteSubdomain status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull(message = "O token é obrigatório.")
    @Column(name = "token", nullable = false, unique = true, updatable = false)
    private UUID token;

    @Column(name = "expires_at", nullable = false)
    @NotNull(message = "A data de expiração é obrigatória.")
    private Instant expiresAt;

    @Column(name = "accepted_at")
    private Instant acceptedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    @NotNull(message = "O tipo de permissão é obrigatório.")
    private PermissionSubdomainType permission;

}
