package com.hospital.app.services;

import com.hospital.app.dto.reward.RewardCreateRequest;
import com.hospital.app.entities.reward.Reward;
import com.hospital.app.services.def.IReaderService;
import com.hospital.app.services.def.IWriteService;

public interface RewardService extends
        IReaderService<Reward, Long>,
        IWriteService<Reward, RewardCreateRequest, Object, Long> {
}