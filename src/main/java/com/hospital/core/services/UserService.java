package com.hospital.core.services;

import com.hospital.core.dto.account.CheckUserExistResponse;
import com.hospital.core.dto.account.UserChangeRoleRequest;
import com.hospital.core.dto.account.UserDetailsForAdminResponse;
import com.hospital.core.entities.account.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findById(final Long id);
    User changeRole(final UserChangeRoleRequest userChangeRoleRequest);
    void resetRole(final Long id);
    List<UserDetailsForAdminResponse> getAllUsersHasRole(int pageNumber);
    CheckUserExistResponse checkUserExist(final Long id);
}
