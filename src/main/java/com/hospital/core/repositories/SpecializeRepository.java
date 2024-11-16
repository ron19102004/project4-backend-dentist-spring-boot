package com.hospital.core.repositories;

import com.hospital.core.entities.account.Specialize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecializeRepository extends JpaRepository<Specialize,Long> {
    List<Specialize> findAllByDeletedAtIsNull();
    Specialize findByIdAndDeletedAtIsNull(Long id);
    Specialize findBySlugAndDeletedAtIsNull(String slug);
}
