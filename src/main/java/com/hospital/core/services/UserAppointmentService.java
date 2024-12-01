package com.hospital.core.services;

import com.hospital.core.dto.appointment.AppointmentDTO;
import com.hospital.core.dto.appointment.BookingAppointmentRequest;

import java.util.List;

public interface UserAppointmentService {
    Long booking(Long userId, BookingAppointmentRequest bookingAppointmentRequest);

    AppointmentDTO getDetailsUserAppointment(Long userId, Long appointmentId);
    void addReward(Long userId, Long appointmentId, Long rewardHistoryId);
    List<AppointmentDTO> getAllMyAppointment(Long userId);

}
