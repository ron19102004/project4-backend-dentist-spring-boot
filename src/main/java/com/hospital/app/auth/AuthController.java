package com.hospital.app.auth;

import com.hospital.app.dto.auth.LoginRequest;
import com.hospital.app.dto.auth.RefreshTokenRequest;
import com.hospital.app.dto.auth.RegisterRequest;
import com.hospital.app.dto.auth.TokenResponse;
import com.hospital.app.entities.account.User;
import com.hospital.app.utils.ResponseLayout;
import com.hospital.app.jwt.JwtCreateTokenDTO;
import com.hospital.app.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<ResponseLayout<TokenResponse>> login(
            @RequestHeader(value = "User-Agent") String userAgent,
            @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationProvider
                .authenticate(UsernamePasswordAuthenticationToken
                        .unauthenticated(loginRequest.username(), loginRequest.password()));
        if (authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            JwtCreateTokenDTO jwtCreateTokenDTO = jwtUtils.createToken(authentication);
            this.authService.saveToken(jwtCreateTokenDTO, userAgent);
            return ResponseEntity.ok(ResponseLayout.<TokenResponse>builder()
                    .message("Đăng nhập thành công")
                    .success(true)
                    .data(TokenResponse.builder()
                            .accessToken(jwtCreateTokenDTO.jwtAccessToken().getTokenValue())
                            .refreshToken(jwtCreateTokenDTO.refreshToken())
                            .user(user)
                            .build())
                    .build());
        }
        return ResponseEntity.ok(ResponseLayout.<TokenResponse>builder()
                .message("Đăng nhập thất bại")
                .success(false)
                .data(null)
                .build());
    }

    @PostMapping("/refreshToken")
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
