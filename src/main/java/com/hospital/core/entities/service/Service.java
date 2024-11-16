package com.hospital.core.entities.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.core.entities.EntityLayout;
import com.hospital.core.entities.invoice.InvoiceService;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    @Column(nullable = false)
    private String name;
    @Column(nullable = false,unique = true)
    private String slug;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Long pointReward;
    //Relationships
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "service")
    private List<InvoiceService> invoiceServices;
}
