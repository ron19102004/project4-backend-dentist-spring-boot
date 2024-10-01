package com.hospital.app.mailer;

import com.hospital.app.entities.User;

public interface MailerService {
    void sendOneTimePassword(User user, String otpCode);
}
