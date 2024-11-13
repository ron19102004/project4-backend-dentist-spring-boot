package com.hospital.app.dto.appointment;

import com.hospital.app.entities.account.Accountant;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.User;
import com.hospital.app.entities.invoice.Invoice;
import com.hospital.app.entities.invoice.InvoiceService;
import com.hospital.app.entities.payment.Payment;
import com.hospital.app.entities.work.Appointment;
import com.hospital.app.entities.work.DentalRecord;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class AppointmentDTO {
    private Appointment appointment;
    private Invoice invoice;
    private DentalRecord dentalRecord;
    private Payment payment;
    private Dentist dentist;
    private User user;
    private List<InvoiceService> invoiceServices;
    private Accountant accountant;
}
