package com.hospital.app.entities.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.blog.Blog;
import com.hospital.app.entities.work.Appointment;
import com.hospital.app.entities.work.DentalRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "Dentists")
@AllArgsConstructor
@NoArgsConstructor
public class Dentist {
    //Attributes
    @Id
    private Long id;
    private String email;
    private String phoneNumber;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private Date deletedAt;
    //Relationships
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private User user;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "dentist")
    private List<Appointment> appointments;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id",name = "specializeId", nullable = false)
    private Specialize specialize;
    @JsonIgnore
    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL)
    private List<Blog> blogs;

}
