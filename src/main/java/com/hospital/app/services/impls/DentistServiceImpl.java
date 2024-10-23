package com.hospital.app.services.impls;

import com.hospital.app.dto.account.AccountantDentistCreateRequest;
import com.hospital.app.entities.account.Dentist;
import com.hospital.app.entities.account.Specialize;
import com.hospital.app.entities.account.User;
import com.hospital.app.exception.ServiceException;
import com.hospital.app.repositories.DentistRepository;
import com.hospital.app.services.DentistService;
import com.hospital.app.services.SpecializeService;
import com.hospital.app.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @Override
    public Dentist createAdvanceAccount(AccountantDentistCreateRequest requestDto) {
        User user = this.userService.findById(requestDto.userId());
        if (user == null) {
            throw ServiceException.builder()
                    .message("Tài khoản người dùng không tồn tại")
                    .clazz(AccountantServiceImpl.class)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        boolean isPresent = this.dentistRepository.findById(requestDto.userId()).isPresent();
        if (isPresent) {
            throw ServiceException.builder()
                    .message("Tài khoản nha sĩ đã được thiết lập")
                    .clazz(AccountantServiceImpl.class)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        Specialize specialize = this.specializeService.getById(requestDto.specializeId());
        if (specialize == null) {
            throw ServiceException.builder()
                    .message("Không tìm thấy chuyên ngành")
                    .clazz(AccountantServiceImpl.class)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        return this.dentistRepository.save(Dentist.builder()
                .user(user)
                .phoneNumber(requestDto.phoneNumber())
                .email(requestDto.email())
                .specialize(specialize)
                .build());
    }
}
