package com.hospital.core.mappers;

import com.hospital.core.dto.reward.RewardCreateRequest;
import com.hospital.core.entities.reward.Reward;
import com.hospital.infrastructure.utils.GgImageUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RewardMapper {
    public Reward toRewardFromRewardCreateRequest(RewardCreateRequest rewardCreateRequest) {
        return Reward.builder()
                .content(rewardCreateRequest.content())
                .points(rewardCreateRequest.points())
                .poster(GgImageUtil.parse(rewardCreateRequest.poster()))
                .isOpened(false)
                .build();
    }

}
