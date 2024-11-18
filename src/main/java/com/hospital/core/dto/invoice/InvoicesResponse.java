package com.hospital.core.dto.invoice;

import com.hospital.core.entities.invoice.InvoiceStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class InvoicesResponse {
    private List<InvoiceDetailsResponse> invoices;
    private int pageNumber;
    private InvoiceStatus status;
}
