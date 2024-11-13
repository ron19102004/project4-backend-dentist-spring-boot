package com.hospital.app.dto.appointment;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class AppointmentByDentistIdInDateResponse {
    private List<AppointmentDTO> appointments;
    private long size;
}
