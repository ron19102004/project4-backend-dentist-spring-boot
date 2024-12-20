package com.hospital.core.repositories;

import com.hospital.core.dto.service.HotServiceResponse;
import com.hospital.core.entities.invoice.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceServiceRepository extends JpaRepository<InvoiceService, Long> {
    @Query(value =
            "SELECT new com.hospital.core.dto.service." +
            "HotServiceResponse(s.id,s.description,s.poster,s.name,s.slug,s.price,s.pointReward, COUNT(isv)) " +
            "FROM InvoiceService isv " +
            "LEFT JOIN isv.service as s " +
            "GROUP BY s.id HAVING COUNT(isv) > 1 " +
            "ORDER BY COUNT(isv) DESC")
    Page<HotServiceResponse> getHotServices(Pageable pageable);
}
