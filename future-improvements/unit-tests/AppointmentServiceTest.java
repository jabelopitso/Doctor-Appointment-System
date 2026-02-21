// FUTURE IMPROVEMENT: JUnit Unit Tests
// ─────────────────────────────────────
// Goal    : Full test coverage for all service classes
// Benefit : Catch bugs early, ensure refactors don't break existing features
//
// Setup:
//   Add to pom.xml:
//     <dependency>
//         <groupId>org.junit.jupiter</groupId>
//         <artifactId>junit-jupiter</artifactId>
//         <version>5.10.0</version>
//         <scope>test</scope>
//     </dependency>
//
// Tests to write:
//   AppointmentServiceTest
//     - testBookAppointment_success()
//     - testBookAppointment_slotNotAvailable()
//     - testBookAppointment_conflictDetected()
//     - testCancelAppointment_success()
//     - testCancelAppointment_wrongPatient()
//     - testCancelAppointment_alreadyCancelled()
//
//   PatientServiceTest
//     - testRegister_success()
//     - testRegister_duplicateUsername()
//     - testLogin_success()
//     - testLogin_wrongPassword()
//
//   DoctorServiceTest
//     - testLogin_success()
//     - testLogin_invalidCredentials()
//     - testAddSlot()
//     - testRemoveSlot()

public class AppointmentServiceTest {
    // Implementation coming soon
}
