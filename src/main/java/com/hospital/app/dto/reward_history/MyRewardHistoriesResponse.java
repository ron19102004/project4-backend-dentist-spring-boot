package com.hospital.app.dto.reward_history;

import com.hospital.app.entities.reward.RewardHistory;
import com.hospital.app.entities.reward.RewardPoint;

import java.util.List;

public record MyRewardHistoriesResponse(
        RewardPoint rewardPoint,
        List<RewardHistory> rewardHistories
) {
}
