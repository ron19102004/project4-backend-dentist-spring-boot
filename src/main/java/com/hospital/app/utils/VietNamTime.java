package com.hospital.app.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class VietNamTime {
    public static Instant instantNow() {
        LocalDateTime vietnamDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return vietnamDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
    }

    public static Date dateNow() {
        LocalDateTime vietnamDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return Date.from(vietnamDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
    }
    public static String toStringFormated(Instant instant){
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        return zonedDateTime.format(formatter);
    }
}
