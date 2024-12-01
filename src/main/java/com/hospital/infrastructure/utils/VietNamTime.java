package com.hospital.infrastructure.utils;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class VietNamTime {
    public Instant instantNow() {
        LocalDateTime vietnamDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return vietnamDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
    }

    public Date dateNow() {
        LocalDateTime vietnamDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return Date.from(vietnamDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
    }

    public String toStringFormated(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        return zonedDateTime.format(formatter);
    }
    public String toStringFormatedDay(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return zonedDateTime.format(formatter);
    }
    public LocalDate toLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public Date[] getStartAndEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Start of the day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfDay = calendar.getTime();

        // End of the day
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfDay = calendar.getTime();

        return new Date[]{startOfDay, endOfDay};
    }
}
