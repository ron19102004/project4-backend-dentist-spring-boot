package com.hospital.core.dto.account;

import com.hospital.core.entities.account.Role;
import jakarta.validation.constraints.NotNull;

public record UserChangeRoleRequest(
        @NotNull
        Role role,
        @NotNull
        Long userId
) {
}
