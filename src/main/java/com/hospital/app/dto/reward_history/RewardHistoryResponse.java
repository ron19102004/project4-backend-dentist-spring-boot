package com.hospital.app.dto.reward_history;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class RewardHistoryResponse {
    private Long id;
    private Long pointsUsed;
    private String content;
    private String createdAt;
    private boolean isUsed;
}
