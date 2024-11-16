package com.hospital.core.mappers;

import com.hospital.core.dto.dental_record.DentalRecordUpdate;
import com.hospital.core.entities.work.DentalRecord;
import com.hospital.infrastructure.utils.VietNamTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DentistAppointmentMapper {
    public DentalRecord toDentalRecord(DentalRecordUpdate dentalRecordUpdate) {
        return DentalRecord.builder()
                .diagnosis(dentalRecordUpdate.diagnosis())
                .treatment(dentalRecordUpdate.treatment())
                .examinationDate(VietNamTime.dateNow())
                .notes(dentalRecordUpdate.notes())
                .build();
    }
}
