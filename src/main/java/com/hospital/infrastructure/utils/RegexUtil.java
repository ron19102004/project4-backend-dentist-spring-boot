package com.hospital.infrastructure.utils;

import java.util.regex.Pattern;

public class RegexUtil {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|vku\\.udn\\.vn)$";
    private static final String PHONE_REGEX = "^(0[1-9]{1})([0-9]{8})$";

    public static boolean isEmail(String email) {
        return Pattern.compile(EMAIL_REGEX)
                .matcher(email)
                .matches();
    }
    public static boolean PhoneNumber(String phoneNumber) {
        return Pattern.compile(PHONE_REGEX)
                .matcher(phoneNumber)
                .matches();
    }
}
