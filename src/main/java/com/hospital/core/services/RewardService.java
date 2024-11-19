package com.hospital.core.services;

import com.hospital.core.dto.reward.RewardChangeStatusRequest;
import com.hospital.core.dto.reward.RewardCreateRequest;
import com.hospital.core.entities.reward.Reward;
import com.hospital.core.services.def.IReaderService;
import com.hospital.core.services.def.IWriteService;

import java.util.List;

public interface RewardService extends
        IReaderService<Reward, Long>,
        IWriteService<Reward, RewardCreateRequest, Object, Long> {
    void changeOpen(RewardChangeStatusRequest request);
    List<Reward> getAllByDeletedIsNull(int pageNumber);
    List<Reward> getAllByDeleted(int pageNumber);
}
