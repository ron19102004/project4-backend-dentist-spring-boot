package com.hospital.core.services.impls;

import com.hospital.core.dto.reward.RewardChangeStatusRequest;
import com.hospital.core.dto.reward.RewardCreateRequest;
import com.hospital.core.dto.reward.TypeChangeReward;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        List<Reward> rewards = getAll();
        eventPublisher.publishEvent(new UpdateListRewardEvent(this, rewards));
        return this.rewardRepository.save(RewardMapper.
                toRewardFromRewardCreateRequest(rewardCreateRequest));
    }

    @Override
    public void update(final Long id, final Object o) {

    }

    @Transactional
    @Override
    public void delete(final Long id) {
        Reward reward = rewardRepository.findById(id).orElse(null);
        if (reward == null) {
            throw ServiceException.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .clazz(RewardServiceImpl.class)
                    .message("Không tìm thấy phần thưởng bởi id:" + id)
                    .build();
        }
        if (reward.getDeletedAt() != null){
            throw ServiceException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .clazz(RewardServiceImpl.class)
                    .message("Phần thưởng này đã được xóa")
                    .build();
        }
        reward.setDeletedAt(VietNamTime.dateNow());
        List<Reward> rewards = getAll();
        eventPublisher.publishEvent(new UpdateListRewardEvent(this, rewards));
    }

    @Transactional
    @Override
    public void changeOpen(RewardChangeStatusRequest request) {
        List<Reward> rewards = rewardRepository.findAllByIdIn(request.listRewardId());
        Set<Long> idsFound = rewards.stream().map(Reward::getId).collect(Collectors.toSet());
        List<Long> idsMissing = request.listRewardId().stream().filter(id -> !idsFound.contains(id)).toList();
        if (!idsMissing.isEmpty()) {
            throw ServiceException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Không tìm thấy phần thưởng với các mã: " + idsMissing)
                    .clazz(RewardServiceImpl.class)
                    .build();
        }
        rewards.forEach(reward -> reward.setIsOpened(request.isOpen() == TypeChangeReward.ON));
        List<Reward> rewardsRedis = getAll();
        eventPublisher.publishEvent(new UpdateListRewardEvent(this, rewardsRedis));
    }

    @Override
    public List<Reward> getAllByDeletedIsNull(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        return rewardRepository.findAllByDeletedAtIsNull(pageable).toList();
    }

    @Override
    public List<Reward> getAllByDeleted(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        return rewardRepository.findAllByDeletedAtIsNotNull(pageable).toList();
    }
}
