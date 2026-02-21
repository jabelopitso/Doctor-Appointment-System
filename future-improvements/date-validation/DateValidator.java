// FUTURE IMPROVEMENT: Date Validation
// ─────────────────────────────────────
// Goal    : Reject any booking slot that is in the past
// Benefit : Prevents patients from accidentally booking historical dates
//
// TODO:
//   - Parse slot strings using LocalDateTime.parse()
//   - Compare against LocalDateTime.now()
//   - Reject if slot is before current date and time
//   - Validate format is exactly "YYYY-MM-DD HH:mm"
//   - Show a friendly error message: "Cannot book a slot in the past"
//
// Example logic:
//   LocalDateTime slotTime = LocalDateTime.parse(slot, formatter);
//   if (slotTime.isBefore(LocalDateTime.now())) {
//       System.out.println("ERROR: Cannot book a slot in the past.");
//       return false;
//   }

public class DateValidator {
    // Implementation coming soon
}
