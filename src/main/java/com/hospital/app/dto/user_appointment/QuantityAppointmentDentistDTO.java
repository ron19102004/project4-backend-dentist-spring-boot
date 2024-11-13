package com.hospital.app.dto.user_appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class QuantityAppointmentDentistDTO {
    private Date appointmentDate;
    private Long quantity;
}
