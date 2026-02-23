package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {

    // Must match exactly how slots are stored: "YYYY-MM-DD HH:mm"
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Checks if a slot string is a valid future date and time.
     * Returns true if valid and in the future.
     * Returns false and prints a reason if invalid or in the past.
     */
    public static boolean isValid(String slot) {

        // 1. Check format
        LocalDateTime slotTime;
        try {
            slotTime = LocalDateTime.parse(slot, FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("\n  ERROR: Invalid date format.");
            System.out.println("         Use YYYY-MM-DD HH:mm  (e.g. 2025-07-15 10:00)");
            return false;
        }

        // 2. Check it is in the future
        if (slotTime.isBefore(LocalDateTime.now())) {
            System.out.println("\n  ERROR: Cannot book a slot in the past.");
            System.out.println("         Selected : " + slot);
            System.out.println("         Now      : " +
                    LocalDateTime.now().format(FORMATTER));
            return false;
        }

        return true;
    }

    /**
     * Checks format only — used when a doctor adds a slot.
     */
    public static boolean isValidFormat(String slot) {
        try {
            LocalDateTime.parse(slot, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("\n  ERROR: Invalid date format.");
            System.out.println("         Use YYYY-MM-DD HH:mm  (e.g. 2025-07-15 10:00)");
            return false;
        }
    }
}
