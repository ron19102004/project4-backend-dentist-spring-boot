package com.hospital.app.services;

import com.hospital.app.dto.account.UserChangeRoleRequest;
import com.hospital.app.entities.account.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserService extends UserDetailsService {
    User findById(Long id);
    User changeRole(UserChangeRoleRequest userChangeRoleRequest);
}
