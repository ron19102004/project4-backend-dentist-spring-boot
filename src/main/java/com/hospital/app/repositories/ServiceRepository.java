package com.hospital.app.repositories;

import com.hospital.app.entities.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findAllByDeletedAtIsNull();
    Service findByIdAndDeletedAtIsNull(Long id);
}
