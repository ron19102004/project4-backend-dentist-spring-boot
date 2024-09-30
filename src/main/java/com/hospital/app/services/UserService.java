package com.hospital.app.services;

import com.hospital.app.entities.User;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserService extends UserDetailsManager {
    User findById(Long id);
}
