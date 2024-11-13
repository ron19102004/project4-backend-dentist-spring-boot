package com.hospital.app.repositories;

import com.hospital.app.dto.user_appointment.QuantityAppointmentDentistDTO;
import com.hospital.app.entities.work.Appointment;
import com.hospital.app.entities.work.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query(value = "SELECT " +
            "new com.hospital.app.dto.user_appointment.QuantityAppointmentDentistDTO(a.appointmentDate,COUNT(a.appointmentDate)) " +
            "FROM Appointment a " +
            "WHERE a.appointmentDate BETWEEN :datePre AND :dateNext " +
            "AND a.status = :status " +
            "AND a.dentist.id = :dentistId " +
            "GROUP BY a.appointmentDate")
    List<QuantityAppointmentDentistDTO> countAppointmentDentistFollowAppointmentDate(@Param("datePre") Date datePre,
                                                                  @Param("dateNext") Date dateNext,
                                                                  @Param("status") AppointmentStatus status,
                                                                  @Param("dentistId") Long dentistId);

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.appointmentDate BETWEEN :datePre AND :dateNext " +
            "AND a.dentist.id = :dentistId " +
            "AND a.status = :status")
    List<Appointment> findAllDentistAppointments(@Param("datePre") Date datePre,
                                                 @Param("dateNext") Date dateNext,
                                                 @Param("dentistId") Long dentistId,
                                                 @Param("status") AppointmentStatus status);

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.appointmentDate BETWEEN :datePre AND :dateNext " +
            "AND a.user.id = :userId")
    List<Appointment> findAllUserAppointments(@Param("datePre") Date datePre,
                                              @Param("dateNext") Date dateNext,
                                              @Param("userId") Long userId);
}
