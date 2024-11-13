package com.hospital.app.services;

import com.hospital.app.dto.appointment.AppointmentByDentistIdInDateResponse;
import com.hospital.app.dto.appointment.AppointmentDTO;
import com.hospital.app.dto.appointment.AppointmentsByDentistRequest;
import com.hospital.app.dto.appointment.QuantityAppointmentDentistDTO;
import com.hospital.app.dto.dental_record.DentalRecordUpdate;

import java.util.Date;
import java.util.List;

public interface DentistAppointmentService {
    List<QuantityAppointmentDentistDTO> countAppointmentDentistInSevenDaysLater(Long dentistId);

    AppointmentByDentistIdInDateResponse getAppointmentByDentistIdInDate(Long dentistId,
                                                                         AppointmentsByDentistRequest request);

    void updateDentalRecord(Long dentistId,Long appointmentId, DentalRecordUpdate request);
}
