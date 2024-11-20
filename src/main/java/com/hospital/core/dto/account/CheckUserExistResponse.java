package com.hospital.core.dto.account;

import com.hospital.core.entities.account.Role;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckUserExistResponse {
    private boolean isExist;
    private Role role;
    private String fullName;
    private String username;
    private String phone;
    private String email;
}
