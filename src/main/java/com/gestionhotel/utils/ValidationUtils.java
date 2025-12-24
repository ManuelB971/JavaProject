package main.java.com.gestionhotel.utils;

import java.time.LocalDate;
import java.util.regex.Pattern;

import main.java.com.gestionhotel.exceptions.ValidationException;

public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationUtils() {
    }

    public static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " ne peut pas être null");
        }
        return value;
    }

    public static String requireNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " ne peut pas être vide");
        }
        return value;
    }

    public static int requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " doit être > 0");
        }
        return value;
    }

    public static long requirePositive(long value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " doit être > 0");
        }
        return value;
    }

    public static double requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " doit être > 0");
        }
        return value;
    }

    public static int requireInRange(int value, int minInclusive, int maxInclusive, String fieldName) {
        if (value < minInclusive || value > maxInclusive) {
            throw new ValidationException(
                    fieldName + " doit être entre " + minInclusive + " et " + maxInclusive);
        }
        return value;
    }

    public static double requireInRange(double value, double minInclusive, double maxInclusive, String fieldName) {
        if (value < minInclusive || value > maxInclusive) {
            throw new ValidationException(
                    fieldName + " doit être entre " + minInclusive + " et " + maxInclusive);
        }
        return value;
    }

    public static boolean isEmail(String email) {
        return email != null && !email.isEmpty() && EMAIL_PATTERN.matcher(email).matches();
    }

    public static String requireValidEmail(String email, String fieldName) {
        if (!isEmail(email)) {
            throw new ValidationException(fieldName + " est invalide");
        }
        return email;
    }

    public static LocalDate requireNonNullDate(LocalDate date, String fieldName) {
        if (date == null) {
            throw new ValidationException(fieldName + " ne peut pas être null");
        }
        return date;
    }

    public static LocalDate requireValidDateFR(String dateStr, String fieldName) {
        requireNotBlank(dateStr, fieldName);
        try {
            return DateUtils.parserDateFR(dateStr);
        } catch (RuntimeException e) {
            throw new ValidationException("Format de date invalide pour " + fieldName + ". Utilisez JJ/MM/AAAA", e);
        }
    }

    public static void requireDateRange(LocalDate start, LocalDate end, String startFieldName, String endFieldName) {
        requireNonNullDate(start, startFieldName);
        requireNonNullDate(end, endFieldName);
        if (!start.isBefore(end)) {
            throw new ValidationException(startFieldName + " doit être avant " + endFieldName);
        }
    }
}
