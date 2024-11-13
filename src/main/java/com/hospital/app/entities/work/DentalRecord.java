package com.hospital.app.entities.work;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "DentalRecords")
@AllArgsConstructor
@NoArgsConstructor
public class DentalRecord {
    //Attribute
    @Id
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private Date  deletedAt;
    @Temporal(TemporalType.DATE)
    private Date examinationDate;
    @Column(columnDefinition = "LONGTEXT")
    private String diagnosis;
    @Column(columnDefinition = "LONGTEXT")
    private String treatment;
    @Column(columnDefinition = "TEXT")
    private String notes;
    //Relationships
    @MapsId
    @OneToOne
    @JoinColumn(name = "id",referencedColumnName = "id", nullable = false)
    private Appointment appointment;
}
