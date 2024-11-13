package com.hospital.app.services.impls;

import com.hospital.app.dto.account.AccountantDentistCreateRequest;
import com.hospital.app.entities.account.Accountant;
import com.hospital.app.entities.account.Role;
import com.hospital.app.entities.account.User;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.mappers.AccountMapper;
import com.hospital.app.repositories.AccountantRepository;
import com.hospital.app.services.AccountantService;
import com.hospital.app.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AccountantServiceImpl implements AccountantService {
    @Autowired
    private AccountantRepository accountantRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserService userService;

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
