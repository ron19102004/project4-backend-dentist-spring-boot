package com.hospital.app.services;

import com.hospital.app.dto.account.UserChangeRoleRequest;
import com.hospital.app.entities.account.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserService extends UserDetailsService {
    User findById(final Long id);
    User changeRole(final UserChangeRoleRequest userChangeRoleRequest);
    void resetRole(final Long id);
}
