package com.hospital.app.utils;

public final class PreAuthUtil {
    public static final String HAS_ADMIN_AUTHORITY = "hasAuthority('ADMIN')";
    public static final String HAS_AUTHENTICATED = "hasAnyAuthority('ADMIN','ACCOUNTANT','DENTIST','PATIENT')";
    public static final String HAS_ACCOUNTANT_AUTHORITY = "hasAuthority('ACCOUNTANT')";
    public static final String HAS_PATIENT_AUTHORITY = "hasAuthority('PATIENT')";
    public static final String HAS_DENTIST_AUTHORITY = "hasAuthority('DENTIST')";
    public static final String HAS_DENTIST_PATIENT_AUTHORITY = "hasAnyAuthority('DENTIST','PATIENT')";
}
