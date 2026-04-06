package com.safenutri.safenutri_api.model.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN(1L, "ADMIN"),
    NUTRICIONISTA(2L, "NUTRICIONISTA"),
    USER(3L, "USER");

    private final Long id;
    private final String role;

    RoleEnum(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
