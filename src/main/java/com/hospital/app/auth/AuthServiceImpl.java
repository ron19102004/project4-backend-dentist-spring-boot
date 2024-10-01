package com.hospital.app.auth;

import com.hospital.app.dto.RegisterDTO;
import com.hospital.app.helpers.ResponseLayout;
import com.hospital.app.mailer.MailerService;
import com.hospital.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private MailerService mailerService;

    @Async
    @Override
    public ResponseLayout<?> register(RegisterDTO registerDTO) {
        return null;
    }
}
