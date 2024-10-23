package com.hospital.app.entities.work;

import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "DentalRecords")
@AllArgsConstructor
@NoArgsConstructor
public class DentalRecord extends EntityLayout {
    //Attribute
    @Temporal(TemporalType.DATE)
    private Date examinationDate;
    @Column(columnDefinition = "LONGTEXT")
    private String diagnosis;
    @Column(columnDefinition = "LONGTEXT")
    private String treatment;
    @Column(columnDefinition = "TEXT")
    private String notes;
    //Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",referencedColumnName = "id",nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dentistId",referencedColumnName = "id",nullable = false)
    private Dentist dentist;
}
