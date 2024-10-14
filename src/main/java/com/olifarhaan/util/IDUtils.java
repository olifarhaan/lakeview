package com.olifarhaan.util;

import java.util.UUID;

public class IDUtils {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}