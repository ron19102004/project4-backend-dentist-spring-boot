package com.hospital.core.repositories;

import com.hospital.core.dto.appointment.QuantityAppointmentDentistDTO;
import com.hospital.core.entities.work.Appointment;
import com.hospital.core.entities.work.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query(value = "SELECT " +
            "new com.hospital.core.dto.appointment.QuantityAppointmentDentistDTO(a.appointmentDate,COUNT(a.appointmentDate)) " +
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
            "WHERE a.appointmentDate = :date " +
            "AND a.dentist.id = :dentistId " +
            "AND a.status = :status")
    List<Appointment> findAllDentistAppointments(@Param("date") LocalDate date,
                                                 @Param("dentistId") Long dentistId,
                                                 @Param("status") AppointmentStatus status);

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.appointmentDate = :date " +
            "AND a.user.id = :userId")
    List<Appointment> findAllUserAppointments(@Param("date") LocalDate date,
                                              @Param("userId") Long userId);

    @Query("SELECT count(a) " +
            "FROM Appointment a " +
            "WHERE a.appointmentDate = :date " +
            "AND a.status = :status " +
            "AND a.dentist.id = :dentistId")
    long countAppointmentsDentist(@Param("date") LocalDate date,
                                  @Param("status") AppointmentStatus status,
                                  @Param("dentistId") Long dentistId);

    Appointment findByIdAndUserId(Long appointmentId, Long userId);
    List<Appointment> findByUserId(Long userId);
}
