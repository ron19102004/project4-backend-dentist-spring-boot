package com.hospital.app.entities.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.invoice.Invoice;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "Accountants")
@AllArgsConstructor
@NoArgsConstructor
public class Accountant {
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
    //Relationships
    @MapsId
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "accountant",cascade = CascadeType.ALL)
    private List<Invoice> invoices;

}
