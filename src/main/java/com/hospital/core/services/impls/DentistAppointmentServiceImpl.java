package com.hospital.core.services.impls;

import com.hospital.core.dto.appointment.AppointmentByDentistIdInDateResponse;
import com.hospital.core.dto.appointment.AppointmentDTO;
import com.hospital.core.dto.appointment.AppointmentsByDentistRequest;
import com.hospital.core.dto.appointment.QuantityAppointmentDentistDTO;
import com.hospital.core.dto.dental_record.DentalRecordUpdate;
import com.hospital.core.entities.work.Appointment;
import com.hospital.core.entities.work.AppointmentStatus;
import com.hospital.core.entities.work.DentalRecord;
import com.hospital.exception.ServiceException;
import com.hospital.core.mappers.AppointmentMapper;
import com.hospital.core.mappers.DentistAppointmentMapper;
import com.hospital.core.repositories.AppointmentRepository;
import com.hospital.core.repositories.DentalRecordRepository;
import com.hospital.core.services.DentistAppointmentService;
import com.hospital.infrastructure.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DentistAppointmentServiceImpl implements DentistAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DentalRecordRepository dentalRecordRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DentistAppointmentServiceImpl(AppointmentRepository appointmentRepository, DentalRecordRepository dentalRecordRepository) {
        this.appointmentRepository = appointmentRepository;
        this.dentalRecordRepository = dentalRecordRepository;
    }

    @Override
    public List<QuantityAppointmentDentistDTO> countAppointmentDentistInSevenDaysLater(Long dentistId) {
        Date now = VietNamTime.dateNow();
        Date sevenDaysLater = Date.from(now.toInstant().plus(7, ChronoUnit.DAYS));
        return appointmentRepository
                .countAppointmentDentistFollowAppointmentDate(
                        VietNamTime.toLocalDate(now),
                        VietNamTime.toLocalDate(sevenDaysLater),
                        AppointmentStatus.SCHEDULED,
                        dentistId);
    }

    @Override
    public AppointmentByDentistIdInDateResponse getAppointmentByDentistIdInDate(Long dentistId, AppointmentsByDentistRequest request) {
        return AppointmentMapper
                .toAppointmentByDentistIdInDateResponse(
                        appointmentRepository
                                .findAllDentistAppointments(request.date(), dentistId, AppointmentStatus.SCHEDULED));
    }

    @Transactional
    @Override
    public void updateDentalRecord(Long dentistId, Long appointmentId, DentalRecordUpdate request) {
        DentalRecord dentalRecord = dentalRecordRepository
                .findByAppointmentIdAndAptStatus(appointmentId, AppointmentStatus.SCHEDULED);
        if (dentalRecord == null) {
            Appointment appointment = appointmentRepository.findByIdAndUserId(appointmentId, dentistId);
            if (appointment == null) {
                throw ServiceException.builder()
                        .clazz(DentistAppointmentServiceImpl.class)
                        .message("Không tìm thấy lịch hẹn")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            if (!Objects.equals(appointment.getDentist().getId(), dentistId)) {
                throw ServiceException.builder()
                        .clazz(DentistAppointmentServiceImpl.class)
                        .message("Bạn không có quyền chỉnh sửa với hồ sơ này")
                        .status(HttpStatus.FORBIDDEN)
                        .build();
            }
            dentalRecord = DentistAppointmentMapper.toDentalRecord(request);
            dentalRecord.setAppointment(appointment);
            dentalRecord.setCreatedAt(VietNamTime.dateNow());
            dentalRecord.setUpdatedAt(VietNamTime.dateNow());
            dentalRecordRepository.save(dentalRecord);
            return;
        } else {
            if (dentalRecord.getAppointment().getStatus() == AppointmentStatus.COMPLETED) {
                throw ServiceException.builder()
                        .clazz(DentistAppointmentServiceImpl.class)
                        .message("Hồ sơ đã được kết thúc")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        }
        dentalRecord.setNotes(request.notes());
        dentalRecord.setTreatment(request.treatment());
        dentalRecord.setDiagnosis(request.diagnosis());
        dentalRecord.setUpdatedAt(VietNamTime.dateNow());
    }

    @Override
    public AppointmentDTO getAppointmentById(Long userOrDentistId, Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment != null) {
            boolean isForDentist = userOrDentistId.equals(appointment.getDentist().getId());
            boolean isForUser = userOrDentistId.equals(appointment.getUser().getId());
            if (!(isForDentist || isForUser)) {
                throw ServiceException.builder()
                        .status(HttpStatus.FORBIDDEN)
                        .message("Không có quyền truy cập")
                        .clazz(DentistAppointmentServiceImpl.class)
                        .build();

            }
        }
        return AppointmentMapper.toAppointmentDTO(appointment);
    }

    @Transactional
    @Override
    public void finishAppointment(Long dentistId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            throw ServiceException.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Không tìm thấy lịch hẹn")
                    .clazz(DentistAppointmentServiceImpl.class)
                    .build();
        }
        if (!appointment.getDentist().getId().equals(dentistId)) {
            throw ServiceException.builder()
                    .status(HttpStatus.FORBIDDEN)
                    .message("Không có quyền truy cập")
                    .clazz(DentistAppointmentServiceImpl.class)
                    .build();
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw ServiceException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Lịch hẹn đã bị hủy")
                    .clazz(DentistAppointmentServiceImpl.class)
                    .build();
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw ServiceException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Lịch hẹn đã hoàn thành")
                    .clazz(DentistAppointmentServiceImpl.class)
                    .build();
        }
        appointment.setStatus(AppointmentStatus.COMPLETED);
    }

    @Transactional
    @Override
    public void cancelAppointment(Long dentistId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            throw ServiceException.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Không tìm thấy lịch hẹn")
                    .clazz(DentistAppointmentServiceImpl.class)
                    .build();
        }
        if (!appointment.getDentist().getId().equals(dentistId)) {
            throw ServiceException.builder()
                    .status(HttpStatus.FORBIDDEN)
                    .message("Không có quyền truy cập")
                    .clazz(DentistAppointmentServiceImpl.class)
                    .build();
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw ServiceException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Lịch hẹn đã hoàn thành")
                    .clazz(DentistAppointmentServiceImpl.class)
                    .build();
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }
}
