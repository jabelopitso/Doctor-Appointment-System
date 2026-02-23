package service;

import model.Appointment;
import model.Doctor;
import model.Patient;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AppointmentService Tests")
class AppointmentServiceTest {

    private AppointmentService appointmentService;
    private AppointmentManager manager;
    private DoctorService      doctorService;
    private PatientService     patientService;
    private Doctor             doctor;
    private Patient            patient;

    @BeforeEach
    void setUp() {
        new File("data/appointments.txt").delete();
        new File("data/patients.txt").delete();
        new File("data/doctors.txt").delete();

        manager            = new AppointmentManager();
        doctorService      = new DoctorService();
        patientService     = new PatientService();
        appointmentService = new AppointmentService(manager);

        doctor  = doctorService.register(
                "John Smith", "Surgeon", "011-000-0404", "john", "john123");
        patient = patientService.register(
                "Jabelo Pitso", 25, "011-000-0001", "jabelo", "1234");

        doctor.addAvailableSlot("2099-07-01 10:00");
        doctor.addAvailableSlot("2099-07-01 14:00");
    }

    @AfterEach
    void tearDown() {
        new File("data/appointments.txt").delete();
        new File("data/patients.txt").delete();
        new File("data/doctors.txt").delete();
    }

    @Test
    @DisplayName("Book appointment successfully")
    void testBook_success() {
        Appointment appt = appointmentService.book(
                patient, doctor, "2099-07-01 10:00", "Checkup");

        assertNotNull(appt);
        assertEquals(Appointment.Status.SCHEDULED, appt.getStatus());
        assertEquals("Jabelo Pitso",     appt.getPatientName());
        assertEquals("John Smith",       appt.getDoctorName());
        assertEquals("2099-07-01 10:00", appt.getSlot());
    }

    @Test
    @DisplayName("Booking removes slot from doctor availability")
    void testBook_removesSlotFromDoctor() {
        appointmentService.book(patient, doctor, "2099-07-01 10:00", "Checkup");

        assertFalse(doctor.getAvailableSlots().contains("2099-07-01 10:00"),
                "Slot should be removed from doctor after booking");
    }

    @Test
    @DisplayName("Booking fails when slot is not available on doctor")
    void testBook_slotNotAvailable() {
        Appointment appt = appointmentService.book(
                patient, doctor, "2099-12-31 09:00", "Checkup");

        assertNull(appt, "Booking with unavailable slot should return null");
    }

    @Test
    @DisplayName("Booking fails for a slot in the past")
    void testBook_pastSlotRejected() {
        doctor.addAvailableSlot("2000-01-01 10:00");

        Appointment appt = appointmentService.book(
                patient, doctor, "2000-01-01 10:00", "Checkup");

        assertNull(appt, "Past slot should be rejected by DateValidator");
    }

    @Test
    @DisplayName("Booking fails when patient already has appointment at same slot")
    void testBook_conflictDetected() {
        Doctor doctor2 = doctorService.register(
                "Jane Doe", "Dermatologist", "011-000-0505", "jane", "jane123");
        doctor2.addAvailableSlot("2099-07-01 10:00");

        appointmentService.book(patient, doctor, "2099-07-01 10:00", "Checkup");

        Appointment conflict = appointmentService.book(
                patient, doctor2, "2099-07-01 10:00", "Skin check");

        assertNull(conflict, "Conflicting appointment should return null");
    }

    @Test
    @DisplayName("Patient can book two appointments at different times")
    void testBook_differentSlots_success() {
        Doctor doctor2 = doctorService.register(
                "Jane Doe", "Dermatologist", "011-000-0505", "jane", "jane123");
        doctor2.addAvailableSlot("2099-08-01 09:00");

        Appointment appt1 = appointmentService.book(
                patient, doctor,  "2099-07-01 10:00", "Checkup");
        Appointment appt2 = appointmentService.book(
                patient, doctor2, "2099-08-01 09:00", "Skin check");

        assertNotNull(appt1, "First booking should succeed");
        assertNotNull(appt2, "Second booking at different time should succeed");
    }

    @Test
    @DisplayName("Cancel appointment successfully")
    void testCancel_success() {
        Appointment appt = appointmentService.book(
                patient, doctor, "2099-07-01 10:00", "Checkup");

        boolean result = appointmentService.cancel(appt.getId(), patient, doctorService);

        assertTrue(result);
        assertEquals(Appointment.Status.CANCELLED, appt.getStatus());
    }

    @Test
    @DisplayName("Cancellation restores slot back to doctor")
    void testCancel_restoresSlot() {
        appointmentService.book(patient, doctor, "2099-07-01 10:00", "Checkup");

        assertFalse(doctor.getAvailableSlots().contains("2099-07-01 10:00"));

        Appointment appt = manager.getByPatient(patient.getId()).get(0);
        appointmentService.cancel(appt.getId(), patient, doctorService);

        assertTrue(doctor.getAvailableSlots().contains("2099-07-01 10:00"),
                "Slot should be restored to doctor after cancellation");
    }

    @Test
    @DisplayName("Cancellation fails for non-existent appointment")
    void testCancel_notFound() {
        boolean result = appointmentService.cancel(999, patient, doctorService);

        assertFalse(result, "Cancelling non-existent appointment should return false");
    }

    @Test
    @DisplayName("Patient cannot cancel another patient's appointment")
    void testCancel_wrongPatient() {
        Patient otherPatient = patientService.register(
                "Other Person", 30, "011-000-0002", "other", "pass");

        Appointment appt = appointmentService.book(
                patient, doctor, "2099-07-01 10:00", "Checkup");

        boolean result = appointmentService.cancel(appt.getId(), otherPatient, doctorService);

        assertFalse(result, "Patient should not cancel another patient's appointment");
        assertEquals(Appointment.Status.SCHEDULED, appt.getStatus());
    }

    @Test
    @DisplayName("Cannot cancel an already cancelled appointment")
    void testCancel_alreadyCancelled() {
        Appointment appt = appointmentService.book(
                patient, doctor, "2099-07-01 10:00", "Checkup");

        appointmentService.cancel(appt.getId(), patient, doctorService);

        assertThrows(IllegalStateException.class, appt::cancel,
                "Cancelling an already cancelled appointment should throw exception");
    }

    @Test
    @DisplayName("Patient has correct number of scheduled appointments")
    void testGetScheduledForPatient() {
        appointmentService.book(patient, doctor, "2099-07-01 10:00", "Checkup");
        appointmentService.book(patient, doctor, "2099-07-01 14:00", "Follow-up");

        List<Appointment> scheduled = appointmentService
                .getScheduledForPatient(patient.getId());

        assertEquals(2, scheduled.size());
    }

    @Test
    @DisplayName("Cancelled appointments excluded from scheduled list")
    void testGetScheduledForPatient_excludesCancelled() {
        Appointment appt = appointmentService.book(
                patient, doctor, "2099-07-01 10:00", "Checkup");

        appointmentService.cancel(appt.getId(), patient, doctorService);

        List<Appointment> scheduled = appointmentService
                .getScheduledForPatient(patient.getId());

        assertEquals(0, scheduled.size());
    }
}
