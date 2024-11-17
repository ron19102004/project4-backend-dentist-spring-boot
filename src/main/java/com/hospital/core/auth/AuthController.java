package com.hospital.core.auth;

import com.hospital.core.annotations.HasRole;
import com.hospital.core.annotations.WithRateLimitIPAddress;
import com.hospital.core.auth.futures.login.LoginFuture;
import com.hospital.core.auth.futures.login.LoginFutureFactory;
import com.hospital.core.auth.futures.login.LoginFutureFactoryImpl;
import com.hospital.core.auth.futures.login.LoginResponseData;
import com.hospital.core.dto.auth.LoginRequest;
import com.hospital.core.dto.auth.RefreshTokenRequest;
import com.hospital.core.dto.auth.RegisterRequest;
import com.hospital.core.dto.auth.TokenResponse;
import com.hospital.core.entities.account.User;
import com.hospital.exception.ServiceException;
import com.hospital.core.repositories.UserRepository;
import com.hospital.infrastructure.utils.ResponseLayout;
import com.hospital.infrastructure.jwt.JwtCreateTokenDTO;
import com.hospital.infrastructure.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final JwtUtils jwtUtils;
    private final DaoAuthenticationProvider authenticationProvider;
    private final JwtAuthenticationProvider jwtRefreshTokenAuthProvider;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(JwtUtils jwtUtils,
                          DaoAuthenticationProvider authenticationProvider,
                          @Qualifier("jwtRefreshTokenAuthProvider")
                          JwtAuthenticationProvider jwtRefreshTokenAuthProvider,
                          AuthService authService,
                          UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.authenticationProvider = authenticationProvider;
        this.jwtRefreshTokenAuthProvider = jwtRefreshTokenAuthProvider;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/reset-password")
    @WithRateLimitIPAddress(limit = 1, duration = 5 * 60000)
    public ResponseEntity<ResponseLayout<?>> forgotPassword(@RequestParam String email) {
        this.authService.resetPasswordRequest(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-reset-password")
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
    @WithRateLimitIPAddress(limit = 1, duration = 15000)
    public ResponseEntity<ResponseLayout<TokenResponse>> login(
            @RequestHeader(value = "User-Agent") String userAgent,
            @RequestBody LoginRequest loginRequest) {
        LoginFutureFactory loginFutureFactory = LoginFutureFactoryImpl.getInstance();
        LoginFuture loginFuture = loginFutureFactory.get(loginRequest);
        LoginResponseData loginResponseData = loginFuture.authenticate(authenticationProvider, userRepository, loginRequest);
        TokenResponse tokenResponse = this.authService.generateLoginToken(
                loginResponseData.authentication(),
                loginResponseData.user(),
                userAgent);
        return ResponseEntity.ok(ResponseLayout.<TokenResponse>builder()
                .message("Đăng nhập thành công")
                .success(true)
                .data(tokenResponse)
                .build());
    }

    @PostMapping("/verify-otp")
    @WithRateLimitIPAddress(limit = 1, duration = 15000)
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
    @HasRole(justCheckAuthentication = true)
    @WithRateLimitIPAddress(limit = 10)
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
