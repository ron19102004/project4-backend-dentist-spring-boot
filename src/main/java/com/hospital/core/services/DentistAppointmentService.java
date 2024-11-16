package com.hospital.core.services;

import com.hospital.core.dto.appointment.AppointmentByDentistIdInDateResponse;
import com.hospital.core.dto.appointment.AppointmentsByDentistRequest;
import com.hospital.core.dto.appointment.QuantityAppointmentDentistDTO;
import com.hospital.core.dto.dental_record.DentalRecordUpdate;

import java.util.List;

public interface DentistAppointmentService {
    List<QuantityAppointmentDentistDTO> countAppointmentDentistInSevenDaysLater(Long dentistId);

    AppointmentByDentistIdInDateResponse getAppointmentByDentistIdInDate(Long dentistId,
                                                                         AppointmentsByDentistRequest request);

    void updateDentalRecord(Long dentistId,Long appointmentId, DentalRecordUpdate request);
}
