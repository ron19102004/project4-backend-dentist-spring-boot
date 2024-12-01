package com.hospital.core.controllers;

import com.hospital.core.annotations.HasRole;
import com.hospital.core.annotations.WithRateLimitIPAddress;
import com.hospital.core.annotations.WithRateLimitRequest;
import com.hospital.core.dto.appointment.AppointmentByDentistIdInDateResponse;
import com.hospital.core.dto.appointment.AppointmentDTO;
import com.hospital.core.dto.appointment.AppointmentsByDentistRequest;
import com.hospital.core.dto.appointment.QuantityAppointmentDentistDTO;
import com.hospital.core.dto.dental_record.DentalRecordUpdate;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.account.User;
import com.hospital.core.entities.work.AppointmentStatus;
import com.hospital.core.services.DentistAppointmentService;
import com.hospital.infrastructure.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dentists/appointments/v1")
public class DentistAppointmentController {

    private final DentistAppointmentService dentistAppointmentService;

    @Autowired
    public DentistAppointmentController(DentistAppointmentService dentistAppointmentService) {
        this.dentistAppointmentService = dentistAppointmentService;
    }

    @GetMapping("/{dentistId}/in-7-days-later")
    @WithRateLimitIPAddress(limit = 5, duration = 5000)
    @WithRateLimitRequest(limit = 100, duration = 5000)
    public ResponseEntity<ResponseLayout<List<QuantityAppointmentDentistDTO>>> booking(
            @PathVariable("dentistId") Long dentistId) {
        List<QuantityAppointmentDentistDTO> quantityAppointmentDentistDTOS =
                dentistAppointmentService.countAppointmentDentistInSevenDaysLater(dentistId);
        return ResponseEntity.ok(ResponseLayout.<List<QuantityAppointmentDentistDTO>>builder()
                .message("Lấy dữ liệu thành công")
                .success(true)
                .data(quantityAppointmentDentistDTOS)
                .build());
    }
    @PostMapping("/change-status-appointment/{appointmentId}/{status}")
    @HasRole(roles = {Role.DENTIST})
    public ResponseEntity<ResponseLayout<Object>> changeStatusAppointment(
            @AuthenticationPrincipal User user,
            @PathVariable("appointmentId") Long appointmentId,
            @PathVariable("status") AppointmentStatus status) {
        switch (status){
            case CANCELLED -> dentistAppointmentService.cancelAppointment(user.getId(),appointmentId);
            case COMPLETED -> dentistAppointmentService.finishAppointment(user.getId(),appointmentId);
        }
        return ResponseEntity.ok(ResponseLayout.builder()
                .message("OK")
                .success(true)
                .build());
    }
    @PostMapping("/all")
    @HasRole(roles = {Role.DENTIST})
    public ResponseEntity<ResponseLayout<AppointmentByDentistIdInDateResponse>> getAppointments(
            @AuthenticationPrincipal User user,
            @RequestBody AppointmentsByDentistRequest request
    ) {
        return ResponseEntity.ok(ResponseLayout.<AppointmentByDentistIdInDateResponse>builder()
                .message("Lấy dữ liệu thành công")
                .success(true)
                .data(dentistAppointmentService.getAppointmentByDentistIdInDate(user.getId(), request))
                .build());
    }
    @GetMapping("/booking/{appointmentId}")
    @HasRole(roles = {Role.DENTIST,Role.PATIENT})
    @WithRateLimitIPAddress(limit = 20, duration = 3000)
    public ResponseEntity<ResponseLayout<AppointmentDTO>> getAppointment(
            @AuthenticationPrincipal User user,
            @PathVariable("appointmentId") Long appointmentId
    ) {
        return ResponseEntity.ok(ResponseLayout.<AppointmentDTO>builder()
                .message("Lấy dữ liệu thành công")
                .success(true)
                .data(dentistAppointmentService.getAppointmentById(user.getId(),appointmentId))
                .build());
    }


    @PatchMapping("/{appointmentId}/edit")
    @HasRole(roles = {Role.DENTIST})
    @WithRateLimitIPAddress(limit = 20, duration = 3000)
    public ResponseEntity<ResponseLayout<Object>> editDentalRecord(
            @AuthenticationPrincipal User user,
            @RequestBody DentalRecordUpdate request,
            @PathVariable("appointmentId") Long appointmentId
    ) {
        dentistAppointmentService.updateDentalRecord(user.getId(), appointmentId, request);
        return ResponseEntity.ok(ResponseLayout.builder()
                .message("Cập nhật thành công")
                .success(true)
                .build());
    }
}
