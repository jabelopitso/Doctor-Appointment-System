package service;

import model.Patient;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PatientService Tests")
class PatientServiceTest {

    private PatientService patientService;

    @BeforeEach
    void setUp() {
        new File("data/patients.txt").delete();
        patientService = new PatientService();
    }

    @AfterEach
    void tearDown() {
        new File("data/patients.txt").delete();
    }

    @Test
    @DisplayName("Register patient successfully")
    void testRegister_success() {
        Patient p = patientService.register(
                "Jabelo Pitso", 25, "011-000-0001", "jabelo", "1234");

        assertNotNull(p);
        assertEquals("Jabelo Pitso", p.getName());
        assertEquals("jabelo",       p.getUsername());
        assertEquals(25,             p.getAge());
    }

    @Test
    @DisplayName("Registration fails when username is already taken")
    void testRegister_duplicateUsername() {
        patientService.register("Jabelo Pitso", 25, "011-000-0001", "jabelo", "1234");
        Patient duplicate = patientService.register(
                "Other Person", 30, "011-000-0002", "jabelo", "5678");

        assertNull(duplicate, "Duplicate username should return null");
    }

    @Test
    @DisplayName("Registration fails for duplicate username with different casing")
    void testRegister_duplicateUsername_caseInsensitive() {
        patientService.register("Jabelo Pitso", 25, "011-000-0001", "jabelo", "1234");
        Patient duplicate = patientService.register(
                "Other Person", 30, "011-000-0002", "JABELO", "5678");

        assertNull(duplicate, "Username check should be case-insensitive");
    }

    @Test
    @DisplayName("Multiple patients can register with different usernames")
    void testRegister_multiplePatients() {
        Patient p1 = patientService.register(
                "Jabelo Pitso", 25, "011-000-0001", "jabelo", "1234");
        Patient p2 = patientService.register(
                "John Smith",   30, "011-000-0002", "john",   "5678");

        assertNotNull(p1);
        assertNotNull(p2);
        assertNotEquals(p1.getId(), p2.getId(), "Each patient should have a unique ID");
    }

    @Test
    @DisplayName("Login succeeds with correct credentials")
    void testLogin_success() {
        patientService.register("Jabelo Pitso", 25, "011-000-0001", "jabelo", "1234");
        Patient loggedIn = patientService.login("jabelo", "1234");

        assertNotNull(loggedIn);
        assertEquals("Jabelo Pitso", loggedIn.getName());
    }

    @Test
    @DisplayName("Login succeeds regardless of username casing")
    void testLogin_caseInsensitiveUsername() {
        patientService.register("Jabelo Pitso", 25, "011-000-0001", "jabelo", "1234");
        Patient loggedIn = patientService.login("JABELO", "1234");

        assertNotNull(loggedIn, "Login should work with uppercase username");
    }

    @Test
    @DisplayName("Login fails with wrong password")
    void testLogin_wrongPassword() {
        patientService.register("Jabelo Pitso", 25, "011-000-0001", "jabelo", "1234");
        Patient loggedIn = patientService.login("jabelo", "wrongpassword");

        assertNull(loggedIn, "Wrong password should return null");
    }

    @Test
    @DisplayName("Login fails with non-existent username")
    void testLogin_unknownUsername() {
        Patient loggedIn = patientService.login("nobody", "1234");

        assertNull(loggedIn, "Unknown username should return null");
    }

    @Test
    @DisplayName("Find patient by ID returns correct patient")
    void testFindById_success() {
        Patient registered = patientService.register(
                "Jabelo Pitso", 25, "011-000-0001", "jabelo", "1234");
        Patient found = patientService.findById(registered.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(registered.getId(), found.getId());
    }

    @Test
    @DisplayName("Find patient by non-existent ID returns empty")
    void testFindById_notFound() {
        assertTrue(patientService.findById(999).isEmpty(),
                "Should return empty Optional for unknown ID");
    }
}
