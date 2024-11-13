package com.hospital.app.services.impls;

import com.hospital.app.dto.service.HotServiceResponse;
import com.hospital.app.dto.service.ServiceCreateRequest;
import com.hospital.app.entities.service.Service;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.mappers.ServiceMapper;
import com.hospital.app.repositories.InvoiceServiceRepository;
import com.hospital.app.repositories.ServiceRepository;
import com.hospital.app.services.ServiceService;
import com.hospital.app.utils.Slugify;
import com.hospital.app.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private InvoiceServiceRepository invoiceServiceRepository;

    @Override
    public Service create(final ServiceCreateRequest serviceCreateRequest) {
        return this.serviceRepository.save(ServiceMapper.toService(serviceCreateRequest));
    }

    @Override
    public void update(final Long id, final Object o) {

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
