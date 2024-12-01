package com.hospital.core.dto.dentist;

import com.hospital.core.entities.account.Dentist;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DentistResponse {
    private Dentist dentist;
    private String fullName;
    private Long id;
}
