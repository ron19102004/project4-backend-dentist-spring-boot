package com.hospital.app.controllers;

import com.hospital.app.annotations.WithRateLimitRequest;
import com.hospital.app.dto.account.AccountantDentistCreateRequest;
import com.hospital.app.dto.account.UserChangeRoleRequest;
import com.hospital.app.entities.account.User;
import com.hospital.app.services.AccountantService;
import com.hospital.app.services.DentistService;
import com.hospital.app.services.UserService;
import com.hospital.app.utils.PreAuthUtil;
import com.hospital.app.utils.ResponseLayout;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    @PreAuthorize(PreAuthUtil.HAS_AUTHENTICATED)
    public ResponseEntity<User> getUserDetails(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PreAuthorize(PreAuthUtil.HAS_ADMIN_AUTHORITY)
    @PostMapping("/reset-role/{id}")
    @WithRateLimitRequest(limit = 10)
    public ResponseEntity<ResponseLayout<Object>> resetRole(@NotNull @PathVariable("id") Long id) {
        this.userService.resetRole(id);
        return ResponseEntity.ok(ResponseLayout.builder()
                .message("Đặt lại quyền thành công. Quyền hiện tại là người dùng")
                .success(true)
                .build());
    }

    @PreAuthorize(PreAuthUtil.HAS_ADMIN_AUTHORITY)
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

    @PreAuthorize(PreAuthUtil.HAS_ADMIN_AUTHORITY)
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
