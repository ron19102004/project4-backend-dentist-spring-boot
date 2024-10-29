package com.hospital.app.services.impls;

import com.hospital.app.dto.account.UserChangeRoleRequest;
import com.hospital.app.entities.account.Role;
import com.hospital.app.entities.account.User;
import com.hospital.app.repositories.UserRepository;
import com.hospital.app.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User findById(final Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public User changeRole(final UserChangeRoleRequest userChangeRoleRequest) {
        User user = this.findById(userChangeRoleRequest.userId());
        if (user == null) {
            throw new UsernameNotFoundException("Người dùng không tồn tại");
        }
        user.setRole(userChangeRoleRequest.role());
        this.entityManager.merge(user);
        return user;
    }
    @Transactional
    @Override
    public void resetRole(final Long id) {
        User user = this.findById(id);
        if (user == null) {
            throw new UsernameNotFoundException("Người dùng không tồn tại");
        }
        user.setRole(Role.PATIENT);
        this.entityManager.merge(user);
    }
}
