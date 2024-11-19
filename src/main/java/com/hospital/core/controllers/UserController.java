package com.hospital.core.controllers;

import com.hospital.core.annotations.HasRole;
import com.hospital.core.annotations.WithRateLimitIPAddress;
import com.hospital.core.dto.account.AccountantDentistCreateRequest;
import com.hospital.core.dto.account.CheckUserExistResponse;
import com.hospital.core.dto.account.UserChangeRoleRequest;
import com.hospital.core.dto.account.UserDetailsForAdminResponse;
import com.hospital.core.dto.reward_history.MyRewardHistoriesResponse;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.account.User;
import com.hospital.core.services.AccountantService;
import com.hospital.core.services.DentistService;
import com.hospital.core.services.RewardHistoryService;
import com.hospital.core.services.UserService;
import com.hospital.infrastructure.utils.ResponseLayout;
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
    private final UserService userService;
    private final DentistService dentistService;
    private final AccountantService accountantService;
    private final RewardHistoryService rewardHistoryService;

    @Autowired
    public UserController(UserService userService,
                          DentistService dentistService,
                          AccountantService accountantService,
                          RewardHistoryService rewardHistoryService) {
        this.userService = userService;
        this.dentistService = dentistService;
        this.accountantService = accountantService;
        this.rewardHistoryService = rewardHistoryService;
    }

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

    @HasRole(roles = Role.ADMIN)
    @GetMapping("/admin/all-user-has-role")
    public ResponseEntity<ResponseLayout<List<UserDetailsForAdminResponse>>> getAllUserHasRole(
            @RequestParam("pageNumber") int pageNumber
    ) {
        return ResponseEntity.ok(ResponseLayout
                .<List<UserDetailsForAdminResponse>>builder()
                .message("Lấy danh sách thành công")
                .success(true)
                .data(userService.getAllUsersHasRole(pageNumber))
                .build());
    }

    @HasRole(roles = Role.ADMIN)
    @GetMapping("/admin/check-user-exist/{id}")
    public ResponseEntity<ResponseLayout<CheckUserExistResponse>> checkUserExist(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ResponseLayout.<CheckUserExistResponse>builder()
                .message("Truy vấn thành công")
                .success(true)
                .data(userService.checkUserExist(id))
                .build());
    }

}
