package com.payment.helpers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class DateUtils {
    public static String toIsoUtcStart(String date) {
        // Parse the input date (yyyy-MM-dd)
        LocalDate localDate = LocalDate.parse(date);

        // Convert to start of day in UTC
        return localDate.atStartOfDay(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);
    }

    public static String toIsoUtcEnd(String date) {
        // Parse the input date (yyyy-MM-dd)
        LocalDate localDate = LocalDate.parse(date);

        // Convert to end of day in UTC (23:59:59)
        return localDate.atTime(23, 59, 59)
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);
    }

    public static String toIsoUtc(String date) {
        LocalDate localDate = LocalDate.parse(date); // parse yyyy-MM-dd
        return localDate.atStartOfDay(ZoneOffset.UTC) // midnight UTC
                .format(DateTimeFormatter.ISO_INSTANT);
    }

    public static String parseToIsoUtc(String dateTimeWithZone) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .parseLenient()
                .appendPattern("EEE, dd MMM yyyy HH:mm:ss z")
                .toFormatter(Locale.ENGLISH);

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeWithZone, formatter);

        // Convert to UTC
        return zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ISO_INSTANT);
    }
}
