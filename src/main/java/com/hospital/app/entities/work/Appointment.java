package com.hospital.app.entities.work;

import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.User;
import com.hospital.app.entities.service.Service;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "Appointments")
@AllArgsConstructor
@NoArgsConstructor
public class Appointment extends EntityLayout {
    //Attributes
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    //Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",referencedColumnName = "id",nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dentistId",referencedColumnName = "id",nullable = false)
    private Dentist dentist;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "serviceId",referencedColumnName = "id",nullable = false)
    private Service service;

}
