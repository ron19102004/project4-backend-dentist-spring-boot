package com.hospital.app.services.def;

import com.hospital.app.dto.account.AccountantDentistCreateRequest;

public interface ICreateAdvanceAccount<Entity> {
    Entity createAdvanceAccount(AccountantDentistCreateRequest requestDto);
}
