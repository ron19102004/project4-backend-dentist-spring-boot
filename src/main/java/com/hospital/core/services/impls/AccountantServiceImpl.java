package com.hospital.core.services.impls;

import com.hospital.core.dto.account.AccountantDentistCreateRequest;
import com.hospital.core.entities.account.Accountant;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.account.User;
import com.hospital.exception.ServiceException;
import com.hospital.core.mappers.AccountMapper;
import com.hospital.core.repositories.AccountantRepository;
import com.hospital.core.services.AccountantService;
import com.hospital.core.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AccountantServiceImpl implements AccountantService {
    private final AccountantRepository accountantRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final UserService userService;
    @Autowired
    public AccountantServiceImpl(AccountantRepository accountantRepository, UserService userService) {
        this.accountantRepository = accountantRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Accountant createAdvanceAccount(final AccountantDentistCreateRequest requestDto) {
        User user = this.userService.findById(requestDto.userId());
        if (user == null) {
            throw ServiceException.builder()
                    .message("Tài khoản người dùng không tồn tại")
                    .clazz(AccountantServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        boolean isPresent = this.accountantRepository.findById(requestDto.userId()).isPresent();
        if (isPresent) {
            throw ServiceException.builder()
                    .message("Tài khoản thu ngân đã được thiết lập")
                    .clazz(AccountantServiceImpl.class)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        user.setRole(Role.ACCOUNTANT);
        this.entityManager.merge(user);
        return this.accountantRepository.save(AccountMapper.toAccountant(requestDto, user));
    }

}
