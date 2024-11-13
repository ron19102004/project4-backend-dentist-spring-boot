package com.hospital.app.services;

import com.hospital.app.dto.user_appointment.BookingAppointmentRequest;

public interface UserAppointmentService {
    void booking(Long userId,BookingAppointmentRequest bookingAppointmentRequest);
}
