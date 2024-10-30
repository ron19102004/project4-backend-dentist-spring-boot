package com.hospital.app.services.impls;

import com.hospital.app.dto.specialize.SpecializeCreateUpdateRequest;
import com.hospital.app.entities.account.Specialize;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.mappers.SpecializeMapper;
import com.hospital.app.repositories.SpecializeRepository;
import com.hospital.app.services.SpecializeService;
import com.hospital.app.utils.HtmlZipUtil;
import com.hospital.app.utils.Slugify;
import com.hospital.app.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.List;

@Service
public class SpecializeServiceImpl implements SpecializeService {
    @Autowired
    private SpecializeRepository specializeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SpecializeMapper> getAll() {
        List<Specialize> specializes = this.specializeRepository.findAllByDeletedAtIsNull();
        return specializes.stream().map(specialize -> {
            try {
                return SpecializeMapper.mapper(specialize);
            } catch (IOException e) {
                throw ServiceException.builder()
                        .clazz(SpecializeServiceImpl.class)
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        }).toList();
    }

    @Override
    public SpecializeMapper getById(final Long id) {
        try {
            return SpecializeMapper.mapper(this.getByIdNormal(id));
        } catch (IOException e) {
            throw ServiceException.builder()
                    .clazz(SpecializeServiceImpl.class)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public Specialize getByIdNormal(Long id) {
        return this.specializeRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Specialize create(final SpecializeCreateUpdateRequest specializeCreateUpdateRequest) {
        try {
            return this.specializeRepository.save(Specialize.builder()
                    .name(specializeCreateUpdateRequest.name())
                    .slug(Slugify.toSlug(specializeCreateUpdateRequest.name()))
                    .description(HtmlZipUtil.compress(specializeCreateUpdateRequest.description()))
                    .build());
        } catch (IOException e) {
            throw ServiceException.builder()
                    .clazz(SpecializeServiceImpl.class)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Transactional
    @Override
    public void update(final Long id, final SpecializeCreateUpdateRequest specializeCreateUpdateRequest) {
        try {
            Specialize specialize = this.getByIdNormal(id);
            if (specialize == null) {
                throw ServiceException.builder()
                        .clazz(SpecializeServiceImpl.class)
                        .status(HttpStatus.NOT_FOUND)
                        .message("Chuyên ngành không xác định")
                        .build();
            }
            specialize.setName(specializeCreateUpdateRequest.name());
            specialize.setSlug(Slugify.toSlug(specializeCreateUpdateRequest.name()));
            specialize.setDescription(HtmlZipUtil.compress(specializeCreateUpdateRequest.description()));
            this.entityManager.merge(specialize);
        } catch (IOException e) {
            throw ServiceException.builder()
                    .clazz(SpecializeServiceImpl.class)
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
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
        this.entityManager.merge(specialize);
    }
}
