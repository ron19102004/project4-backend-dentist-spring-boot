package com.hospital.app.mappers;

import com.hospital.app.dto.appointment.AppointmentByDentistIdInDateResponse;
import com.hospital.app.dto.appointment.AppointmentDTO;
import com.hospital.app.entities.work.Appointment;
import com.hospital.app.exception.ServiceException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import java.util.List;

@UtilityClass
public class AppointmentMapper {
    public AppointmentDTO toAppointmentDTO(Appointment appointment) {
        if (appointment == null) {
            throw ServiceException.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Không tìm thấy hồ sơ")
                    .clazz(AppointmentMapper.class)
                    .build();
        }
        return AppointmentDTO.builder()
                .appointment(appointment)
                .invoice(appointment.getInvoice())
                .dentalRecord(appointment.getDentalRecord())
                .payment(appointment.getInvoice().getPayment())
                .dentist(appointment.getDentist())
                .invoiceServices(appointment.getInvoice().getInvoiceServices())
                .user(appointment.getUser())
                .accountant(appointment.getInvoice().getAccountant())
                .build();
    }

    public AppointmentByDentistIdInDateResponse toAppointmentByDentistIdInDateResponse(List<Appointment> appointments) {
        return AppointmentByDentistIdInDateResponse.builder()
                .size(appointments.size())
                .appointments(appointments.stream()
                        .map(AppointmentMapper::toAppointmentDTO)
                        .toList())
                .build();
    }
}
