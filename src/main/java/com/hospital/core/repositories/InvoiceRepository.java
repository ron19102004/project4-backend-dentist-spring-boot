package com.hospital.core.repositories;

import com.hospital.core.entities.invoice.Invoice;
import com.hospital.core.entities.invoice.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    Page<Invoice> findAllByStatus(InvoiceStatus status, Pageable pageable);

}
