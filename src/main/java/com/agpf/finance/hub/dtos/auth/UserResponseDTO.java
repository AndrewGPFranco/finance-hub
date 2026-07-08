package com.agpf.finance.hub.dtos.auth;

import com.agpf.finance.hub.enums.user.UserRoleType;

import java.util.UUID;

public record UserResponseDTO(UUID id, String email, String username, String firstName, String lastName,
                              UserRoleType role) {
}
