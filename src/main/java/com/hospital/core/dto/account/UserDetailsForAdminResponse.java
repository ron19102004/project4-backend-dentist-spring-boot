package com.hospital.core.dto.account;

import com.hospital.core.entities.account.Accountant;
import com.hospital.core.entities.account.Dentist;
import com.hospital.core.entities.account.Role;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDetailsForAdminResponse {
    private Long id;
    private String username;
    private Role role;
    private Dentist dentist;
    private Accountant accountant;
    private String fullName;
    private String email;
    private String phone;
}
