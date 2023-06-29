package com.hidglobal.reports.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiTestConstant {
    public static final String KEY = "key";
    public static final String ATTRIBUTES = "attributes";
    public static final String DATE_TIME = "dateTime";
    public static final String CRED_HOLDER_ID = "credentialHolderId";
    public static final String CRED_HOLDER_NAME = "credentialHolderName";
    public static final String EVENT_NAME = "eventName";
    public static final DateTimeFormatter ISO_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    public static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
