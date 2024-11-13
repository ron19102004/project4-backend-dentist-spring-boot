package com.hospital.app.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeFormatter {
    public String formatMillisecondsToHMS(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02dh:%02dm:%02ds", hours, minutes, seconds);
    }
}
