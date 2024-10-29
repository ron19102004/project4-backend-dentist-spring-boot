package com.hospital.app.mailer;

import com.hospital.app.entities.account.User;

import java.util.Map;

public interface MailerService {
    void sendOneTimePassword(final User user, final String otpCode);
    void sendWelcomeEmail(final User user);
    void requestResetPassword(final User user,final String token,final Map<String,Object> claims);
}
