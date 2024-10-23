package com.hospital.app.auth;

import com.hospital.app.dto.auth.RegisterRequest;
import com.hospital.app.entities.account.Role;
import com.hospital.app.entities.account.User;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.jwt.JwtCreateTokenDTO;
import com.hospital.app.jwt.JwtUtils;
import com.hospital.app.mailer.MailerService;
import com.hospital.app.repositories.UserRepository;
import com.hospital.app.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public void saveToken(JwtCreateTokenDTO jwtCreateTokenDTO, String userAgent) {
        boolean isMobile = userAgent.equalsIgnoreCase("mobile");
        this.tokenService.saveToken(jwtCreateTokenDTO,isMobile);
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
        User user = User.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .phoneNumber(registerRequest.phoneNumber())
                .gender(registerRequest.gender())
                .address(registerRequest.address())
                .role(Role.PATIENT)
                .fullName(registerRequest.fullName())
                .password(passwordEncoder.encode(registerRequest.password()))
                .build();
        return this.userRepository.save(user);
    }
}
