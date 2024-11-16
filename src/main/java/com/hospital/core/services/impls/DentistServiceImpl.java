package com.hospital.core.services.impls;

import com.hospital.core.dto.account.AccountantDentistCreateRequest;
import com.hospital.core.entities.account.Dentist;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.account.Specialize;
import com.hospital.core.entities.account.User;
import com.hospital.exception.ServiceException;
import com.hospital.core.mappers.AccountMapper;
import com.hospital.core.repositories.DentistRepository;
import com.hospital.core.services.DentistService;
import com.hospital.core.services.SpecializeService;
import com.hospital.core.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DentistServiceImpl implements DentistService {
    @Autowired
    private DentistRepository dentistRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserService userService;
    @Autowired
    private SpecializeService specializeService;

    @Transactional
    @Override
    public Dentist createAdvanceAccount(final AccountantDentistCreateRequest requestDto) {
        User user = this.userService.findById(requestDto.userId());
        if (user == null) {
            throw ServiceException.builder()
                    .message("Tài khoản người dùng không tồn tại")
                    .clazz(DentistServiceImpl.class)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        boolean isPresent = this.dentistRepository.findById(requestDto.userId()).isPresent();
        if (isPresent) {
            throw ServiceException.builder()
                    .message("Tài khoản nha sĩ đã được thiết lập")
                    .clazz(DentistServiceImpl.class)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        Specialize specialize = this.specializeService.getByIdNormal(requestDto.specializeId());
        if (specialize == null) {
            throw ServiceException.builder()
                    .message("Không tìm thấy chuyên ngành")
                    .clazz(DentistServiceImpl.class)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        user.setRole(Role.DENTIST);
        this.entityManager.merge(user);
        return this.dentistRepository.save(AccountMapper.toDentist(requestDto, user, specialize));
    }
}
