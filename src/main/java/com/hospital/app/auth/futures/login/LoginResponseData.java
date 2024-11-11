package com.hospital.app.auth.futures.login;

import com.hospital.app.entities.account.User;
import org.springframework.security.core.Authentication;

public record LoginResponseData(
        Authentication authentication,
        User user
) {
}
