package com.hospital.app.entities.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.account.Accountant;
import com.hospital.app.entities.account.User;
import com.hospital.app.entities.payment.Payment;
import com.hospital.app.entities.work.Appointment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "Invoices")
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    //Attributes
    @Id
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private Date  deletedAt;
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
    private Long rewardHistoryId;
    private BigDecimal amountOriginPaid;
    //Relationships
    @MapsId
    @OneToOne
    @JoinColumn(name = "id",referencedColumnName = "id", nullable = false)
    private Appointment appointment;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountantId", referencedColumnName = "id")
    private Accountant accountant;
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "invoice")
    private Payment payment;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "invoice")
    private List<InvoiceService> invoiceServices;
}
