package com.agpf.finance.hub.models.user;

import com.agpf.finance.hub.enums.user.UserRoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email(message = "Informe um email válido.")
    @NotBlank(message = "O email é obrigatório.")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "O nome de usuário é obrigatório.")
    @Size(max = 30, message = "O nome de usuário deve ter no máximo 30 caracteres.")
    @Column(name = "username", unique = true, nullable = false, length = 30)
    private String username;

    @NotBlank(message = "A senha é obrigatória.")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @NotNull(message = "O perfil do usuário é obrigatório.")
    @Column(name = "role", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private UserRoleType role;

    @NotBlank(message = "O sobrenome é obrigatório.")
    @Column(name = "last_name", nullable = false, length = 40)
    private String lastName;

    @NotBlank(message = "O nome é obrigatório.")
    @Column(name = "first_name", nullable = false, length = 40)
    private String firstName;

    @NotNull(message = "A data de criação é obrigatória.")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
