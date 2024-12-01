package com.hospital.core.services;

import com.hospital.core.dto.reward_history.MyRewardHistoriesResponse;
import com.hospital.core.entities.reward.RewardHistory;

public interface RewardHistoryService {
    RewardHistory plusPoint(Long userId, Long point, String message);
    RewardHistory usePoint(Long userId, Long rewardId);
    MyRewardHistoriesResponse getMyRewardHistories(Long userId);
}
