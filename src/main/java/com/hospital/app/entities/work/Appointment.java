package com.hospital.app.entities.work;

import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.User;
import com.hospital.app.entities.invoice.Invoice;
import com.hospital.app.entities.service.Service;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Appointments",indexes = {
        @Index(name = "idx_appointment_date", columnList = "appointmentDate"),
        @Index(name = "idx_dentist_id", columnList = "dentist_id"),
        @Index(name = "idx_appointment_status", columnList = "status")
})
public class Appointment extends EntityLayout {
    //Attributes
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    @Column(columnDefinition = "LONGTEXT")
    private String notes;
    //Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",referencedColumnName = "id",nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dentistId",referencedColumnName = "id",nullable = false)
    private Dentist dentist;
    @OneToOne(mappedBy = "appointment", orphanRemoval = true)
    private Invoice invoice;
    @OneToOne(mappedBy = "appointment",orphanRemoval = true)
    private DentalRecord dentalRecord;

}
