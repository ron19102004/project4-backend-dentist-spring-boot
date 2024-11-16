package com.hospital.core.mappers;

import com.hospital.core.dto.appointment.BookingAppointmentRequest;
import com.hospital.core.entities.invoice.Invoice;
import com.hospital.core.entities.invoice.InvoiceStatus;
import com.hospital.core.entities.work.Appointment;
import com.hospital.core.entities.work.AppointmentStatus;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class UserAppointmentMapper {
    public Appointment toAppointment(BookingAppointmentRequest request) {
        return Appointment.builder()
                .appointmentDate(request.appointmentDate())
                .notes(request.appointmentNote())
                .status(AppointmentStatus.SCHEDULED)
                .build();
    }

    public Invoice toInvoice(BookingAppointmentRequest request) {
        return Invoice.builder()
                .status(InvoiceStatus.PENDING)
                .amountOriginPaid(new BigDecimal("0.0"))
                .build();
    }
}
