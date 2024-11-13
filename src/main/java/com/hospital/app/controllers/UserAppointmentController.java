package com.hospital.app.controllers;

import com.hospital.app.annotations.HasRole;
import com.hospital.app.annotations.WithRateLimitIPAddress;
import com.hospital.app.annotations.WithRateLimitRequest;
import com.hospital.app.dto.appointment.AppointmentDTO;
import com.hospital.app.dto.appointment.BookingAppointmentRequest;
import com.hospital.app.entities.account.User;
import com.hospital.app.services.UserAppointmentService;
import com.hospital.app.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/appointments/v1")
public class UserAppointmentController {
    @Autowired
    private UserAppointmentService userAppointmentService;

    @PostMapping("/booking")
    @HasRole(justCheckAuthentication = true)
    @WithRateLimitIPAddress(limit = 5)
    @WithRateLimitRequest(duration = 3000)
    public ResponseEntity<ResponseLayout<Object>> booking(@AuthenticationPrincipal User user,
                                                          @RequestBody BookingAppointmentRequest bookingAppointmentRequest) {
        userAppointmentService.booking(user.getId(), bookingAppointmentRequest);
        return ResponseEntity.ok(ResponseLayout.builder()
                .message("Đặt hẹn thành công")
                .success(true)
                .build());
    }

    @PostMapping("/{appointmentId}/details")
    @HasRole(justCheckAuthentication = true)
    @WithRateLimitIPAddress(limit = 10, duration = 30000)
    public ResponseEntity<ResponseLayout<AppointmentDTO>> getDetails(@AuthenticationPrincipal User user,
                                                                      @PathVariable("appointmentId") Long appointmentId) {
        return ResponseEntity.ok(ResponseLayout.<AppointmentDTO>builder()
                .message("Lấy thông tin thành công")
                .success(true)
                .data(userAppointmentService.getDetailsUserAppointment(user.getId(), appointmentId))
                .build());
    }
}
