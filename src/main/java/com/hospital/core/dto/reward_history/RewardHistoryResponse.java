package com.hospital.core.dto.reward_history;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RewardHistoryResponse {
    private Long id;
    private Long pointsUsed;
    private String content;
    private String createdAt;
    private boolean isUsed;
}
