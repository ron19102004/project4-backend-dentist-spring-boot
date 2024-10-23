package com.hospital.app.mailer;

import com.hospital.app.entities.account.User;

public interface MailerService {
    void sendOneTimePassword(User user, String otpCode);
}
