package com.hospital.app.entities.reward;

import com.hospital.app.entities.account.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "RewardPoints")
@AllArgsConstructor
@NoArgsConstructor
public class RewardPoint {
    //Attributes
    @Id
    private Long id;
    private Long pointsUsed;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date lastUpdate;
    //Relationships
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private User user;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "rewardPoint")
    private List<RewardRedemptionHistory> rewardRedemptionHistories;
}
