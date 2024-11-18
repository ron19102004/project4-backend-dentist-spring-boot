package com.hospital.core.controllers;


import com.hospital.core.annotations.HasRole;
import com.hospital.core.dto.invoice.InvoicesResponse;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.invoice.InvoiceStatus;
import com.hospital.core.services.InvoiceService;
import com.hospital.infrastructure.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices/v1")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    @GetMapping("/all/{pageNumber}/{status}")
    @HasRole(roles = {Role.ACCOUNTANT})
    public ResponseEntity<ResponseLayout<InvoicesResponse>> getInvoices(@PathVariable("pageNumber") int pageNumber,
                                                                        @PathVariable("status") InvoiceStatus status) {
        return ResponseEntity.ok(ResponseLayout.<InvoicesResponse>builder()
                .data(invoiceService.getAllInvoices(pageNumber, status))
                .success(true)
                .message("Lấy dữ liệu thành công")
                .build());
    }
}
