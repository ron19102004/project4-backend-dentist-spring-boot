package com.hospital.core.mappers;

import com.hospital.core.dto.dentist.DentistResponse;
import com.hospital.core.entities.account.Dentist;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DentistMapper {
    public DentistResponse toDentistResponse(Dentist dentist) {
        if (dentist == null) return null;
        return DentistResponse.builder()
                .id(dentist.getId())
                .fullName(dentist.getUser().getFullName())
                .dentist(dentist)
                .build();
    }
}
