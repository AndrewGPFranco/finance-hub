package com.agpf.finance.hub.dtos.subdomain.invite;

import com.agpf.finance.hub.enums.subdomain.PermissionSubdomainType;
import com.agpf.finance.hub.enums.subdomain.StatusInviteSubdomain;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.subdomain.SubdomainInvite;
import com.agpf.finance.hub.models.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public record RegisterSubdomainInviteDTO(
        @NotNull UUID idSubdomain,
        @Email String emailGuest,
        @NotNull PermissionSubdomainType permission
) {

    public static SubdomainInvite toEntity(RegisterSubdomainInviteDTO dto, User user, Subdomain subdomain) {
        return SubdomainInvite.builder()
                .invitedBy(user).subdomain(subdomain).emailGuest(dto.emailGuest())
                .status(StatusInviteSubdomain.PENDING).token(UUID.randomUUID())
                .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .answeredAt(null).permission(dto.permission()).build();
    }

}
