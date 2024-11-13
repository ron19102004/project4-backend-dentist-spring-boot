package com.hospital.app.auth;

import com.hospital.app.dto.auth.RegisterRequest;
import com.hospital.app.dto.auth.TokenResponse;
import com.hospital.app.entities.account.Role;
import com.hospital.app.entities.account.User;
import com.hospital.app.entities.reward.RewardHistory;
import com.hospital.app.entities.reward.RewardPoint;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.jwt.JwtCreateTokenDTO;
import com.hospital.app.jwt.JwtUtils;
import com.hospital.app.jwt.TokenDTO;
import com.hospital.app.mailer.MailerService;
import com.hospital.app.repositories.UserRepository;
import com.hospital.app.services.RewardHistoryService;
import com.hospital.app.services.TokenService;
import com.hospital.app.services.impls.AccountantServiceImpl;
import com.hospital.app.utils.PWUtil;
import com.hospital.app.utils.VietNamTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailerService mailerService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RewardHistoryService rewardHistoryService;
    @PersistenceContext
    private EntityManager entityManager;
    private static final long TWO_FACTOR_AUTHENTICATION_EXPIRED_TIME = 1;
    private static final int OTP_LENGTH = 6;
    private static final long POINT_WHEN_REGISTER_ACCOUNT = 10;

    @Transactional
    @Override
    public void resetPasswordRequest(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw ServiceException.builder()
                    .message("Tài khoản người dùng không tồn tại")
                    .clazz(AuthServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        String password = PWUtil.generatePassword(10);
        Map<String, Object> claims = new HashMap<>();
        claims.put("password", password);
        String token = jwtUtils.encodeToken(TokenDTO.builder()
                .claims(claims)
                .subject(email)
                .build(), 5);
        user.setTokenResetPassword(token);
        this.entityManager.merge(user);
        mailerService.sendResetPasswordRequest(user, token);
    }

    @Transactional
    @Override
    public void resetPasswordHandle(String token) {
        TokenDTO tokenDTO = jwtUtils.decodeToken(token);
        User user = userRepository.findByEmail(tokenDTO.subject()).orElse(null);
        if (user == null) {
            throw ServiceException.builder()
                    .message("Tài khoản người dùng không tồn tại")
                    .clazz(AuthServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        if (user.getTokenResetPassword() == null) {
            throw ServiceException.builder()
                    .clazz(AuthServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .message("Không tìm thấy token khôi phục")
                    .build();
        }
        if (!user.getTokenResetPassword().equals(token)) {
            throw ServiceException.builder()
                    .clazz(AuthServiceImpl.class)
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("Token không hợp lệ")
                    .build();
        }
        String password = (String) tokenDTO.claims().get("password");
        user.setPassword(passwordEncoder.encode(password));
        user.setTokenResetPassword(null);
        this.entityManager.merge(user);
        this.mailerService.sendResetPasswordSuccess(user, password);
    }

    @Transactional
    @Override
    public TokenResponse generateLoginToken(Authentication authentication, User user, String userAgent) {
        if (user.isActiveTwoFactorAuthentication()) {
            User userDb = userRepository.findByEmail(user.getEmail()).orElse(user);
            String codeTFA = PWUtil.generateOTP(OTP_LENGTH);
            Instant now = VietNamTime.instantNow();
            userDb.setCodeTwoFactorAuthentication(codeTFA);
            userDb.setCodeTFAExpirationAt(Date.from(now.plus(TWO_FACTOR_AUTHENTICATION_EXPIRED_TIME, ChronoUnit.MINUTES)));
            this.entityManager.merge(userDb);
            Map<String, Object> claims = new HashMap<>();
            claims.put("code", codeTFA);
            String tokenToVerifyOTP = jwtUtils.encodeToken(
                    new TokenDTO(userDb.getId().toString(), claims),
                    TWO_FACTOR_AUTHENTICATION_EXPIRED_TIME
            );
            this.mailerService.sendOneTimePassword(user, codeTFA, now);
            return TokenResponse.builder()
                    .accessToken(tokenToVerifyOTP)
                    .refreshToken(null)
                    .user(User.builder()
                            .email(user.getEmail())
                            .fullName(user.getFullName())
                            .isActiveTwoFactorAuthentication(user.isActiveTwoFactorAuthentication())
                            .build())
                    .build();
        }
        JwtCreateTokenDTO jwtCreateTokenDTO = jwtUtils.createToken(authentication);
        this.saveToken(jwtCreateTokenDTO, userAgent);
        return TokenResponse.builder()
                .accessToken(jwtCreateTokenDTO.jwtAccessToken().getTokenValue())
                .refreshToken(jwtCreateTokenDTO.refreshToken())
                .user(user)
                .build();
    }

    @Transactional
    @Override
    public TokenResponse verifyOTP(String token, String otpCode, String userAgent) {
        TokenDTO tokenDTO = jwtUtils.decodeToken(token);
        Map<String, Object> claims = tokenDTO.claims();
        String codeTFA = (String) claims.get("code");
        if (!codeTFA.equals(otpCode)) {
            throw ServiceException.builder()
                    .message("Mã xác thực và token không khớp")
                    .clazz(AuthServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        User user = userRepository.findById(Long.parseLong(tokenDTO.subject())).orElse(null);
        if (user == null) {
            throw ServiceException.builder()
                    .message("Tài khoản người dùng không tồn tại")
                    .clazz(AuthServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        if (user.getCodeTwoFactorAuthentication() == null) {
            throw ServiceException.builder()
                    .message("Mã xác thực không tồn tại")
                    .clazz(AuthServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        if (!user.getCodeTwoFactorAuthentication().equals(otpCode)) {
            throw ServiceException.builder()
                    .message("Mã xác thực không hợp lệ")
                    .clazz(AuthServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if (user.getCodeTFAExpirationAt().before(VietNamTime.dateNow())) {
            throw ServiceException.builder()
                    .message("Mã xác thực đã hết hạn")
                    .clazz(AuthServiceImpl.class)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        user.setCodeTFAExpirationAt(null);
        user.setCodeTwoFactorAuthentication(null);
        this.entityManager.merge(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        JwtCreateTokenDTO jwtCreateTokenDTO = jwtUtils.createToken(authentication);
        this.saveToken(jwtCreateTokenDTO, userAgent);
        return TokenResponse.builder()
                .accessToken(jwtCreateTokenDTO.jwtAccessToken().getTokenValue())
                .refreshToken(jwtCreateTokenDTO.refreshToken())
                .user(user)
                .build();
    }

    @Transactional
    @Override
    public void changeTFAStatus(User user) {
        User userDb = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (userDb == null) {
            throw ServiceException.builder()
                    .message("Tài khoản người dùng không tồn tại")
                    .clazz(AuthServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        userDb.setActiveTwoFactorAuthentication(!user.isActiveTwoFactorAuthentication());
        this.entityManager.merge(userDb);
    }

    public void saveToken(JwtCreateTokenDTO jwtCreateTokenDTO, String userAgent) {
        boolean isMobile = userAgent.equalsIgnoreCase("mobile");
        this.tokenService.saveToken(jwtCreateTokenDTO, isMobile);
    }

    @Override
    public void refreshToken(String accessTokenOld, JwtCreateTokenDTO jwtCreateTokenDTO, String userAgent) {
        this.tokenService.deleteTokenByAccessToken(accessTokenOld);
        this.saveToken(jwtCreateTokenDTO, userAgent);
    }

    @Override
    public User register(RegisterRequest registerRequest) {
        boolean userNameFound = this.userRepository.findByUsername(registerRequest.username()).isPresent();
        if (userNameFound) {
            throw ServiceException.builder()
                    .clazz(AuthServiceImpl.class)
                    .message("Tên đăng nhập đã tồn tại")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        boolean emailFound = this.userRepository.findByEmail(registerRequest.email()).isPresent();
        if (emailFound) {
            throw ServiceException.builder()
                    .clazz(AuthServiceImpl.class)
                    .message("Email đã tồn tại")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        boolean phoneNumberFound = this.userRepository.findByPhoneNumber(registerRequest.phoneNumber()).isPresent();
        if (phoneNumberFound) {
            throw ServiceException.builder()
                    .clazz(AuthServiceImpl.class)
                    .message("Số điện thoại đã tồn tại")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        User user = this.userRepository.save(User.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .phoneNumber(registerRequest.phoneNumber())
                .gender(registerRequest.gender())
                .address(registerRequest.address())
                .role(Role.PATIENT)
                .fullName(registerRequest.fullName())
                .password(passwordEncoder.encode(registerRequest.password()))
                .isActiveTwoFactorAuthentication(false)
                .build());

        RewardHistory rewardHistory = this.rewardHistoryService.plusPoint(
                user.getId(),
                POINT_WHEN_REGISTER_ACCOUNT,
                "Chúc mừng " + user.getFullName() + "! Bạn đã đăng ký tài khoản thành công." +
                        " Chúng tôi xin chúc bạn nhiều sức khoẻ và những trải nghiệm tuyệt vời cùng chúng tôi." +
                        " Cảm ơn bạn đã lựa chọn và tin tưởng!");
        user.setRewardPoint(rewardHistory.getRewardPoint());
        mailerService.sendWelcomeEmail(user);
        return user;
    }
}
