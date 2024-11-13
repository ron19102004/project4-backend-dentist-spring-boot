package com.hospital.app.mappers;

import com.hospital.app.dto.reward.RewardCreateRequest;
import com.hospital.app.entities.reward.Reward;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RewardMapper {
    public Reward toRewardFromRewardCreateRequest(RewardCreateRequest rewardCreateRequest) {
        return Reward.builder()
                .content(rewardCreateRequest.content())
                .points(rewardCreateRequest.points())
                .build();
    }

}
