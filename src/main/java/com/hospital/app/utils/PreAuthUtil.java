package com.hospital.app.utils;

public final class PreAuthUtil {
    public static final String HAS_ADMIN_AUTHORITY = "hasAuthority('ADMIN')";
    public static final String HAS_AUTHENTICATED = "hasAnyAuthority('ADMIN','ACCOUNTANT','DENTIST','PATIENT')";
    public static final String HAS_ACCOUNTANT_AUTHORITY = "hasAuthority('ACCOUNTANT')";

}
