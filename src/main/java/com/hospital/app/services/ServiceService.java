package com.hospital.app.services;

import com.hospital.app.dto.service.ServiceCreateRequest;
import com.hospital.app.entities.service.Service;
import com.hospital.app.services.def.IReaderService;
import com.hospital.app.services.def.IWriteService;

public interface ServiceService extends
        IReaderService<Service, Long>,
        IWriteService<Service, ServiceCreateRequest, Object, Long> {
}
