package com.hospital.app.services;

import com.hospital.app.dto.appointment.AppointmentDTO;
import com.hospital.app.dto.appointment.BookingAppointmentRequest;

public interface UserAppointmentService {
    void booking(Long userId, BookingAppointmentRequest bookingAppointmentRequest);

    AppointmentDTO getDetailsUserAppointment(Long userId, Long appointmentId);
}
