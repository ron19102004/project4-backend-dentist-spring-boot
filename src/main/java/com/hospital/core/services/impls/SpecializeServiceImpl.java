package com.hospital.core.services.impls;

import com.hospital.core.dto.specialize.SpecializeCreateUpdateRequest;
import com.hospital.core.dto.specialize.SpecializeResponse;
import com.hospital.core.entities.account.Specialize;
import com.hospital.core.events.UpdateListSpecializeEvent;
import com.hospital.exception.ServiceException;
import com.hospital.core.mappers.SpecializeMapper;
import com.hospital.core.repositories.SpecializeRepository;
import com.hospital.core.services.SpecializeService;
import com.hospital.infrastructure.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializeServiceImpl implements SpecializeService {
    private final SpecializeRepository specializeRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    public SpecializeServiceImpl(SpecializeRepository specializeRepository,
                                 ApplicationEventPublisher eventPublisher) {
        this.specializeRepository = specializeRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<SpecializeResponse> getAll() {
        List<Specialize> specializes = this.specializeRepository.findAllByDeletedAtIsNull();
        return specializes.stream().map(SpecializeMapper::toSpecializeResponse).toList();
    }

    @Override
    public SpecializeResponse getById(final Long id) {
        return SpecializeMapper.toSpecializeResponse(this.getByIdNormal(id));
    }

    @Override
    public Specialize getByIdNormal(Long id) {
        return this.specializeRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Specialize create(final SpecializeCreateUpdateRequest specializeCreateUpdateRequest) {
        Specialize specialize = this.specializeRepository.save(SpecializeMapper.toSpecialize(specializeCreateUpdateRequest));
        List<SpecializeResponse> specializeResponses = getAll();
        eventPublisher.publishEvent(new UpdateListSpecializeEvent(this,specializeResponses));
        return specialize ;
    }

    @Transactional
    @Override
    public void update(final Long id, final SpecializeCreateUpdateRequest specializeCreateUpdateRequest) {
        Specialize specialize = this.getByIdNormal(id);
        if (specialize == null) {
            throw ServiceException.builder()
                    .clazz(SpecializeServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .message("Chuyên ngành không xác định")
                    .build();
        }
        Specialize specializeMapper = SpecializeMapper.toSpecialize(specializeCreateUpdateRequest);
        specialize.setName(specializeMapper.getName());
        specialize.setSlug(specializeMapper.getSlug());
        specialize.setDescription(specializeMapper.getDescription());
        List<SpecializeResponse> specializeResponses = getAll();
        eventPublisher.publishEvent(new UpdateListSpecializeEvent(this,specializeResponses));
    }

    @Transactional
    @Override
    public void delete(final Long id) {
        Specialize specialize = this.getByIdNormal(id);
        if (specialize == null) {
            throw ServiceException.builder()
                    .clazz(SpecializeServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .message("Chuyên ngành không xác định")
                    .build();
        }
        specialize.setDeletedAt(VietNamTime.dateNow());
        List<SpecializeResponse> specializeResponses = getAll();
        eventPublisher.publishEvent(new UpdateListSpecializeEvent(this,specializeResponses));
    }
}
