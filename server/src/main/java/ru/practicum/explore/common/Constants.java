package ru.practicum.explore.common;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);

    public static final DateTimeFormatter getDefaultFormatter() {
        return DEFAULT_DATETIME_FORMATTER;
    }
}
