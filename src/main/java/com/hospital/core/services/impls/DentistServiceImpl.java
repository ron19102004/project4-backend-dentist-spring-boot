package com.hospital.core.services.impls;

import com.hospital.core.dto.account.AccountantDentistCreateRequest;
import com.hospital.core.dto.dentist.DentistResponse;
import com.hospital.core.entities.account.Dentist;
import com.hospital.core.entities.account.Role;
import com.hospital.core.entities.account.Specialize;
import com.hospital.core.entities.account.User;
import com.hospital.core.mappers.DentistMapper;
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

import java.util.List;

@Service
public class DentistServiceImpl implements DentistService {
    private final DentistRepository dentistRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final UserService userService;
    private final SpecializeService specializeService;

    @Autowired
    public DentistServiceImpl(DentistRepository dentistRepository, SpecializeService specializeService, UserService userService) {
        this.dentistRepository = dentistRepository;
        this.specializeService = specializeService;
        this.userService = userService;
    }

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
        Dentist dentist = this.dentistRepository.findById(requestDto.userId()).orElse(null);
        user.setRole(Role.DENTIST);
        Specialize specialize = this.specializeService.getByIdNormal(requestDto.specializeId());
        if (specialize == null) {
            throw ServiceException.builder()
                    .message("Không tìm thấy chuyên ngành")
                    .clazz(DentistServiceImpl.class)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        if (dentist != null) {
            Dentist denFromReq = AccountMapper.toDentist(requestDto, user, specialize);
            dentist.setSpecialize(denFromReq.getSpecialize());
            dentist.setEmail(denFromReq.getEmail());
            dentist.setPhoneNumber(dentist.getPhoneNumber());
            dentist.setDescription(denFromReq.getDescription());
            return dentist;
        }
        return this.dentistRepository.save(AccountMapper.toDentist(requestDto, user, specialize));
    }

    @Override
    public Dentist findById(Long id) {
        return dentistRepository.findByIdWithRole(id, Role.DENTIST);
    }

    @Override
    public List<DentistResponse> findAll() {
        return dentistRepository
                .findAllWithRoleRole(Role.DENTIST)
                .stream()
                .map(DentistMapper::toDentistResponse)
                .toList();
    }
}
