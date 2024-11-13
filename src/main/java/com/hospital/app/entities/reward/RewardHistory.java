package com.hospital.app.entities.reward;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.EntityLayout;
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
    private Long pointsUsed;
    @Column(columnDefinition = "TEXT")
    private String notes;
    //Relationships
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rewardPointId",referencedColumnName = "id",nullable = false)
    private RewardPoint rewardPoint;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rewardId",referencedColumnName = "id")
    private Reward reward;
}
