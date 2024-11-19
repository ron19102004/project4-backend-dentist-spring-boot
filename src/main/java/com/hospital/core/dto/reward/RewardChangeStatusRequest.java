package com.hospital.core.dto.reward;

import java.util.List;

public record RewardChangeStatusRequest(
        List<Long> listRewardId,
        TypeChangeReward isOpen
) {
}
