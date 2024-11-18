package com.hospital.core.dto.invoice;

import com.hospital.core.entities.account.Accountant;
import com.hospital.core.entities.invoice.InvoiceService;
import com.hospital.core.entities.invoice.InvoiceStatus;
import com.hospital.core.entities.payment.Payment;
import com.hospital.core.entities.reward.RewardHistory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Builder
@Data
public class InvoiceDetailsResponse {
    private Long appointmentId;
    private RewardHistory rewardHistory;
    private Payment payment;
    private List<InvoiceService> invoiceServiceList;
    private BigDecimal amountOriginPaid;
    private Accountant accountant;
    private String patientName;
    private InvoiceStatus invoiceStatus;
    private Date createdAt;
    private Date updatedAt;
}
