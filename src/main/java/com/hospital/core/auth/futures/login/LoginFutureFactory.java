package com.hospital.core.auth.futures.login;

import com.hospital.core.dto.auth.LoginRequest;

@FunctionalInterface
public interface LoginFutureFactory {
    LoginFuture get( LoginRequest loginRequest);
}
