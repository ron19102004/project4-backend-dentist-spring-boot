package com.hospital.app.auth.futures.login;

import com.hospital.app.dto.auth.LoginRequest;
import com.hospital.app.utils.RegexUtil;

public class LoginFutureFactoryImpl implements LoginFutureFactory {
    private static EmailPasswordLoginFutureStrategy emailPasswordLoginFutureStrategy;
    private static UsernamePasswordLoginFutureStrategy usernamePasswordLoginFutureStrategy;
    private static PhoneNumberPasswordLoginFutureStrategy phoneNumberPasswordLoginFutureStrategy;
    private static LoginFutureFactoryImpl instance;
    private LoginFutureFactoryImpl() {}
    public static LoginFutureFactoryImpl getInstance() {
        if (instance == null) instance = new LoginFutureFactoryImpl();
        return instance;
    }
    private enum LoginType {
        USERNAME_PASSWORD,
        PHONE_NUMBER_PASSWORD,
        EMAIL_PASSWORD
    }
    private LoginType getType(String value) {
        if (RegexUtil.isEmail(value)) return LoginType.EMAIL_PASSWORD;
        else if (RegexUtil.PhoneNumber(value)) return LoginType.PHONE_NUMBER_PASSWORD;
        return LoginType.USERNAME_PASSWORD;
    }
    @Override
    public LoginFuture get(LoginRequest loginRequest) {
        LoginType type = getType(loginRequest.username());
        switch (type) {
            case USERNAME_PASSWORD -> {
                if (usernamePasswordLoginFutureStrategy == null) {
                    usernamePasswordLoginFutureStrategy = new UsernamePasswordLoginFutureStrategy();
                }
                return usernamePasswordLoginFutureStrategy;
            }
            case PHONE_NUMBER_PASSWORD -> {
                if (phoneNumberPasswordLoginFutureStrategy == null) {
                    phoneNumberPasswordLoginFutureStrategy = new PhoneNumberPasswordLoginFutureStrategy();
                }
                return phoneNumberPasswordLoginFutureStrategy;
            }
            case EMAIL_PASSWORD -> {
                if (emailPasswordLoginFutureStrategy == null) {
                    emailPasswordLoginFutureStrategy = new EmailPasswordLoginFutureStrategy();
                }
                return emailPasswordLoginFutureStrategy;
            }
        }
        return null;
    }
}
