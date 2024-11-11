package com.hospital.app.controllers;

import com.hospital.app.annotations.HasRole;
import com.hospital.app.dto.service.ServiceCreateRequest;
import com.hospital.app.entities.account.Role;
import com.hospital.app.entities.service.Service;
import com.hospital.app.services.ServiceService;
import com.hospital.app.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/v1")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @GetMapping("/all")
    public ResponseEntity<ResponseLayout<List<Service>>> getAllServices() {
        System.out.println(serviceService);
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
    public ResponseEntity<ResponseLayout<Service>> deleteService(@PathVariable("id") Long id) {
        this.serviceService.delete(id);
        return ResponseEntity.ok(ResponseLayout
                .<Service>builder()
                .message("Xóa dịch vụ thành công")
                .success(true)
                .build());
    }
}
