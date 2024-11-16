package com.hospital.core.auth.futures.login;

import com.hospital.core.entities.account.User;
import org.springframework.security.core.Authentication;

public record LoginResponseData(
        Authentication authentication,
        User user
) {
}
