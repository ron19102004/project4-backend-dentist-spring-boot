package com.hospital.app.entities.invoice;

import com.hospital.app.entities.EntityLayout;
import com.hospital.app.entities.account.Accountant;
import com.hospital.app.entities.account.User;
import com.hospital.app.entities.payment.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "Invoices")
@AllArgsConstructor
@NoArgsConstructor
public class Invoice extends EntityLayout {
    //Attributes
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
    //Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountantId", referencedColumnName = "id", nullable = false)
    private Accountant accountant;
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "invoice")
    private Payment payment;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "invoice")
    private List<InvoiceService> invoiceServices;
}
