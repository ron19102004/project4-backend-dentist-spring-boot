package com.hospital.app.services.impls;

import com.hospital.app.dto.service.ServiceCreateRequest;
import com.hospital.app.entities.service.Service;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.repositories.ServiceRepository;
import com.hospital.app.services.ServiceService;
import com.hospital.app.utils.Slugify;
import com.hospital.app.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Service create(ServiceCreateRequest serviceCreateRequest) {
        return this.serviceRepository.save(Service.builder()
                .name(serviceCreateRequest.name())
                .slug(Slugify.toSlug(serviceCreateRequest.name()))
                .description(serviceCreateRequest.description())
                .price(serviceCreateRequest.price())
                .build());
    }

    @Override
    public void update(Long id, Object o) {

    }
    @Transactional
    @Override
    public void delete(Long id) {
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
    public Service getById(Long id) {
        return this.serviceRepository.findByIdAndDeletedAtIsNull(id);
    }
}
