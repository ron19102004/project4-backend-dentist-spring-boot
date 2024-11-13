package com.hospital.app.services.impls;

import com.hospital.app.dto.user_appointment.QuantityAppointmentDentistDTO;
import com.hospital.app.entities.work.AppointmentStatus;
import com.hospital.app.repositories.AppointmentRepository;
import com.hospital.app.services.DentistAppointmentService;
import com.hospital.app.utils.VietNamTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class DentistAppointmentServiceImpl implements DentistAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<QuantityAppointmentDentistDTO> countAppointmentDentistInSevenNextDay(Long dentistId) {
        Date now = VietNamTime.dateNow();
        Date sevenNextDay = Date.from(now.toInstant().plus(7, ChronoUnit.DAYS));
        return appointmentRepository
                .countAppointmentDentistFollowAppointmentDate(
                        now,
                        sevenNextDay,
                        AppointmentStatus.SCHEDULED,
                        dentistId);
    }
}
