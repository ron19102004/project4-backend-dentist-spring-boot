package com.hospital.kafka.consumers;

import com.hospital.core.entities.account.User;
import com.hospital.core.entities.invoice.Invoice;
import com.hospital.core.entities.invoice.InvoiceService;
import com.hospital.core.entities.payment.Payment;
import com.hospital.core.entities.payment.PaymentType;
import com.hospital.core.entities.work.Appointment;
import com.hospital.core.mappers.UserAppointmentMapper;
import com.hospital.core.repositories.*;
import com.hospital.exception.ServiceException;
import com.hospital.kafka.events.BookingKafkaEvent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingConsumerListener {
    private final InvoiceServiceRepository invoiceServiceRepository;
    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final InvoiceRepository invoiceRepository;
    @Autowired
    public BookingConsumerListener(InvoiceServiceRepository invoiceServiceRepository,
                                   PaymentRepository paymentRepository,
                                   AppointmentRepository appointmentRepository,
                                   InvoiceRepository invoiceRepository) {
        this.invoiceServiceRepository = invoiceServiceRepository;
        this.paymentRepository = paymentRepository;
        this.appointmentRepository = appointmentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @KafkaListener(
            topics = "booking-event",
            containerFactory = "bookingEventConcurrentKafkaListenerContainerFactory",
            groupId = "booking-consumer")
    @RetryableTopic(
            backoff = @Backoff(value = 2000L),
            attempts = "5",
            autoCreateTopics = "true",
            exclude = {NullPointerException.class, IllegalArgumentException.class})
    @Transactional
    public void bookingHandler(BookingKafkaEvent event) {
        try {
            Appointment appointment = appointmentRepository
                    .findById(event.getAppointmentId())
                    .orElse(null);
            if (appointment == null) {
                throw ServiceException.builder()
                        .clazz(BookingConsumerListener.class)
                        .message(event.getAppointmentId().toString())
                        .build();
            }
            BigDecimal totalPrice = event.getServices().stream()
                    .map(com.hospital.core.entities.service.Service::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Invoice invoice = UserAppointmentMapper.toInvoice(event.getBookingAppointmentRequest());
            invoice.setAmountOriginPaid(totalPrice);
            invoice.setAppointment(appointment);
            Invoice invoiceSaved = invoiceRepository.save(invoice);

            Payment payment = Payment.builder()
                    .amountPaid(totalPrice)
                    .paymentType(PaymentType.CASH)
                    .invoice(invoiceSaved)
                    .build();
            paymentRepository.save(payment);

            List<InvoiceService> invoiceServices = new ArrayList<>();
            event.getServices().forEach(service -> {
                invoiceServices.add(InvoiceService.builder()
                        .service(service)
                        .invoice(invoiceSaved)
                        .priceServiceCurrent(service.getPrice())
                        .nameServiceCurrent(service.getName())
                        .pointRewardCurrent(service.getPointReward())
                        .build());
            });
            invoiceServiceRepository.saveAll(invoiceServices);
            //send email
        } catch (Exception e){
            throw ServiceException.builder()
                    .clazz(BookingConsumerListener.class)
                    .message(event.getAppointmentId().toString())
                    .build();
        }
    }
}
