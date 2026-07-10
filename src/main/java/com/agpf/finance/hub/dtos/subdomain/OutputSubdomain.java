package com.agpf.finance.hub.dtos.subdomain;

import com.agpf.finance.hub.models.subdomain.Subdomain;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OutputSubdomain(
        String urlPhoto,
        @NotNull UUID id,
        @NotBlank String name
) {

    public static OutputSubdomain fromEntity(Subdomain subdomain) {
        return new OutputSubdomain(subdomain.getUrlPhoto(), subdomain.getId(), subdomain.getName());
    }

}
