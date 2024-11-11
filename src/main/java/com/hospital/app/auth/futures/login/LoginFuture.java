package com.hospital.app.auth.futures.login;

import com.hospital.app.dto.auth.LoginRequest;
import com.hospital.app.exception.AuthenticationException;
import com.hospital.app.repositories.UserRepository;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@FunctionalInterface
public interface LoginFuture {
    LoginResponseData authenticate(DaoAuthenticationProvider authenticationProvider,
                                   UserRepository userRepository,
                                   LoginRequest loginRequest) throws AuthenticationException;
}
