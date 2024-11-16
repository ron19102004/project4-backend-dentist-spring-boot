package com.hospital.core.dto.reward_history;

import com.hospital.core.entities.reward.RewardPoint;

import java.util.List;

public record MyRewardHistoriesResponse(
        RewardPoint rewardPoint,
        List<RewardHistoryResponse> rewardHistories
) {
}
