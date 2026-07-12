package com.agpf.finance.hub.dtos.subdomain;

import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record RegisterSubdomainDTO(
        String urlPhoto,
        MultipartFile photo,
        @NotBlank(message = "É necessário informar um nome ao subdomínio.") String name
) {

    public static Subdomain toEntity(RegisterSubdomainDTO dto, User user, String photo) {
        return Subdomain.builder().user(user).name(dto.name).urlPhoto(photo).build();
    }

}
