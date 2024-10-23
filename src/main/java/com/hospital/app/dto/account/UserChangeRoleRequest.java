package com.hospital.app.dto.account;

import com.hospital.app.entities.account.Role;
import jakarta.validation.constraints.NotNull;

public record UserChangeRoleRequest(
        @NotNull
        Role role,
        @NotNull
        Long userId
) {
}
