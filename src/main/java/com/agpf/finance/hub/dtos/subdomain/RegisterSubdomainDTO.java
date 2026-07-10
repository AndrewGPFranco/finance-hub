package com.agpf.finance.hub.dtos.subdomain;

import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import jakarta.validation.constraints.NotBlank;

public record RegisterSubdomainDTO(
        String urlPhoto,
        @NotBlank(message = "É necessário informar um nome ao subdomínio.") String name
) {

    public static Subdomain toEntity(RegisterSubdomainDTO dto, User user) {
        return Subdomain.builder().user(user)
                .name(dto.name).urlPhoto(dto.urlPhoto).build();
    }

}
