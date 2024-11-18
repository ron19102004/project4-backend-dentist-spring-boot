package com.hospital.core.services.impls;

import com.hospital.core.dto.service.HotServiceResponse;
import com.hospital.core.dto.service.ServiceCreateRequest;
import com.hospital.core.entities.service.Service;
import com.hospital.core.events.UpdateListServiceEvent;
import com.hospital.exception.ServiceException;
import com.hospital.core.mappers.ServiceMapper;
import com.hospital.core.repositories.InvoiceServiceRepository;
import com.hospital.core.repositories.ServiceRepository;
import com.hospital.core.services.ServiceService;
import com.hospital.infrastructure.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final InvoiceServiceRepository invoiceServiceRepository;
    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository,
                              InvoiceServiceRepository invoiceServiceRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.serviceRepository = serviceRepository;
        this.invoiceServiceRepository = invoiceServiceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Service create(final ServiceCreateRequest serviceCreateRequest) {
        Service service =this.serviceRepository.save(ServiceMapper.toService(serviceCreateRequest));
        List<Service> services = getAll();
        eventPublisher.publishEvent(new UpdateListServiceEvent(this,services));
        return service;
    }

    @Override
    public void update(final Long id, final Object o) {
        List<Service> services = getAll();
        eventPublisher.publishEvent(new UpdateListServiceEvent(this,services));
    }

    @Transactional
    @Override
    public void delete(final Long id) {
        Service service = this.getById(id);
        if (service == null) {
            throw ServiceException.builder()
                    .clazz(ServiceServiceImpl.class)
                    .message("Không tìm thấy dịch vụ")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        service.setDeletedAt(VietNamTime.dateNow());
        this.entityManager.merge(service);
        List<Service> services = getAll();
        eventPublisher.publishEvent(new UpdateListServiceEvent(this,services));
    }

    @Override
    public List<Service> getAll() {
        return this.serviceRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public Service getById(final Long id) {
        return this.serviceRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public List<HotServiceResponse> hotServices() {
        Pageable pageable = PageRequest.of(0, 4);
        return invoiceServiceRepository.getHotServices(pageable).toList();
    }
}
