package com.hospital.core.repositories;

import com.hospital.core.entities.reward.RewardHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RewardHistoryRepository extends JpaRepository<RewardHistory,Long> {

}
