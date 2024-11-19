package com.hospital.core.repositories;

import com.hospital.core.entities.reward.Reward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findAllByDeletedAtIsNullAndIsOpened(Boolean isOpened);
    Reward findByIdAndDeletedAtIsNullAndIsOpened(Long id,Boolean isOpened);
    List<Reward> findAllByIdIn(List<Long> ids);
    Page<Reward> findAllByDeletedAtIsNull(Pageable pageable);
    Page<Reward> findAllByDeletedAtIsNotNull(Pageable pageable);
}
