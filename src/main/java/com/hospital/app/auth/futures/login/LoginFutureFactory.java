package com.hospital.app.auth.futures.login;

import com.hospital.app.dto.auth.LoginRequest;

@FunctionalInterface
public interface LoginFutureFactory {
    LoginFuture get( LoginRequest loginRequest);
}
