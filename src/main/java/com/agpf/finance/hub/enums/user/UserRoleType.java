package com.agpf.finance.hub.enums.user;

import lombok.Getter;

@Getter
public enum UserRoleType {

    USER("Usuário"),
    MODERATOR("Moderador"),
    ADMIN("Administrador");

    private final String label;

    UserRoleType(String label) {
        this.label = label;
    }
}
