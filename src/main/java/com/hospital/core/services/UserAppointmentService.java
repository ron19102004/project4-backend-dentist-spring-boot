package com.hospital.core.services;

import com.hospital.core.dto.appointment.AppointmentDTO;
import com.hospital.core.dto.appointment.BookingAppointmentRequest;
import com.hospital.core.entities.work.Appointment;
import com.hospital.kafka.events.BookingKafkaEvent;

public interface UserAppointmentService {
    Long booking(Long userId, BookingAppointmentRequest bookingAppointmentRequest);

    AppointmentDTO getDetailsUserAppointment(Long userId, Long appointmentId);
    void addReward(Long userId, Long appointmentId, Long rewardHistoryId);
}
