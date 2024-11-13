package com.hospital.app.entities.reward;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Long id;
    private Long point;
    private Long pointsUsed;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date lastUpdatedAt;
    //Relationships
    @JsonIgnore
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private User user;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "rewardPoint")
    private List<RewardHistory> rewardHistories;
}
