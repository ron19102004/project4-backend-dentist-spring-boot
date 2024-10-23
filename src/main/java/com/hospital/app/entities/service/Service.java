package com.hospital.app.entities.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.invoice.InvoiceService;
import com.hospital.app.entities.work.Appointment;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "Services")
@AllArgsConstructor
@NoArgsConstructor
public class Service extends EntityLayout {
    //Attributes
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String poster;
    private String name;
    private String slug;
    //Relationships
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "service")
    private List<InvoiceService> invoiceServices;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "service")
    private List<Appointment> appointments;
}
