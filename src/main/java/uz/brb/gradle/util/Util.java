package uz.brb.gradle.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class Util {
    public static String dateTimeFormatter(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");

        return date.toInstant()
                .atZone(ZoneId.of("UTC+5"))
                .toLocalDateTime()
                .format(formatter);
    }

    public static String localDateTimeFormatter(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");

        return localDateTime
                .atZone(ZoneId.of("UTC+5"))
                .toLocalDateTime()
                .format(formatter);
    }

    public static Date convertToLocalDateTimeToDateWithMinutesAdded(int minute) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime utcPlus5DateTime = LocalDateTime.now()
                .plusMinutes(minute)
                .atZone(zoneId)
                .withZoneSameInstant(ZoneId.of("UTC+5"));
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            String formattedDateTime = utcPlus5DateTime.format(formatter);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            return dateFormat.parse(formattedDateTime);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error("DATE_PARSING {}", e.getMessage());
            }
        }
        return Date.from(utcPlus5DateTime.toInstant());
    }

    public static Date convertToLocalDateTimeToDate() {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime utcPlus5DateTime = LocalDateTime.now()
                .atZone(zoneId)
                .withZoneSameInstant(ZoneId.of("UTC+5"));
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            String formattedDateTime = utcPlus5DateTime.format(formatter);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            return dateFormat.parse(formattedDateTime);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error("DATE_PARSING {}", e.getMessage());
            }
        }
        return Date.from(utcPlus5DateTime.toInstant());
    }

    public static String convertToLocalDateTimeToString(String pattern) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime utcPlus5DateTime = LocalDateTime.now()
                .atZone(zoneId)
                .withZoneSameInstant(ZoneId.of("UTC+5"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return utcPlus5DateTime.format(formatter);
    }

    public static String convertToLocalDateTimePlusMonthToString(String pattern, int months, int plusDays) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime utcPlus5DateTime = LocalDateTime.now()
                .plusMonths(months)
                .plusDays(plusDays)
                .atZone(zoneId)
                .withZoneSameInstant(ZoneId.of("UTC+5"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return utcPlus5DateTime.format(formatter);
    }
}
