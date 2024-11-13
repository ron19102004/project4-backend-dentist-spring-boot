package com.hospital.app.controllers;

import com.hospital.app.annotations.HasRole;
import com.hospital.app.annotations.WithRateLimitIPAddress;
import com.hospital.app.dto.account.AccountantDentistCreateRequest;
import com.hospital.app.dto.account.UserChangeRoleRequest;
import com.hospital.app.dto.reward_history.MyRewardHistoriesResponse;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.Role;
import com.hospital.app.entities.account.User;
import com.hospital.app.repositories.DentistRepository;
import com.hospital.app.services.AccountantService;
import com.hospital.app.services.DentistService;
import com.hospital.app.services.RewardHistoryService;
import com.hospital.app.services.UserService;
import com.hospital.app.utils.ResponseLayout;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Default UserController -> UserControllerVer1
 */
@RestController
@RequestMapping("/api/users/v1")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private DentistService dentistService;
    @Autowired
    private AccountantService accountantService;
    @Autowired
    private RewardHistoryService rewardHistoryService;

    @GetMapping("/my-reward-history")
    @HasRole(justCheckAuthentication = true)
    @WithRateLimitIPAddress(limit = 20, duration = 30000)
    public ResponseEntity<ResponseLayout<MyRewardHistoriesResponse>> getMyRewardHistory(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(ResponseLayout.<MyRewardHistoriesResponse>builder()
                .data(rewardHistoryService.getMyRewardHistories(user.getId()))
                .success(true)
                .message("Lấy tất cả thành công")
                .build());
    }

    @GetMapping("/me")
    @HasRole(justCheckAuthentication = true)
    @WithRateLimitIPAddress(limit = 20, duration = 30000)
    public ResponseEntity<User> getUserDetails(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @HasRole(roles = {Role.ADMIN})
    @PostMapping("/reset-role/{id}")
    @WithRateLimitIPAddress(duration = 15000, limit = 510)
    public ResponseEntity<ResponseLayout<Object>> resetRole(@NotNull @PathVariable("id") Long id) {
        this.userService.resetRole(id);
        return ResponseEntity.ok(ResponseLayout.builder()
                .message("Đặt lại quyền thành công. Quyền hiện tại là người dùng")
                .success(true)
                .build());
    }

    @HasRole(roles = {Role.ADMIN})
    @PostMapping("/new-dentist-or-accountant")
    public ResponseEntity<ResponseLayout<Object>> createDentistOrAccountant(
            @RequestBody AccountantDentistCreateRequest accountantDentistCreateRequest) {
        if (accountantDentistCreateRequest.specializeId() == null || accountantDentistCreateRequest.specializeId() <= 0) {
            return ResponseEntity.ok(ResponseLayout.builder()
                    .data(this.accountantService.createAdvanceAccount(accountantDentistCreateRequest))
                    .success(true)
                    .message("Thiết lập thành công quyền thu ngân")
                    .build());
        }
        return ResponseEntity.ok(ResponseLayout.builder()
                .data(this.dentistService.createAdvanceAccount(accountantDentistCreateRequest))
                .success(true)
                .message("Thiết lập thành công quyền bác sĩ")
                .build());

    }

    @HasRole(roles = {Role.ADMIN})
    @PostMapping("/change-role-user")
    public ResponseEntity<ResponseLayout<User>> changeRoleUser(
            @RequestBody UserChangeRoleRequest userChangeRoleRequest) {
        return ResponseEntity.ok(ResponseLayout
                .<User>builder()
                .message("Đổi quyền thành công")
                .success(true)
                .data(this.userService.changeRole(userChangeRoleRequest))
                .build());
    }
}
