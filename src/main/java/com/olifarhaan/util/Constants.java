package com.olifarhaan.util;

public class Constants {
    public static final String PHONE_REGEX = "^$|^\\d{10}$|^\\d{12}$";
    public static final String INVALID_PHONE_MESSAGE = "Phone number must be 10 digits, 2 leading digits can also be included for country code";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String INVALID_PASSWORD_MESSAGE = "Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character";
}
