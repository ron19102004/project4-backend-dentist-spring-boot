package com.hospital.app.entities.reward;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.app.entities.EntityLayout;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    private Long points;
    private String content;
    @Column(columnDefinition = "TEXT")
    private String poster;
    @ColumnDefault("FALSE")
    private Boolean isOpened;
    //Relationships
    @JsonIgnore
    @OneToMany(mappedBy = "reward",cascade = CascadeType.ALL)
    private List<RewardHistory> rewardHistories;
}
