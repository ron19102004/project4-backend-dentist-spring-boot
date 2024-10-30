package com.hospital.app.controllers;

import com.hospital.app.dto.reward.RewardCreateRequest;
import com.hospital.app.entities.reward.Reward;
import com.hospital.app.services.RewardService;
import com.hospital.app.utils.PreAuthUtil;
import com.hospital.app.utils.ResponseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/rewards/v1")
public class RewardController {
    @Autowired
    private RewardService rewardService;

    @GetMapping("/all")
    public ResponseEntity<ResponseLayout<List<Reward>>> getAllRewards() {
        return ResponseEntity.ok(ResponseLayout
                .<List<Reward>>builder()
                .data(this.rewardService.getAll())
                .success(true)
                .message("Lấy tất thông tin phần quà thành công")
                .build());
    }

    @GetMapping("/{id}")
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
    @PreAuthorize(PreAuthUtil.HAS_ACCOUNTANT_AUTHORITY)
    public ResponseEntity<ResponseLayout<Reward>> createReward(@RequestBody RewardCreateRequest rewardCreateRequest) {
        return ResponseEntity.ok(ResponseLayout
                .<Reward>builder()
                .data(this.rewardService.create(rewardCreateRequest))
                .success(true)
                .message("Tạo mới thành công")
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(PreAuthUtil.HAS_ACCOUNTANT_AUTHORITY)
    public ResponseEntity<ResponseLayout<Object>> deleteReward(@PathVariable("id") Long id) {
        this.rewardService.delete(id);
        return ResponseEntity.ok(ResponseLayout
                .builder()
                .message("Xóa thành công")
                .success(true)
                .build());
    }
}
