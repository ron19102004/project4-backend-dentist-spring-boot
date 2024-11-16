package com.hospital.core.controllers;

import com.hospital.core.annotations.HasRole;
import com.hospital.core.annotations.WithRateLimitIPAddress;
import com.hospital.core.dto.specialize.SpecializeCreateUpdateRequest;
import com.hospital.core.dto.specialize.SpecializeResponse;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.account.Specialize;
import com.hospital.core.services.SpecializeService;
import com.hospital.infrastructure.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializes/v1")
public class SpecializeController {
    @Autowired
    private SpecializeService specializeService;

    @GetMapping("/all")
    @WithRateLimitIPAddress(limit = 5,duration = 15000)
    public ResponseEntity<ResponseLayout<List<SpecializeResponse>>> getAllSpecializes() {
        return ResponseEntity.ok(ResponseLayout
                .<List<SpecializeResponse>>builder()
                .data(this.specializeService.getAll())
                .success(true)
                .message("Lấy tất cả chuyên nghành thành công")
                .build());
    }

    @GetMapping("/{id}")
    @WithRateLimitIPAddress(duration = 15000,limit = 5)
    public ResponseEntity<ResponseLayout<SpecializeResponse>> getSpecializeById(@PathVariable("id") Long id) {
        SpecializeResponse specialize = this.specializeService.getById(id);
        return ResponseEntity.ok(ResponseLayout
                .<SpecializeResponse>builder()
                .message(specialize != null ? "Lấy thông tin thành công" : "Không tìm thấy chuyên nghành")
                .success(specialize != null)
                .data(specialize)
                .build());
    }

    @HasRole(roles = {Role.ADMIN})
    @PostMapping("/new")
    public ResponseEntity<ResponseLayout<Specialize>> addSpecialize(@RequestBody SpecializeCreateUpdateRequest specializeCreateUpdateRequest) {
        return ResponseEntity.ok(ResponseLayout
                .<Specialize>builder()
                .data(this.specializeService.create(specializeCreateUpdateRequest))
                .success(true)
                .message("Thêm chuyên ngành thành công")
                .build());
    }

    @HasRole(roles = {Role.ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<ResponseLayout<Object>> updateSpecialize(
            @PathVariable Long id,
            @RequestBody SpecializeCreateUpdateRequest specializeCreateUpdateRequest) {
        this.specializeService.update(id, specializeCreateUpdateRequest);
        return ResponseEntity.ok(ResponseLayout.builder()
                .message("Chỉnh sửa chuyên ngành thành công")
                .success(true)
                .build());
    }

    @HasRole(roles = {Role.ADMIN})
    @DeleteMapping("/{id}")
    @WithRateLimitIPAddress(duration = 10000,limit = 5)
    public ResponseEntity<ResponseLayout<Object>> deleteSpecialize(@PathVariable("id") Long id) {
        this.specializeService.delete(id);
        return ResponseEntity.ok(ResponseLayout.builder()
                .message("Xóa chuyên ngành thành công")
                .success(true)
                .build());
    }
}
