package com.hospital.core.auth.futures.login;

import com.hospital.core.dto.auth.LoginRequest;
import com.hospital.core.entities.account.User;
import com.hospital.exception.AuthenticationException;
import com.hospital.core.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

public class UsernamePasswordLoginFutureStrategy implements LoginFuture {
    @Override
    public LoginResponseData authenticate(DaoAuthenticationProvider authenticationProvider,
                                          UserRepository userRepository,
                                          LoginRequest loginRequest) throws AuthenticationException {
        User user = userRepository.findByUsername(loginRequest.username()).orElse(null);
        if (user == null) {
            throw new AuthenticationException("Không tìm thấy người dùng.Vui lòng kiểm tra lại tên tài khoản");
        }
        Authentication authentication = authenticationProvider
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.username(),
                                loginRequest.password()));
        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException("Xác thực thất bại. Mật khẩu không chính xác");
        }
        return new LoginResponseData(authentication, user);
    }
}
