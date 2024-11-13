package com.hospital.app.services.impls;

import com.hospital.app.dto.user_appointment.AppointmentDentistDTO;
import com.hospital.app.dto.user_appointment.BookingAppointmentRequest;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.invoice.Invoice;
import com.hospital.app.entities.work.Appointment;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.mappers.UserAppointmentMapper;
import com.hospital.app.repositories.AppointmentRepository;
import com.hospital.app.repositories.DentistRepository;
import com.hospital.app.repositories.InvoiceRepository;
import com.hospital.app.repositories.UserRepository;
import com.hospital.app.services.UserAppointmentService;
import com.hospital.app.utils.VietNamTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

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

    @Override
    public void booking(Long userId, BookingAppointmentRequest bookingAppointmentRequest) {
        Appointment appointment = UserAppointmentMapper.toAppointment(bookingAppointmentRequest);
        Dentist dentist = dentistRepository.findById(bookingAppointmentRequest.dentistId()).orElse(null);
        if (dentist == null){
            throw ServiceException.builder()
                    .message("Bác sĩ không tồn tại")
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Date datePre = Date.from(appointment.getAppointmentDate().toInstant().minus(1, ChronoUnit.DAYS));
        Date dateNext = Date.from(appointment.getAppointmentDate().toInstant().plus(1, ChronoUnit.DAYS));
        List<Appointment> appointmentList = appointmentRepository.
                findAllUserAppointments(datePre, dateNext, userId);
        if (!appointmentList.isEmpty()){
            throw ServiceException.builder()
                    .message("Đã có cuộc hẹn vào " + VietNamTime.toStringFormated(appointment.getAppointmentDate().toInstant()))
                    .clazz(UserAppointmentServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Invoice invoice = UserAppointmentMapper.toInvoice(bookingAppointmentRequest);

    }
}
