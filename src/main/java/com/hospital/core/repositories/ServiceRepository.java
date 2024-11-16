package com.hospital.core.repositories;

import com.hospital.core.entities.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findAllByDeletedAtIsNull();
    Service findByIdAndDeletedAtIsNull(Long id);
    List<Service> findAllByIdIn(List<Long> ids);
}
