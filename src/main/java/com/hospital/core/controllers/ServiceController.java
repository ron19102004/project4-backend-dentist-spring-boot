package com.hospital.core.controllers;

import com.hospital.core.annotations.HasRole;
import com.hospital.core.annotations.WithRateLimitIPAddress;
import com.hospital.core.dto.service.HotServiceResponse;
import com.hospital.core.dto.service.ServiceCreateRequest;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.service.Service;
import com.hospital.core.services.ServiceService;
import com.hospital.infrastructure.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/v1")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @GetMapping("/hot")
    @WithRateLimitIPAddress(duration = 15000,limit = 5)
    public ResponseEntity<ResponseLayout<List<HotServiceResponse>>> getHotServices() {
        return ResponseEntity.ok(ResponseLayout
                .<List<HotServiceResponse>>builder()
                .data(this.serviceService.hotServices())
                .success(true)
                .message("Lấy thông tin dịch vụ thành công")
                .build());
    }
    @GetMapping("/all")
    @WithRateLimitIPAddress(duration = 15000,limit = 5)
    public ResponseEntity<ResponseLayout<List<Service>>> getAllServices() {
        return ResponseEntity.ok(ResponseLayout
                .<List<Service>>builder()
                .data(this.serviceService.getAll())
                .success(true)
                .message("Lấy thông tin dịch vụ thành công")
                .build());
    }

    @GetMapping("/{id}")
    @WithRateLimitIPAddress(duration = 15000,limit = 5)
    public ResponseEntity<ResponseLayout<Service>> getServiceById(@PathVariable("id") Long id) {
        Service service = this.serviceService.getById(id);
        return ResponseEntity.ok(ResponseLayout
                .<Service>builder()
                .message(service != null ? "Lấy thông tin dịch vụ thành công" : "Không tìm thấy dịch vụ")
                .success(service != null)
                .data(service)
                .build());
    }

    @HasRole(roles = {Role.ACCOUNTANT})
    @PostMapping("/new")
    public ResponseEntity<ResponseLayout<Service>> createService(@RequestBody ServiceCreateRequest serviceCreateRequest) {
        return ResponseEntity.ok(ResponseLayout
                .<Service>builder()
                .data(this.serviceService.create(serviceCreateRequest))
                .success(true)
                .message("Thêm dịch vụ thành công")
                .build());
    }

    @HasRole(roles = {Role.ACCOUNTANT})
    @DeleteMapping("/{id}")
    @WithRateLimitIPAddress(duration = 10000,limit = 5)
    public ResponseEntity<ResponseLayout<Service>> deleteService(@PathVariable("id") Long id) {
        this.serviceService.delete(id);
        return ResponseEntity.ok(ResponseLayout
                .<Service>builder()
                .message("Xóa dịch vụ thành công")
                .success(true)
                .build());
    }
}
