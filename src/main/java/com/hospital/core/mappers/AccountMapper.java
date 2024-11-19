package com.hospital.core.mappers;

import com.hospital.core.dto.account.AccountantDentistCreateRequest;
import com.hospital.core.dto.account.CheckUserExistResponse;
import com.hospital.core.dto.account.UserDetailsForAdminResponse;
import com.hospital.core.entities.account.Accountant;
import com.hospital.core.entities.account.Dentist;
import com.hospital.core.entities.account.Specialize;
import com.hospital.core.entities.account.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountMapper {
    public Accountant toAccountant(AccountantDentistCreateRequest requestDto, User user){
        return Accountant.builder()
                .user(user)
                .email(requestDto.email())
                .phoneNumber(requestDto.phoneNumber())
                .build();
    };
    public Dentist toDentist(AccountantDentistCreateRequest requestDto, User user, Specialize specialize){
        return Dentist.builder()
                .user(user)
                .phoneNumber(requestDto.phoneNumber())
                .email(requestDto.email())
                .description(requestDto.description())
                .specialize(specialize)
                .build();
    }
    public UserDetailsForAdminResponse toUserDetailsForAdminResponse(User user){
        return UserDetailsForAdminResponse.builder()
                .accountant(user.getAccountant())
                .id(user.getId())
                .dentist(user.getDentist())
                .role(user.getRole())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phone(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }
    public CheckUserExistResponse toCheckUserExistResponse(User user){
        return CheckUserExistResponse.builder()
                .isExist(user != null)
                .role(user != null ? user.getRole() : null)
                .build();
    }
}
