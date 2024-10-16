package com.employees.employees.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateUtil {
    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy")
    );

    public static LocalDate parseDate(String date) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try{
                return date.isEmpty() ? null: LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {

            }
        }
        throw new IllegalArgumentException("Date format not supported: " + date);
    }
}
