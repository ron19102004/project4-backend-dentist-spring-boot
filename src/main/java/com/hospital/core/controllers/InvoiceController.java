package com.hospital.core.controllers;


import com.hospital.core.annotations.HasRole;
import com.hospital.core.dto.invoice.InvoicesResponse;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.invoice.InvoiceStatus;
import com.hospital.core.services.InvoiceService;
import com.hospital.infrastructure.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices/v1")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/all")
    @HasRole(roles = {Role.ACCOUNTANT})
    public ResponseEntity<ResponseLayout<InvoicesResponse>> getInvoices(
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam(value = "appointmentId", required = false, defaultValue = "0") long appointmentId,
            @RequestParam("status") InvoiceStatus status) {
        InvoicesResponse invoicesResponse;
        if (appointmentId == 0) {
            invoicesResponse = invoiceService.getByPageNumberAndStatus(pageNumber, status);
        } else {
            invoicesResponse = invoiceService.getByAppointmentIdAndPageNumberAndStatus(appointmentId, pageNumber, status);
        }
        return ResponseEntity.ok(ResponseLayout.<InvoicesResponse>builder()
                .data(invoicesResponse)
                .success(true)
                .message("Lấy dữ liệu thành công")
                .build());
    }

}
