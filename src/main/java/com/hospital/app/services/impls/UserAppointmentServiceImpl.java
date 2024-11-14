package com.hospital.app.services.impls;

import com.hospital.app.dto.appointment.AppointmentDTO;
import com.hospital.app.dto.appointment.BookingAppointmentRequest;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.User;
import com.hospital.app.entities.invoice.Invoice;
import com.hospital.app.entities.invoice.InvoiceService;
import com.hospital.app.entities.payment.Payment;
import com.hospital.app.entities.payment.PaymentType;
import com.hospital.app.entities.reward.RewardHistory;
import com.hospital.app.entities.work.Appointment;
import com.hospital.app.entities.work.AppointmentStatus;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.mappers.AppointmentMapper;
import com.hospital.app.mappers.UserAppointmentMapper;
import com.hospital.app.repositories.*;
import com.hospital.app.services.UserAppointmentService;
import com.hospital.app.utils.ResponseLayout;
import com.hospital.app.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserAppointmentServiceImpl implements UserAppointmentService {
    @Autowired
    private DentistRepository dentistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private InvoiceServiceRepository invoiceServiceRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RewardHistoryRepository rewardHistoryRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final static long MAX_APPOINTMENT_IN_DAY = 50;

    @Override
    public void booking(Long userId, BookingAppointmentRequest bookingAppointmentRequest) {
        if (userId == bookingAppointmentRequest.dentistId()) {
            throw ServiceException.builder()
                    .message("Hồ sơ không hợp lệ. Mã người dùng và mã bác sĩ trùng nhau!")
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Appointment appointment = UserAppointmentMapper.toAppointment(bookingAppointmentRequest);
        Dentist dentist = dentistRepository.findById(bookingAppointmentRequest.dentistId()).orElse(null);
        if (dentist == null) {
            throw ServiceException.builder()
                    .message("Bác sĩ không tồn tại")
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        List<Appointment> appointmentList = appointmentRepository.
                findAllUserAppointments(bookingAppointmentRequest.appointmentDate(), userId);
        if (!appointmentList.isEmpty()) {
            throw ServiceException.builder()
                    .message("Bạn đã có cuộc hẹn vào ngày " + appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd-MM-YYYY")))
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        long countAppointmentsDentist = appointmentRepository.countAppointmentsDentist(
                bookingAppointmentRequest.appointmentDate(),
                AppointmentStatus.SCHEDULED,
                dentist.getId());
        if (MAX_APPOINTMENT_IN_DAY <= countAppointmentsDentist) {
            throw ServiceException.builder()
                    .message("Số lượng lịch hẹn trong ngày đã đầy. Xin quý khách chọn ngày khác!")
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        /**
         * Check services when request has exists all in database?
         */
        List<com.hospital.app.entities.service.Service> services =
                serviceRepository.findAllByIdIn(bookingAppointmentRequest.services());
        Set<Long> foundIds = services.stream()
                .map(com.hospital.app.entities.service.Service::getId)
                .collect(Collectors.toSet());
        List<Long> missingIds = bookingAppointmentRequest
                .services()
                .stream()
                .filter(id -> !foundIds.contains(id))
                .toList();
        if (!missingIds.isEmpty()) {
            throw ServiceException.builder()
                    .message("Không tìm thấy Service với các ID: " + missingIds)
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        /**
         * Save appointment
         */
        User user = userRepository.findById(userId).orElse(null);
        appointment.setUser(user);
        appointment.setDentist(dentist);
        Appointment appointmentSaved = appointmentRepository.save(appointment);
        /**
         * Save invoice
         */
        BigDecimal totalPrice = services.stream()
                .map(com.hospital.app.entities.service.Service::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Invoice invoice = UserAppointmentMapper.toInvoice(bookingAppointmentRequest);
        invoice.setAppointment(appointmentSaved);
        invoice.setAmountOriginPaid(totalPrice);
        Invoice invoiceSaved = invoiceRepository.save(invoice);
        /**
         * Save payment
         */
        Payment payment = Payment.builder()
                .amountPaid(totalPrice)
                .paymentType(PaymentType.CASH)
                .invoice(invoiceSaved)
                .build();
        paymentRepository.save(payment);
        /**
         * Save invoice services
         */
        List<InvoiceService> invoiceServices = new ArrayList<>();
        services.forEach(service -> {
            invoiceServices.add(InvoiceService.builder()
                    .service(service)
                    .invoice(invoiceSaved)
                    .priceServiceCurrent(service.getPrice())
                    .nameServiceCurrent(service.getName())
                    .pointRewardCurrent(service.getPointReward())
                    .build());
        });
        invoiceServiceRepository.saveAll(invoiceServices);
        //send mail about to booking
    }

    @Override
    public AppointmentDTO getDetailsUserAppointment(Long userId, Long appointmentId) {
        return AppointmentMapper
                .toAppointmentDTO(appointmentRepository
                        .findByIdAndUserId(appointmentId, userId));
    }

    @Transactional
    @Override
    public void addReward(Long userId, Long appointmentId, Long rewardHistoryId) {
        RewardHistory rewardHistory = rewardHistoryRepository.findById(rewardHistoryId).orElse(null);
        if (rewardHistory == null) {
            throw ServiceException.builder()
                    .message("Không tìm thấy lịch sử đổi điểm với các mã: " + rewardHistoryId)
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        if (!Objects.equals(rewardHistory.getReward().getId(), userId)){
            throw ServiceException.builder()
                    .message("Mã đổi thưởng không hợp lệ")
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
        if (rewardHistory.getInvoice() != null){
            throw ServiceException.builder()
                    .message("Mã đổi thưởng đã được sử dụng")
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Invoice invoice = invoiceRepository.findById(appointmentId).orElse(null);
        if (invoice == null) {
            throw ServiceException.builder()
                    .message("Không tìm thấy hóa đơn với các mã: " + appointmentId)
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        if (!Objects.equals(invoice.getAppointment().getUser().getId(), userId)){
            throw ServiceException.builder()
                    .message("Hóa đơn không hợp lệ")
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
        if (invoice.getRewardHistory() != null){
            throw ServiceException.builder()
                    .message("Hóa đơn đã được áp dụng mã đổi điểm khác")
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        invoice.setRewardHistory(rewardHistory);
        entityManager.merge(invoice);
    }
}
