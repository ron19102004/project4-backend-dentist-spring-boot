package com.hospital.infrastructure.aspects;

import com.hospital.core.annotations.HasRole;
import com.hospital.core.entities.account.User;
import com.hospital.exception.AuthenticationException;
import com.hospital.exception.AuthorizationDeniedCustomException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Order(1)
public class HasRoleAspect {
    private boolean isAnonymousUser(Object object){
        return object instanceof String;
    }
    @Before("@annotation(hasRole)")
    public void hasRole(HasRole hasRole) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated() || isAnonymousUser(authentication.getPrincipal())) {
            throw AuthenticationException.builder()
                    .message("Xác thực người dùng không thành công")
                    .build();
        }
        if (hasRole.justCheckAuthentication()) return;
        User user = (User) authentication.getPrincipal();
        if (!Arrays.asList(hasRole.roles()).contains(user.getRole())) {
            throw AuthorizationDeniedCustomException.builder()
                    .message("Người dùng không có quyền truy cập")
                    .build();
        }
    }
}
