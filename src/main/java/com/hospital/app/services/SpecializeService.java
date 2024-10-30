package com.hospital.app.services;

import com.hospital.app.dto.specialize.SpecializeCreateUpdateRequest;
import com.hospital.app.entities.account.Specialize;
import com.hospital.app.mappers.SpecializeMapper;
import com.hospital.app.services.def.IReaderService;
import com.hospital.app.services.def.IWriteService;

public interface SpecializeService extends
        IReaderService<SpecializeMapper, Long>,
        IWriteService<Specialize, SpecializeCreateUpdateRequest, SpecializeCreateUpdateRequest, Long> {
    Specialize getByIdNormal(Long id);
}
