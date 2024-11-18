package com.hospital.core.services;

import com.hospital.core.dto.invoice.InvoicesResponse;
import com.hospital.core.entities.invoice.InvoiceStatus;

public interface InvoiceService {
    InvoicesResponse getAllInvoices(int pageNumber, InvoiceStatus status);
}
