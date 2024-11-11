package com.hospital.app.auth.futures.login;

import com.hospital.app.dto.auth.LoginRequest;
import com.hospital.app.entities.account.User;
import com.hospital.app.exception.AuthenticationException;
import com.hospital.app.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

public class PhoneNumberPasswordLoginFutureStrategy implements LoginFuture {
    @Override
    public LoginResponseData authenticate(DaoAuthenticationProvider authenticationProvider,
                                          UserRepository userRepository,
                                          LoginRequest loginRequest) throws AuthenticationException {
        User user = userRepository.findByPhoneNumber(loginRequest.username()).orElse(null);
        if (user == null) {
            throw new AuthenticationException("Không tìm thấy người dùng.Vui lòng kiểm tra lại số điện thoại");
        }
        Authentication authentication = authenticationProvider
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                loginRequest.password()));
        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException("Xác thực thất bại.Mật khẩu không đúng.");
        }
        return new LoginResponseData(authentication, user);
    }
}
