package com.hospital.app.services.impls;

import com.hospital.app.entities.reward.RewardPoint;
import com.hospital.app.repositories.RewardPointRepository;
import com.hospital.app.services.RewardPointService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardPointServiceImpl implements RewardPointService {
    @Autowired
    private RewardPointRepository rewardPointRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public RewardPoint saveRewardPoint(final RewardPoint rewardPoint) {
        return this.rewardPointRepository.save(rewardPoint);
    }
}
