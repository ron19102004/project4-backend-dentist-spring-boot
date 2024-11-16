package com.hospital.infrastructure.mailer;

import com.hospital.core.entities.account.User;

import java.time.Instant;

public interface MailerService {
    void sendOneTimePassword(final User user, final String otpCode,final Instant timeSent);
    void sendWelcomeEmail(final User user);
    void sendResetPasswordRequest(final User user,final String token);
    void sendResetPasswordSuccess(final User user,final String password);
}
