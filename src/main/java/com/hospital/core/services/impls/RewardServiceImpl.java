package com.hospital.core.services.impls;

import com.hospital.core.dto.reward.RewardCreateRequest;
import com.hospital.core.entities.reward.Reward;
import com.hospital.core.events.UpdateListRewardEvent;
import com.hospital.exception.ServiceException;
import com.hospital.core.mappers.RewardMapper;
import com.hospital.core.repositories.RewardRepository;
import com.hospital.core.services.RewardService;
import com.hospital.infrastructure.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardServiceImpl implements RewardService {
    private final RewardRepository rewardRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public RewardServiceImpl(RewardRepository rewardRepository,
                             ApplicationEventPublisher eventPublisher) {
        this.rewardRepository = rewardRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<Reward> getAll() {
        return this.rewardRepository.findAllByDeletedAtIsNullAndIsOpened(true);
    }

    @Override
    public Reward getById(final Long id) {
        return this.rewardRepository.findByIdAndDeletedAtIsNullAndIsOpened(id, true);
    }

    @Override
    public Reward create(final RewardCreateRequest rewardCreateRequest) {
        if (rewardCreateRequest.points() <= 0) {
            throw ServiceException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .clazz(RewardServiceImpl.class)
                    .message("Điểm tích lũy phải lớn hơn 0")
                    .build();
        }
        return this.rewardRepository.save(RewardMapper.
                toRewardFromRewardCreateRequest(rewardCreateRequest));
    }

    @Override
    public void update(final Long id, final Object o) {

    }

    @Transactional
    @Override
    public void delete(final Long id) {
        Reward reward = this.getById(id);
        if (reward == null) {
            throw ServiceException.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .clazz(RewardServiceImpl.class)
                    .message("Không tìm thấy phần thưởng bởi id:" + id)
                    .build();
        }
        reward.setDeletedAt(VietNamTime.dateNow());
        this.entityManager.merge(reward);
        List<Reward> rewards = getAll();
        eventPublisher.publishEvent(new UpdateListRewardEvent(this, rewards));
    }
}
