package com.hospital.core.controllers;

import com.hospital.core.annotations.HasRole;
import com.hospital.core.annotations.WithRateLimitIPAddress;
import com.hospital.core.annotations.WithRateLimitRequest;
import com.hospital.core.dto.appointment.AppointmentDTO;
import com.hospital.core.dto.appointment.BookingAppointmentRequest;
import com.hospital.core.entities.account.User;
import com.hospital.core.entities.work.Appointment;
import com.hospital.core.services.UserAppointmentService;
import com.hospital.infrastructure.utils.ResponseLayout;
import com.hospital.kafka.events.BookingKafkaEvent;
import com.hospital.kafka.producers.BookingKafkaEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/appointments/v1")
public class UserAppointmentController {
    @Autowired
    private UserAppointmentService userAppointmentService;
    @Autowired
    private BookingKafkaEventProducer bookingKafkaEventProducer;

    @PostMapping("/booking")
    @HasRole(justCheckAuthentication = true)
    @WithRateLimitIPAddress(limit = 5)
    @WithRateLimitRequest(duration = 3000)
    public ResponseEntity<ResponseLayout<Object>> booking(@AuthenticationPrincipal User user,
                                                          @RequestBody BookingAppointmentRequest bookingAppointmentRequest) {
        BookingKafkaEvent bookingKafkaEvent = userAppointmentService.booking(user.getId(), bookingAppointmentRequest);
        bookingKafkaEventProducer.pushBookingKafkaEvent(bookingKafkaEvent);
        return ResponseEntity.ok(ResponseLayout.builder()
                .message("Đặt hẹn thành công")
                .success(true)
                .data(bookingKafkaEvent.getAppointmentId())
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

    @PostMapping("/invoice/add-reward/{appointmentId}/{rewardHistoryId}")
    @HasRole(justCheckAuthentication = true)
    @WithRateLimitIPAddress(limit = 5)
    @WithRateLimitRequest(duration = 3000)
    public ResponseEntity<ResponseLayout<Object>> addReward(
            @AuthenticationPrincipal User user,
            @PathVariable("appointmentId") Long appointmentId,
            @PathVariable("rewardHistoryId") Long rewardHistoryId
    ) {
        userAppointmentService.addReward(user.getId(), appointmentId, rewardHistoryId);
        return ResponseEntity.ok(ResponseLayout.<Object>builder()
                .message("Thêm mã đổi quà thành công")
                .success(true)
                .build());
    }
}
