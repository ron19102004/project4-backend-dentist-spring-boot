package com.hospital.core.services.impls;

import com.hospital.core.dto.appointment.AppointmentByDentistIdInDateResponse;
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

@Service
public class DentistAppointmentServiceImpl implements DentistAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DentalRecordRepository dentalRecordRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuantityAppointmentDentistDTO> countAppointmentDentistInSevenDaysLater(Long dentistId) {
        Date now = VietNamTime.dateNow();
        Date sevenDaysLater = Date.from(now.toInstant().plus(7, ChronoUnit.DAYS));
        return appointmentRepository
                .countAppointmentDentistFollowAppointmentDate(
                        now,
                        sevenDaysLater,
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
            Appointment appointment = appointmentRepository.findByIdAndUserId(appointmentId, request.dentistId());
            if (appointment == null){
                throw ServiceException.builder()
                        .clazz(DentistAppointmentServiceImpl.class)
                        .message("Không tìm thấy lịch hẹn")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            if (appointment.getDentist().getId() != dentistId){
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
            if (dentalRecord.getAppointment().getStatus() == AppointmentStatus.COMPLETED){
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
        entityManager.merge(dentalRecord);
    }
}
