package com.hospital.core.services.impls;

import com.hospital.core.dto.reward_history.MyRewardHistoriesResponse;
import com.hospital.core.entities.account.User;
import com.hospital.core.entities.reward.Reward;
import com.hospital.core.entities.reward.RewardHistory;
import com.hospital.core.entities.reward.RewardPoint;
import com.hospital.exception.ServiceException;
import com.hospital.core.mappers.RewardHistoryMapper;
import com.hospital.core.repositories.RewardHistoryRepository;
import com.hospital.core.repositories.RewardPointRepository;
import com.hospital.core.repositories.RewardRepository;
import com.hospital.core.repositories.UserRepository;
import com.hospital.core.services.RewardHistoryService;
import com.hospital.infrastructure.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class RewardHistoryServiceImpl implements RewardHistoryService {
    private final RewardHistoryRepository rewardHistoryRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final RewardPointRepository rewardPointRepository;
    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;
    @Autowired
    public RewardHistoryServiceImpl(RewardHistoryRepository rewardHistoryRepository,
                                    RewardPointRepository rewardPointRepository,
                                    UserRepository userRepository,
                                    RewardRepository rewardRepository) {
        this.rewardHistoryRepository = rewardHistoryRepository;
        this.rewardPointRepository = rewardPointRepository;
        this.userRepository = userRepository;
        this.rewardRepository = rewardRepository;
    }

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
        RewardHistory rewardHistory = RewardHistory.builder()
                .content(message)
                .pointsUsed(point)
                .rewardPoint(myRewardPoint)
                .build();
        return rewardHistoryRepository.save(rewardHistory);
    }

    @Transactional
    @Override
    public RewardHistory usePoint(Long userId, Long rewardId) {
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

        StringBuilder message = new StringBuilder();
        message.append("Cảm ơn ")
                .append(myRewardPoint.getUser().getFullName())
                .append(" đã sử dụng dịch vụ của chúng tôi.")
                .append("Quý khách đã quy đổi ")
                .append(reward.getPoints())
                .append(" điểm để lấy dịch vụ : ")
                .append(reward.getContent());
        RewardHistory rewardHistory = RewardHistory.builder()
                .content(message.toString())
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
