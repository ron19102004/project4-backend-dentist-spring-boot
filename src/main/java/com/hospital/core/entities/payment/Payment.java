package com.hospital.core.entities.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.core.entities.invoice.Invoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "Payments")
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    //Attributes
    @Id
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    @Column(nullable = false)
    private BigDecimal amountPaid;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private Long discountPercent;
    //Relationships
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(referencedColumnName = "id", name = "id", nullable = false)
    private Invoice invoice;

}
