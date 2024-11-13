package com.hospital.app.mappers;

import com.hospital.app.dto.user_appointment.BookingAppointmentRequest;
import com.hospital.app.entities.invoice.Invoice;
import com.hospital.app.entities.invoice.InvoiceStatus;
import com.hospital.app.entities.work.Appointment;
import com.hospital.app.entities.work.AppointmentStatus;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class UserAppointmentMapper {
    public Appointment toAppointment(BookingAppointmentRequest request){
        return Appointment.builder()
                .appointmentDate(request.appointmentDate())
                .notes(request.appointmentNote())
                .status(AppointmentStatus.SCHEDULED)
                .build();
    }
    public Invoice toInvoice(BookingAppointmentRequest request){
        return Invoice.builder()
                .status(InvoiceStatus.PENDING)
                .amountOriginPaid(new BigDecimal("0.0"))
                .build();
    }
}
