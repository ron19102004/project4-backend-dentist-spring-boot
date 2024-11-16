package com.hospital.core.entities.reward;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.core.entities.account.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Column(nullable = false)
    private Long point;
    @Column(nullable = false)
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
