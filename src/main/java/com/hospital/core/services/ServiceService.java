package com.hospital.core.services;

import com.hospital.core.dto.service.HotServiceResponse;
import com.hospital.core.dto.service.ServiceCreateRequest;
import com.hospital.core.entities.service.Service;
import com.hospital.core.services.def.IReaderService;
import com.hospital.core.services.def.IWriteService;

import java.util.List;

public interface ServiceService extends
        IReaderService<Service, Long>,
        IWriteService<Service, ServiceCreateRequest, Object, Long> {
    List<HotServiceResponse> hotServices();
}
