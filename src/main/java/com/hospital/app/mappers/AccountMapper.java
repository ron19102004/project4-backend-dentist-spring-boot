package com.hospital.app.mappers;

import com.hospital.app.dto.account.AccountantDentistCreateRequest;
import com.hospital.app.entities.account.Accountant;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.Specialize;
import com.hospital.app.entities.account.User;
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
}
