package com.hospital.core.controllers;

import com.hospital.core.annotations.HasRole;
import com.hospital.core.annotations.WithRateLimitIPAddress;
import com.hospital.core.dto.reward.RewardCreateRequest;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.reward.Reward;
import com.hospital.core.services.RewardService;
import com.hospital.infrastructure.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards/v1")
public class RewardController {
    @Autowired
    private RewardService rewardService;

    @GetMapping("/all")
    @WithRateLimitIPAddress(duration = 15000,limit = 5)
    public ResponseEntity<ResponseLayout<List<Reward>>> getAllRewards() {
        return ResponseEntity.ok(ResponseLayout
                .<List<Reward>>builder()
                .data(this.rewardService.getAll())
                .success(true)
                .message("Lấy tất thông tin phần quà thành công")
                .build());
    }

    @GetMapping("/{id}")
    @WithRateLimitIPAddress(duration = 15000,limit = 5)
    public ResponseEntity<ResponseLayout<Reward>> getRewardById(@PathVariable("id") Long id) {
        Reward reward = this.rewardService.getById(id);
        return ResponseEntity.ok(ResponseLayout
                .<Reward>builder()
                .message(reward != null ? "Lấy thành công" : "Không tìm thấy phần quà")
                .success(reward != null)
                .data(reward)
                .build());
    }

    @PostMapping("/new")
    @HasRole(roles = {Role.ACCOUNTANT})
    public ResponseEntity<ResponseLayout<Reward>> createReward(@RequestBody RewardCreateRequest rewardCreateRequest) {
        return ResponseEntity.ok(ResponseLayout
                .<Reward>builder()
                .data(this.rewardService.create(rewardCreateRequest))
                .success(true)
                .message("Tạo mới thành công")
                .build());
    }

    @DeleteMapping("/{id}")
    @HasRole(roles = {Role.ACCOUNTANT})
    @WithRateLimitIPAddress(duration = 10000,limit = 5)
    public ResponseEntity<ResponseLayout<Object>> deleteReward(@PathVariable("id") Long id) {
        this.rewardService.delete(id);
        return ResponseEntity.ok(ResponseLayout
                .builder()
                .message("Xóa thành công")
                .success(true)
                .build());
    }
}
