package com.hospital.app.entities.work;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.User;
import com.hospital.app.entities.invoice.Invoice;
import com.hospital.app.entities.service.Service;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Appointments", indexes = {
        @Index(name = "idx_appointment_date", columnList = "appointmentDate"),
        @Index(name = "idx_dentist_id", columnList = "dentist_id"),
        @Index(name = "idx_appointment_status", columnList = "status")
})
public class Appointment extends EntityLayout {
    //Attributes
    @Column(nullable = false)
//    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;
    @Column(columnDefinition = "LONGTEXT")
    private String notes;
    //Relationships
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private User user;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dentistId", referencedColumnName = "id", nullable = false)
    private Dentist dentist;
    @JsonIgnore
    @OneToOne(mappedBy = "appointment", orphanRemoval = true)
    private Invoice invoice;
    @JsonIgnore
    @OneToOne(mappedBy = "appointment", orphanRemoval = true)
    private DentalRecord dentalRecord;

}
