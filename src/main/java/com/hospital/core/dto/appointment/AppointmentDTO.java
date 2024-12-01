package com.hospital.core.dto.appointment;

import com.hospital.core.dto.dentist.DentistResponse;
import com.hospital.core.entities.account.Accountant;
import com.hospital.core.entities.account.Dentist;
import com.hospital.core.entities.account.User;
import com.hospital.core.entities.invoice.Invoice;
import com.hospital.core.entities.invoice.InvoiceService;
import com.hospital.core.entities.payment.Payment;
import com.hospital.core.entities.work.Appointment;
import com.hospital.core.entities.work.DentalRecord;
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
    private DentistResponse dentist;
    private User user;
    private List<InvoiceService> invoiceServices;
    private Accountant accountant;
}
