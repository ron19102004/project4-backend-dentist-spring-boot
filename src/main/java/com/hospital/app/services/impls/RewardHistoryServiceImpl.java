package com.hospital.app.services.impls;

import com.hospital.app.dto.reward_history.MyRewardHistoriesResponse;
import com.hospital.app.entities.account.User;
import com.hospital.app.entities.reward.Reward;
import com.hospital.app.entities.reward.RewardHistory;
import com.hospital.app.entities.reward.RewardPoint;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.mappers.RewardHistoryMapper;
import com.hospital.app.repositories.RewardHistoryRepository;
import com.hospital.app.repositories.RewardPointRepository;
import com.hospital.app.repositories.RewardRepository;
import com.hospital.app.repositories.UserRepository;
import com.hospital.app.services.RewardHistoryService;
import com.hospital.app.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RewardHistoryServiceImpl implements RewardHistoryService {
    @Autowired
    private RewardHistoryRepository rewardHistoryRepository;
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private RewardPointRepository rewardPointRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RewardRepository rewardRepository;


    @Transactional
    @Override
    public RewardHistory plusPoint(Long userId, Long point, String message) {
        RewardPoint myRewardPoint = rewardPointRepository.findById(userId).orElse(null);
        if (myRewardPoint == null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                throw ServiceException.builder()
                        .message("Tài khoản người dùng không tồn tại")
                        .clazz(RewardHistoryServiceImpl.class)
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }
            myRewardPoint = rewardPointRepository.save(RewardPoint.builder()
                    .user(user)
                    .point(0L)
                    .pointsUsed(0L)
                    .lastUpdatedAt(VietNamTime.dateNow())
                    .build());
        }
        Long newPoint = myRewardPoint.getPoint() + point;
        myRewardPoint.setPoint(newPoint);
        myRewardPoint.setLastUpdatedAt(VietNamTime.dateNow());
        entityManager.merge(myRewardPoint);
        RewardHistory rewardHistory = RewardHistory.builder()
                .content(message)
                .pointsUsed(point)
                .rewardPoint(myRewardPoint)
                .build();
        return rewardHistoryRepository.save(rewardHistory);
    }

    @Transactional
    @Override
    public RewardHistory usePoint(Long userId, Long rewardId, String message) {
        RewardPoint myRewardPoint = rewardPointRepository.findById(userId).orElse(null);
        if (myRewardPoint == null) {
            throw ServiceException.builder()
                    .message("Không tìm thấy điểm thưởng")
                    .clazz(RewardHistoryServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        Reward reward = rewardRepository.findByIdAndDeletedAtIsNullAndIsOpened(rewardId, true);
        if (reward == null) {
            throw ServiceException.builder()
                    .message("Không tìm thấy phần thưởng")
                    .clazz(RewardHistoryServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        if (myRewardPoint.getPoint() < reward.getPoints()) {
            throw ServiceException.builder()
                    .message("Điểm thưởng không đủ để đổi giá trị phần quà")
                    .clazz(RewardHistoryServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Long newPoint = myRewardPoint.getPoint() - reward.getPoints();
        Long newPointUsed = myRewardPoint.getPointsUsed() + reward.getPoints();
        myRewardPoint.setPoint(newPoint);
        myRewardPoint.setPointsUsed(newPointUsed);
        myRewardPoint.setLastUpdatedAt(VietNamTime.dateNow());
        entityManager.merge(myRewardPoint);

        RewardHistory rewardHistory = RewardHistory.builder()
                .content(message)
                .pointsUsed(-reward.getPoints())
                .reward(reward)
                .rewardPoint(myRewardPoint)
                .build();
        return rewardHistoryRepository.save(rewardHistory);
    }

    @Override
    public MyRewardHistoriesResponse getMyRewardHistories(Long userId) {
        RewardPoint myRewardPoint = rewardPointRepository.findById(userId).orElse(null);
        if (myRewardPoint == null) {
            throw ServiceException.builder()
                    .message("Không tìm thấy điểm thưởng")
                    .clazz(RewardHistoryServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        myRewardPoint.getRewardHistories().sort(Comparator.comparing(RewardHistory::getId).reversed());
        return new MyRewardHistoriesResponse(
                myRewardPoint,
                myRewardPoint
                        .getRewardHistories()
                        .stream()
                        .map(RewardHistoryMapper::toRewardHistoryResponse)
                        .toList());
    }
}
