package com.hospital.core.mappers;

import com.hospital.core.dto.appointment.AppointmentByDentistIdInDateResponse;
import com.hospital.core.dto.appointment.AppointmentDTO;
import com.hospital.core.dto.dentist.DentistResponse;
import com.hospital.core.dto.invoice.InvoiceDetailsResponse;
import com.hospital.core.entities.invoice.Invoice;
import com.hospital.core.entities.work.Appointment;
import com.hospital.exception.ServiceException;
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
        AppointmentDTO.AppointmentDTOBuilder builder = AppointmentDTO.builder();
        builder
                .appointment(appointment)
                .user(appointment.getUser());
        if (appointment.getInvoice() != null) {
            builder
                    .invoice(appointment.getInvoice())
                    .payment(appointment.getInvoice().getPayment())
                    .invoiceServices(appointment.getInvoice().getInvoiceServices())
                    .accountant(appointment.getInvoice().getAccountant());
        }
        if (appointment.getDentist() != null) {
            builder.dentist(DentistMapper.toDentistResponse(appointment.getDentist()));
        }
        if (appointment.getDentalRecord() != null) {
            builder.dentalRecord(appointment.getDentalRecord());
        }
        return builder.build();
    }

    public AppointmentByDentistIdInDateResponse toAppointmentByDentistIdInDateResponse(List<Appointment> appointments) {
        return AppointmentByDentistIdInDateResponse.builder()
                .size(appointments.size())
                .appointments(appointments.stream()
                        .map(AppointmentMapper::toAppointmentDTO)
                        .toList())
                .build();
    }

    public InvoiceDetailsResponse toInvoiceDetailsResponse(Invoice invoice) {
        return InvoiceDetailsResponse.builder()
                .appointmentId(invoice.getId())
                .invoiceServiceList(invoice.getInvoiceServices())
                .invoiceStatus(invoice.getStatus())
                .patientName(invoice.getAppointment().getUser().getFullName())
                .amountOriginPaid(invoice.getAmountOriginPaid())
                .rewardHistory(invoice.getRewardHistory())
                .accountant(invoice.getAccountant())
                .payment(invoice.getPayment())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }
}
