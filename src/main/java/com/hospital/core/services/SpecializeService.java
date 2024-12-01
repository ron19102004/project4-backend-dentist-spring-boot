package com.hospital.core.services;

import com.hospital.core.dto.specialize.SpecializeCreateUpdateRequest;
import com.hospital.core.dto.specialize.SpecializeResponse;
import com.hospital.core.entities.account.Specialize;
import com.hospital.core.services.def.IReaderService;
import com.hospital.core.services.def.IWriteService;

public interface SpecializeService extends
        IReaderService<SpecializeResponse, Long>,
        IWriteService<Specialize, SpecializeCreateUpdateRequest, SpecializeCreateUpdateRequest, Long> {
    Specialize getByIdNormal(Long id);
    SpecializeResponse getBySlug(String slug);
}
