package service;

import model.Doctor;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DoctorService Tests")
class DoctorServiceTest {

    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        new File("data/doctors.txt").delete();
        doctorService = new DoctorService();
    }

    @AfterEach
    void tearDown() {
        new File("data/doctors.txt").delete();
    }

    @Test
    @DisplayName("Register doctor successfully")
    void testRegister_success() {
        Doctor d = doctorService.register(
                "John Smith", "Surgeon", "011-000-0404", "john", "john123");

        assertNotNull(d);
        assertEquals("John Smith", d.getName());
        assertEquals("Surgeon",    d.getSpecialization());
        assertEquals("john",       d.getUsername());
    }

    @Test
    @DisplayName("Registration fails when username is already taken")
    void testRegister_duplicateUsername() {
        doctorService.register(
                "John Smith", "Surgeon", "011-000-0404", "john", "john123");
        Doctor duplicate = doctorService.register(
                "Jane Doe", "Cardiologist", "011-000-0505", "john", "pass456");

        assertNull(duplicate, "Duplicate username should return null");
    }

    @Test
    @DisplayName("Default doctors are seeded on first run")
    void testDefaultDoctors_seeded() {
        assertEquals(3, doctorService.getAllDoctors().size(),
                "Should have 3 default doctors on fresh start");
    }

    @Test
    @DisplayName("Doctor login succeeds with correct credentials")
    void testLogin_success() {
        Doctor loggedIn = doctorService.login("sarah", "sarah123").orElse(null);

        assertNotNull(loggedIn);
        assertEquals("Sarah Johnson", loggedIn.getName());
    }

    @Test
    @DisplayName("Doctor login fails with wrong password")
    void testLogin_wrongPassword() {
        assertTrue(doctorService.login("sarah", "wrongpassword").isEmpty(),
                "Wrong password should return empty Optional");
    }

    @Test
    @DisplayName("Doctor login fails with unknown username")
    void testLogin_unknownUsername() {
        assertTrue(doctorService.login("nobody", "1234").isEmpty(),
                "Unknown username should return empty Optional");
    }

    @Test
    @DisplayName("Doctor can add an available slot")
    void testAddSlot() {
        Doctor d = doctorService.register(
                "John Smith", "Surgeon", "011-000-0404", "john", "john123");
        d.addAvailableSlot("2099-07-01 10:00");

        assertTrue(d.getAvailableSlots().contains("2099-07-01 10:00"),
                "Slot should be added to doctor's list");
    }

    @Test
    @DisplayName("Doctor cannot add duplicate slot")
    void testAddSlot_noDuplicates() {
        Doctor d = doctorService.register(
                "John Smith", "Surgeon", "011-000-0404", "john", "john123");
        d.addAvailableSlot("2099-07-01 10:00");
        d.addAvailableSlot("2099-07-01 10:00");

        assertEquals(1, d.getAvailableSlots().size(),
                "Duplicate slot should not be added twice");
    }

    @Test
    @DisplayName("Doctor can remove a slot")
    void testRemoveSlot() {
        Doctor d = doctorService.register(
                "John Smith", "Surgeon", "011-000-0404", "john", "john123");
        d.addAvailableSlot("2099-07-01 10:00");
        d.removeSlot("2099-07-01 10:00");

        assertFalse(d.getAvailableSlots().contains("2099-07-01 10:00"),
                "Slot should be removed");
    }

    @Test
    @DisplayName("Find doctor by ID returns correct doctor")
    void testFindById_success() {
        Doctor registered = doctorService.register(
                "John Smith", "Surgeon", "011-000-0404", "john", "john123");
        Doctor found = doctorService.findById(registered.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(registered.getId(), found.getId());
    }

    @Test
    @DisplayName("Find doctor by non-existent ID returns empty")
    void testFindById_notFound() {
        assertTrue(doctorService.findById(999).isEmpty(),
                "Should return empty Optional for unknown ID");
    }
}
