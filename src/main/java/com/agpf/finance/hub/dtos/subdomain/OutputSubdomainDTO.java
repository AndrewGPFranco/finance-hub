package com.agpf.finance.hub.dtos.subdomain;

import com.agpf.finance.hub.enums.subdomain.PermissionSubdomainType;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OutputSubdomainDTO(
        boolean isOwner,
        String urlPhoto,
        @NotNull UUID id,
        @NotBlank String name,
        @NotNull PermissionSubdomainType permission
) {

    public static OutputSubdomainDTO fromEntity(Subdomain subdomain, boolean isOwner) {
        return new OutputSubdomainDTO(isOwner, subdomain.getUrlPhoto(),
                subdomain.getId(), subdomain.getName(), PermissionSubdomainType.EDITOR);
    }

}
