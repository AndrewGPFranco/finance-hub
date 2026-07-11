package com.agpf.finance.hub.dtos.subdomain.invite;

import com.agpf.finance.hub.enums.subdomain.ResponseInvitationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ResponseInvitationDTO(
        @NotBlank UUID token,
        @NotNull ResponseInvitationType response
) {
}
