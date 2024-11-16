package com.hospital.core.mappers;

import com.hospital.core.dto.reward_history.RewardHistoryResponse;
import com.hospital.core.entities.reward.RewardHistory;
import com.hospital.infrastructure.utils.VietNamTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RewardHistoryMapper {
    public RewardHistoryResponse toRewardHistoryResponse(RewardHistory rewardHistory){
        return RewardHistoryResponse.builder()
                .id(rewardHistory.getId())
                .pointsUsed(rewardHistory.getPointsUsed())
                .content(rewardHistory.getContent())
                .createdAt(VietNamTime.toStringFormated(rewardHistory.getCreatedAt().toInstant()))
                .isUsed(rewardHistory.getInvoice() != null)
                .build();
    }
}
