package com.agpf.finance.hub.dtos.subdomain.invite;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record OutputInvitationDTO(
        @NotBlank UUID token,
        @NotBlank String username,
        @NotNull Instant expiresAt,
        @NotBlank String titleSubdomain
) {
}
