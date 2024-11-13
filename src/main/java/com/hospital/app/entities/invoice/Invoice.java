package com.hospital.app.entities.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.account.Accountant;
import com.hospital.app.entities.account.User;
import com.hospital.app.entities.payment.Payment;
import com.hospital.app.entities.reward.RewardHistory;
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
    private BigDecimal amountOriginPaid;
    //Relationships
    @MapsId
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "id",referencedColumnName = "id", nullable = false)
    private Appointment appointment;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountantId", referencedColumnName = "id")
    @JsonIgnore
    private Accountant accountant;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "invoice")
    private Payment payment;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "invoice",fetch = FetchType.EAGER)
    private List<InvoiceService> invoiceServices;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rewardHistoryId",referencedColumnName = "id",unique = true)
    private RewardHistory rewardHistory;
}
