package com.hospital.core.services.impls;

import com.hospital.core.dto.invoice.InvoiceDetailsResponse;
import com.hospital.core.dto.invoice.InvoicesResponse;
import com.hospital.core.entities.invoice.Invoice;
import com.hospital.core.entities.invoice.InvoiceStatus;
import com.hospital.core.mappers.AppointmentMapper;
import com.hospital.core.repositories.InvoiceRepository;
import com.hospital.core.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public InvoicesResponse getByAppointmentIdAndPageNumberAndStatus(long appointmentId, int pageNumber, InvoiceStatus status) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        Page<Invoice> invoices = invoiceRepository.findAllByIdAndStatus(appointmentId, status, pageable);
        return InvoicesResponse.builder()
                .pageNumber(pageNumber)
                .status(status)
                .invoices(invoices
                        .toList()
                        .stream()
                        .map(AppointmentMapper::toInvoiceDetailsResponse)
                        .toList())
                .build();
    }

    @Override
    public InvoicesResponse getByPageNumberAndStatus(int pageNumber, InvoiceStatus status) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        Page<Invoice> invoices = invoiceRepository.findAllByStatus(status, pageable);
        return InvoicesResponse.builder()
                .pageNumber(pageNumber)
                .status(status)
                .invoices(invoices
                        .toList()
                        .stream()
                        .map(AppointmentMapper::toInvoiceDetailsResponse)
                        .toList())
                .build();
    }
}
