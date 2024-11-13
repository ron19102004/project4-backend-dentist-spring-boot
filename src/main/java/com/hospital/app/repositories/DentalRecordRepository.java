package com.hospital.app.repositories;

import com.hospital.app.entities.work.AppointmentStatus;
import com.hospital.app.entities.work.DentalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DentalRecordRepository extends JpaRepository<DentalRecord, Long> {
    @Query(value = "SELECT d FROM  DentalRecord d " +
            "WHERE d.appointment.id = :appointmentId " +
            "AND d.appointment.status = :status ")
    DentalRecord findByAppointmentIdAndAptStatus(@Param("appointmentId") Long appointmentId,
                                                 @Param("appointmentId") AppointmentStatus status);
}
