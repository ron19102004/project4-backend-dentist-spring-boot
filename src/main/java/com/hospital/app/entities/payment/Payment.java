package com.hospital.app.entities.payment;

import com.hospital.app.entities.invoice.Invoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    private BigDecimal amountPaid;
    private PaymentStatus status;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    //Relationships
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "id", nullable = false)
    private Invoice invoice;

}
