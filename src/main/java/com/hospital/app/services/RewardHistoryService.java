package com.hospital.app.services;

import com.hospital.app.dto.reward_history.MyRewardHistoriesResponse;
import com.hospital.app.entities.reward.RewardHistory;

public interface RewardHistoryService {
    RewardHistory plusPoint(Long userId, Long point, String message);
    RewardHistory usePoint(Long userId, Long rewardId, String message);
    MyRewardHistoriesResponse getMyRewardHistories(Long userId);
}
