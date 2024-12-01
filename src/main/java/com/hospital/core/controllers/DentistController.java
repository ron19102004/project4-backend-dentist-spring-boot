package com.hospital.core.controllers;

import com.hospital.core.dto.dentist.DentistResponse;
import com.hospital.core.entities.EntityLayout;
import com.hospital.core.entities.account.Dentist;
import com.hospital.core.mappers.DentistMapper;
import com.hospital.core.services.DentistService;
import com.hospital.infrastructure.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dentists/v1")
public class DentistController {
    private final DentistService dentistService;

    @Autowired
    public DentistController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseLayout<List<DentistResponse>>> getAllDentists() {
        return ResponseEntity.ok(
                ResponseLayout.<List<DentistResponse>>builder()
                        .data(dentistService.findAll())
                        .success(true)
                        .message("OK")
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseLayout<DentistResponse>> getDentist(@PathVariable("id") Long id) {
        DentistResponse dentistResponse = DentistMapper.toDentistResponse(dentistService.findById(id));
        return ResponseEntity.ok(
                ResponseLayout.<DentistResponse>builder()
                        .data(dentistResponse)
                        .success(dentistResponse != null)
                        .message(dentistResponse == null ? "Không tìm thấy" : "Đã tìm thấy")
                        .build());
    }
}
