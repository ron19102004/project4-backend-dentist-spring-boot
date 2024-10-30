package com.hospital.app.auth;

import com.hospital.app.annotations.WithRateLimitIPAddress;
import com.hospital.app.annotations.WithRateLimitRequest;
import com.hospital.app.dto.auth.LoginRequest;
import com.hospital.app.dto.auth.RefreshTokenRequest;
import com.hospital.app.dto.auth.RegisterRequest;
import com.hospital.app.dto.auth.TokenResponse;
import com.hospital.app.entities.account.User;
import com.hospital.app.exception.RateLimitException;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.jwt.TokenDTO;
import com.hospital.app.utils.PreAuthUtil;
import com.hospital.app.utils.ResponseLayout;
import com.hospital.app.jwt.JwtCreateTokenDTO;
import com.hospital.app.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.*;
/**
 * Default AuthController -> AuthControllerVer1
 */
@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private DaoAuthenticationProvider authenticationProvider;
    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    private JwtAuthenticationProvider jwtRefreshTokenAuthProvider;
    @Autowired
    private AuthService authService;

    @PostMapping("/reset-password")
    @WithRateLimitRequest
    @WithRateLimitIPAddress(limit = 1, duration = 5 * 60000)
    public ResponseEntity<ResponseLayout<?>> forgotPassword(@RequestParam String email) {
        this.authService.resetPasswordRequest(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-reset-password")
    @WithRateLimitRequest
    @WithRateLimitIPAddress(limit = 1, duration = 5000)
    public ResponseEntity<ResponseLayout<Object>> verifyNewPassword(@RequestParam String token) {
        try {
            this.authService.resetPasswordHandle(token);
        } catch (JwtValidationException jwtValidationException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseLayout.builder()
                    .success(false)
                    .message("Token xác thực hết hạn. Vui lòng yêu cầu khôi phục mật khẩu")
                    .build());
        } catch (ServiceException serviceException) {
            return ResponseEntity.status(serviceException.getStatus()).body(ResponseLayout.builder()
                    .message(serviceException.getMessage())
                    .success(false)
                    .build());
        }
        return ResponseEntity.ok(ResponseLayout.builder()
                .success(true)
                .message("Xác thực thành công. Vui lòng kiểm tra email để lấy mật khẩu")
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseLayout<User>> register(@RequestBody RegisterRequest registerRequest) {
        User user = this.authService.register(registerRequest);
        return ResponseEntity.ok(ResponseLayout.<User>builder()
                .data(user)
                .success(true)
                .message("Đăng kí thành công")
                .build());
    }

    @PostMapping("/login")
    @WithRateLimitIPAddress(limit = 1, duration = 3000)
    public ResponseEntity<ResponseLayout<TokenResponse>> login(
            @RequestHeader(value = "User-Agent") String userAgent,
            @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationProvider
                .authenticate(UsernamePasswordAuthenticationToken
                        .unauthenticated(loginRequest.username(), loginRequest.password()));
        if (authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(ResponseLayout.<TokenResponse>builder()
                    .message("Đăng nhập thành công")
                    .success(true)
                    .data(this.authService.login(authentication, user, userAgent))
                    .build());
        }
        return ResponseEntity.ok(ResponseLayout.<TokenResponse>builder()
                .message("Đăng nhập thất bại")
                .success(false)
                .data(null)
                .build());
    }

    @PostMapping("/verify-otp")
    @WithRateLimitIPAddress(limit = 1, duration = 3000)
    public ResponseEntity<ResponseLayout<TokenResponse>> verifyOTP(
            @RequestHeader(value = "User-Agent") String userAgent,
            @RequestParam("otp") String otp,
            @RequestParam("token") String token) {
        return ResponseEntity.ok(ResponseLayout.<TokenResponse>builder()
                .message("Đăng nhập thành công")
                .success(true)
                .data(this.authService.verifyOTP(token, otp, userAgent))
                .build());
    }

    @PostMapping("/change-tfa")
    @PreAuthorize(PreAuthUtil.HAS_AUTHENTICATED)
    @WithRateLimitIPAddress(limit = 1)
    public ResponseEntity<ResponseLayout<TokenResponse>> changeTFA(@AuthenticationPrincipal User user) {
        this.authService.changeTFAStatus(user);
        return ResponseEntity.ok(ResponseLayout.<TokenResponse>builder()
                .message("Thay đổi thành công")
                .success(true)
                .data(null)
                .build());
    }

    @WithRateLimitIPAddress(limit = 1)
    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseLayout<TokenResponse>> token(
            @RequestHeader(value = "User-Agent") String userAgent,
            @RequestBody RefreshTokenRequest refreshTokenRequest) {
        Authentication authentication = jwtRefreshTokenAuthProvider
                .authenticate(new BearerTokenAuthenticationToken(refreshTokenRequest.refreshToken()));
        if (authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            JwtCreateTokenDTO jwtCreateTokenDTO = jwtUtils.createToken(authentication);
            this.authService.refreshToken(refreshTokenRequest.accessToken(), jwtCreateTokenDTO, userAgent);
            return ResponseEntity.ok(ResponseLayout.<TokenResponse>builder()
                    .message("Thực hiện thành công")
                    .success(true)
                    .data(TokenResponse.builder()
                            .accessToken(jwtCreateTokenDTO.jwtAccessToken().getTokenValue())
                            .refreshToken(jwtCreateTokenDTO.refreshToken())
                            .user(user)
                            .build())
                    .build());
        }
        return ResponseEntity.ok(ResponseLayout.<TokenResponse>builder()
                .message("Thực hiện thất bại")
                .success(false)
                .data(null)
                .build());
    }
}
