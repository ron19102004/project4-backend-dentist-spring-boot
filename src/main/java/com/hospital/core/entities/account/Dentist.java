package com.hospital.core.entities.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.core.entities.blog.Blog;
import com.hospital.core.entities.work.Appointment;
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
    @Column(unique = true,nullable = false)
    private String email;
    @Column(unique = true,nullable = false)
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
    @JsonIgnore
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private User user;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "dentist")
    private List<Appointment> appointments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id",name = "specializeId", nullable = false)
    private Specialize specialize;
    @JsonIgnore
    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL)
    private List<Blog> blogs;

}
