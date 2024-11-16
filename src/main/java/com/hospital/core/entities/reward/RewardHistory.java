package com.hospital.core.entities.reward;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.core.entities.EntityLayout;
import com.hospital.core.entities.invoice.Invoice;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "RewardHistories")
@AllArgsConstructor
@NoArgsConstructor
public class RewardHistory extends EntityLayout {
    //Attributes
    @Column(nullable = false)
    private Long pointsUsed;
    @Column(columnDefinition = "LONGTEXT",nullable = false)
    private String content;
    //Relationships
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rewardPointId",referencedColumnName = "id",nullable = false)
    private RewardPoint rewardPoint;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rewardId",referencedColumnName = "id")
    private Reward reward;
    @JsonIgnore
    @OneToOne(mappedBy = "rewardHistory")
    private Invoice invoice;
}
