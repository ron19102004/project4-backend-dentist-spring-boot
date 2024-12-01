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
        return AppointmentDTO.builder()
                .appointment(appointment)
                .invoice(appointment.getInvoice())
                .dentalRecord(appointment.getDentalRecord())
                .payment(appointment.getInvoice().getPayment())
                .dentist(DentistMapper.toDentistResponse(appointment.getDentist()))
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
