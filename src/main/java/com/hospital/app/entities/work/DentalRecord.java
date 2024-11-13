package com.hospital.app.entities.work;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private Date  deletedAt;
    @Temporal(TemporalType.DATE)
    private Date examinationDate;
    @Column(columnDefinition = "LONGTEXT",nullable = false)
    private String diagnosis;
    @Column(columnDefinition = "LONGTEXT",nullable = false)
    private String treatment;
    @Column(columnDefinition = "TEXT",nullable = false)
    private String notes;
    //Relationships
    @JsonIgnore
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id",referencedColumnName = "id", nullable = false)
    private Appointment appointment;
}
