package com.hospital.app.services.impls;

import com.hospital.app.dto.reward.RewardCreateRequest;
import com.hospital.app.entities.reward.Reward;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.mappers.RewardMapper;
import com.hospital.app.repositories.RewardRepository;
import com.hospital.app.services.RewardService;
import com.hospital.app.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RewardServiceImpl implements RewardService {
    @Autowired
    private RewardRepository rewardRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Reward> getAll() {
        return this.rewardRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public Reward getById(final Long id) {
        return this.rewardRepository.findByIdAndDeletedAtIsNull(id);
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
    }
}
