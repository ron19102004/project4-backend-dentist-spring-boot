package com.hospital.app.controllers;

import com.hospital.app.dto.user_appointment.BookingAppointmentRequest;
import com.hospital.app.services.UserAppointmentService;
import com.hospital.app.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/appointment/v1")
public class UserAppointmentController {
    @Autowired
    private UserAppointmentService userAppointmentService;
    @PostMapping("/booking")
    public ResponseEntity<ResponseLayout<Object>> booking(@RequestBody BookingAppointmentRequest bookingAppointmentRequest) {

        return ResponseEntity.ok(ResponseLayout.builder()
                .message("Đặt hẹn thành công")
                .success(true)
                .build());
    }
}
