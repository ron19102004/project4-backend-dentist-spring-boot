package com.hospital.core.mappers;

import com.hospital.core.dto.user.UserDetailDTO;
import com.hospital.core.entities.account.Accountant;
import com.hospital.core.entities.account.Dentist;
import com.hospital.core.entities.account.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public UserDetailDTO userToUserDetailDTO(User user, Dentist dentist, Accountant accountant) {
        return UserDetailDTO.builder()
                .user(user)
                .dentist(dentist)
                .accountant(accountant)
                .build();
    }
}
