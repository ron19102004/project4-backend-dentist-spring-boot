package com.hospital.core.services.def;

import com.hospital.core.dto.account.AccountantDentistCreateRequest;

public interface ICreateAdvanceAccount<Entity> {
    Entity createAdvanceAccount(final AccountantDentistCreateRequest requestDto);
    Entity findById(final Long id);
}
