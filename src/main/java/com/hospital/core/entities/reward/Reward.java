package com.hospital.core.entities.reward;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.core.entities.EntityLayout;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.context.event.EventListener;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "Rewards")
@AllArgsConstructor
@NoArgsConstructor
public class Reward extends EntityLayout {
    //Attributes
    @Column(nullable = false)
    private Long points;
    @Column(nullable = false)
    private String content;
    @Column(columnDefinition = "TEXT")
    private String poster;
    @ColumnDefault("FALSE")
    private Boolean isOpened;
    //Relationships
    @JsonIgnore
    @OneToMany(mappedBy = "reward")
    private List<RewardHistory> rewardHistories;
}
