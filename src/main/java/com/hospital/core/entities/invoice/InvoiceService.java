package com.hospital.core.entities.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.core.entities.EntityLayout;
import com.hospital.core.entities.service.Service;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "InvoiceServices")
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceService extends EntityLayout {
    //Attributes
    @Column(nullable = false)
    private BigDecimal priceServiceCurrent;
    @Column(nullable = false)
    private String nameServiceCurrent;
    @Column(nullable = false)
    private Long pointRewardCurrent;
    //Relationships
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id",name = "invoiceId",nullable = false)
    private Invoice invoice;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id",name = "serviceId",nullable = false)
    private Service service;
}
