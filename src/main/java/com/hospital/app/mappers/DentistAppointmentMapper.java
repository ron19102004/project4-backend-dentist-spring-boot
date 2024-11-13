package com.hospital.app.mappers;

import com.hospital.app.dto.dental_record.DentalRecordUpdate;
import com.hospital.app.entities.work.DentalRecord;
import com.hospital.app.utils.VietNamTime;
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
