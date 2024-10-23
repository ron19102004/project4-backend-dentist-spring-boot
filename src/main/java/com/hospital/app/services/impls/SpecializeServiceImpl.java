package com.hospital.app.services.impls;

import com.hospital.app.dto.specialize.SpecializeCreateUpdateRequest;
import com.hospital.app.entities.account.Specialize;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.repositories.SpecializeRepository;
import com.hospital.app.services.SpecializeService;
import com.hospital.app.utils.Slugify;
import com.hospital.app.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class SpecializeServiceImpl implements SpecializeService {
    @Autowired
    private SpecializeRepository specializeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Specialize> getAll() {
        return this.specializeRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public Specialize getById(Long id) {
        return this.specializeRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Specialize create(SpecializeCreateUpdateRequest specializeCreateUpdateRequest) {
        return this.specializeRepository.save(Specialize.builder()
                .name(specializeCreateUpdateRequest.name())
                .slug(Slugify.toSlug(specializeCreateUpdateRequest.name()))
                .build());
    }

    @Transactional
    @Override
    public void update(Long id, SpecializeCreateUpdateRequest specializeCreateUpdateRequest) {
        Specialize specialize = this.getById(id);
        if (specialize == null) {
            throw ServiceException.builder()
                    .clazz(SpecializeServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .message("Chuyên ngành không xác định")
                    .build();
        }
        specialize.setName(specializeCreateUpdateRequest.name());
        specialize.setSlug(Slugify.toSlug(specializeCreateUpdateRequest.name()));
        this.entityManager.merge(specialize);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Specialize specialize = this.getById(id);
        if (specialize == null) {
            throw ServiceException.builder()
                    .clazz(SpecializeServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .message("Chuyên ngành không xác định")
                    .build();
        }
        specialize.setDeletedAt(VietNamTime.dateNow());
        this.entityManager.merge(specialize);
    }
}
