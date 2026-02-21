// FUTURE IMPROVEMENT: Email & SMS Notifications
// ───────────────────────────────────────────────
// Goal    : Send confirmation messages when appointments are booked or cancelled
// Benefit : Keeps patients and doctors informed without logging into the system
//
// EMAIL SETUP (JavaMail):
//   Add to pom.xml:
//     <dependency>
//         <groupId>com.sun.mail</groupId>
//         <artifactId>jakarta.mail</artifactId>
//         <version>2.0.1</version>
//     </dependency>
//
//   Configure SMTP (Gmail example):
//     host     = smtp.gmail.com
//     port     = 587
//     auth     = true
//     starttls = true
//
// SMS SETUP (Twilio):
//   Add to pom.xml:
//     <dependency>
//         <groupId>com.twilio.sdk</groupId>
//         <artifactId>twilio</artifactId>
//         <version>9.3.0</version>
//     </dependency>
//
// Notifications to implement:
//   - sendBookingConfirmation(Patient, Appointment)
//   - sendCancellationNotice(Patient, Appointment)
//   - sendReminderToDoctor(Doctor, Appointment)
//   - sendDayBeforeReminder(Patient, Appointment)

public class NotificationService {
    // Implementation coming soon
}
