package com.hospital.core.mappers;

import com.hospital.core.dto.reward.RewardCreateRequest;
import com.hospital.core.entities.reward.Reward;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RewardMapper {
    public Reward toRewardFromRewardCreateRequest(RewardCreateRequest rewardCreateRequest) {
        return Reward.builder()
                .content(rewardCreateRequest.content())
                .points(rewardCreateRequest.points())
                .isOpened(false)
                .build();
    }

}
