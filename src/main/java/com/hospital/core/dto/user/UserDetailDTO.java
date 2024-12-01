package com.hospital.core.dto.user;

import com.hospital.core.entities.account.Accountant;
import com.hospital.core.entities.account.Dentist;
import com.hospital.core.entities.account.User;
import lombok.Builder;

@Builder
public record UserDetailDTO(
        User user,
        Dentist dentist,
        Accountant accountant
) {
}
