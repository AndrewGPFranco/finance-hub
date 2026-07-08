package com.agpf.finance.hub.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @Email(message = "Informe um email válido.")
        @NotBlank(message = "O email é obrigatório.")
        String email,

        @NotBlank(message = "O nome de usuário é obrigatório.")
        @Size(max = 30, message = "O nome de usuário deve ter no máximo 30 caracteres.")
        String username,

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 40, message = "O nome deve ter no máximo 40 caracteres.")
        String firstName,

        @NotBlank(message = "O sobrenome é obrigatório.")
        @Size(max = 40, message = "O sobrenome deve ter no máximo 40 caracteres.")
        String lastName,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres.")
        String password
) {
}
