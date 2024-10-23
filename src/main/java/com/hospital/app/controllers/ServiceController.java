package com.hospital.app.controllers;

import com.hospital.app.dto.service.ServiceCreateRequest;
import com.hospital.app.entities.service.Service;
import com.hospital.app.services.ServiceService;
import com.hospital.app.utils.PreAuthUtil;
import com.hospital.app.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/v1")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @GetMapping("/all")
    public ResponseEntity<ResponseLayout<List<Service>>> getAllServices() {
        return ResponseEntity.ok(ResponseLayout
                .<List<Service>>builder()
                .data(this.serviceService.getAll())
                .success(true)
                .message("Lấy thông tin dịch vụ thành công")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseLayout<Service>> getServiceById(@PathVariable("id") Long id) {
        Service service = this.serviceService.getById(id);
        return ResponseEntity.ok(ResponseLayout
                .<Service>builder()
                .message(service != null ? "Lấy thông tin dịch vụ thành công" : "Không tìm thấy dịch vụ")
                .success(service != null)
                .data(service)
                .build());
    }

    @PreAuthorize(PreAuthUtil.HAS_ACCOUNTANT_AUTHORITY)
    @PostMapping("/new")
    private ResponseEntity<ResponseLayout<Service>> createService(@RequestBody ServiceCreateRequest serviceCreateRequest) {
        return ResponseEntity.ok(ResponseLayout
                .<Service>builder()
                .data(this.serviceService.create(serviceCreateRequest))
                .success(true)
                .message("Thêm dịch vụ thành công")
                .build());
    }

    @PreAuthorize(PreAuthUtil.HAS_ACCOUNTANT_AUTHORITY)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseLayout<Service>> deleteService(@PathVariable("id") Long id) {
        this.serviceService.delete(id);
        return ResponseEntity.ok(ResponseLayout
                .<Service>builder()
                .message("Xóa dịch vụ thành công")
                .success(true)
                .build());
    }
}
