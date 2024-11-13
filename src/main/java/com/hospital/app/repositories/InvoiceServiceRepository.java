package com.hospital.app.repositories;

import com.hospital.app.dto.service.HotServiceResponse;
import com.hospital.app.entities.invoice.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceServiceRepository extends JpaRepository<InvoiceService, Long> {
    @Query(value =
            "SELECT new com.hospital.app.dto.service." +
            "HotServiceResponse(s.description,s.poster,s.name,s.slug,s.price,s.pointReward, COUNT(isv)) " +
            "FROM InvoiceService isv " +
            "LEFT JOIN isv.service as s " +
            "GROUP BY s.id HAVING COUNT(isv) > 1 " +
            "ORDER BY COUNT(isv) DESC")
    Page<HotServiceResponse> getHotServices(Pageable pageable);
}
