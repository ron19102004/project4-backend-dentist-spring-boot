package com.hospital.app.repositories;

import com.hospital.app.entities.reward.RewardPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardPointRepository extends JpaRepository<RewardPoint, Long> {
}
