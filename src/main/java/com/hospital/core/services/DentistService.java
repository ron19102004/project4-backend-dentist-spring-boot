package com.hospital.core.services;

import com.hospital.core.dto.dentist.DentistResponse;
import com.hospital.core.entities.account.Dentist;
import com.hospital.core.services.def.ICreateAdvanceAccount;

import java.util.List;

public interface DentistService extends ICreateAdvanceAccount<Dentist> {
    List<DentistResponse> findAll();
}
