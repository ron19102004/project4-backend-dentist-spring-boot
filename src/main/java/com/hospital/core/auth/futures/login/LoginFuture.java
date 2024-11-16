package com.hospital.core.auth.futures.login;

import com.hospital.core.dto.auth.LoginRequest;
import com.hospital.exception.AuthenticationException;
import com.hospital.core.repositories.UserRepository;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@FunctionalInterface
public interface LoginFuture {
    LoginResponseData authenticate(DaoAuthenticationProvider authenticationProvider,
                                   UserRepository userRepository,
                                   LoginRequest loginRequest) throws AuthenticationException;
}
