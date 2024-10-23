package com.hospital.app.controllers;

import com.hospital.app.dto.specialize.SpecializeCreateUpdateRequest;
import com.hospital.app.entities.account.Specialize;
import com.hospital.app.services.SpecializeService;
import com.hospital.app.utils.PreAuthUtil;
import com.hospital.app.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializes/v1")
public class SpecializeController {
    @Autowired
    private SpecializeService specializeService;

    @GetMapping("/all")
    public ResponseEntity<ResponseLayout<List<Specialize>>> getAllSpecializes() {
        return ResponseEntity.ok(ResponseLayout
                .<List<Specialize>>builder()
                .data(this.specializeService.getAll())
                .success(true)
                .message("Lấy tất cả chuyên nghành thành công")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseLayout<Specialize>> getSpecializeById(@PathVariable("id") Long id) {
        Specialize specialize = this.specializeService.getById(id);
        return ResponseEntity.ok(ResponseLayout
                .<Specialize>builder()
                .message(specialize != null ? "Lấy thông tin thành công" : "Không tìm thấy chuyên nghành")
                .success(specialize != null)
                .data(specialize)
                .build());
    }
    @PreAuthorize(PreAuthUtil.HAS_ADMIN_AUTHORITY)
    @PostMapping("/new")
    public ResponseEntity<ResponseLayout<Specialize>> addSpecialize(@RequestBody SpecializeCreateUpdateRequest specializeCreateUpdateRequest) {
        return ResponseEntity.ok(ResponseLayout
                .<Specialize>builder()
                .data(this.specializeService.create(specializeCreateUpdateRequest))
                .success(true)
                .message("Thêm chuyên ngành thành công")
                .build());
    }
    @PreAuthorize(PreAuthUtil.HAS_ADMIN_AUTHORITY)
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
    @PreAuthorize(PreAuthUtil.HAS_ADMIN_AUTHORITY)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseLayout<Object>> deleteSpecialize(@PathVariable("id") Long id) {
        this.specializeService.delete(id);
        return ResponseEntity.ok(ResponseLayout.builder()
                .message("Xóa chuyên ngành thành công")
                .success(true)
                .build());
    }
}
