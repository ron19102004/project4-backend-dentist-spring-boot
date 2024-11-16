package com.hospital.core.services;

import com.hospital.core.dto.account.UserChangeRoleRequest;
import com.hospital.core.entities.account.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findById(final Long id);
    User changeRole(final UserChangeRoleRequest userChangeRoleRequest);
    void resetRole(final Long id);
}
