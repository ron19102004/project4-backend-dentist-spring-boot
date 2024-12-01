package com.hospital.infrastructure.kafka.events;

import com.hospital.core.dto.appointment.BookingAppointmentRequest;
import com.hospital.core.entities.service.Service;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class BookingKafkaEvent {
    private Long appointmentId;
    private List<Service> services;
    private BookingAppointmentRequest bookingAppointmentRequest;
}
