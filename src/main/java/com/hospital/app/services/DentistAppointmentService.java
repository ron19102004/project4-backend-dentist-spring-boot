package com.hospital.app.services;

import com.hospital.app.dto.user_appointment.QuantityAppointmentDentistDTO;

import java.util.List;

public interface DentistAppointmentService {
    List<QuantityAppointmentDentistDTO> countAppointmentDentistInSevenNextDay(Long dentistId);
}
