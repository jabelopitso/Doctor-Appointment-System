package service;

import model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages Patient registration, login, and retrieval.
 */
public class PatientService {

    private List<Patient> patients = new ArrayList<>();
    private int nextId = 1;

    // ─── Register ─────────────────────────────────────────────────────────────

    /**
     * Registers a new patient. Usernames must be unique.
     *
     * @return the new Patient, or null if username already exists
     */
    public Patient register(String name, int age, String contact,
                            String username, String password) {

        // Check for duplicate username
        boolean exists = patients.stream()
                .anyMatch(p -> p.getUsername().equalsIgnoreCase(username));

        if (exists) {
            System.out.println("\n  ❌ Username '" + username + "' is already taken. Please choose another.");
            return null;
        }

        Patient patient = new Patient(nextId++, name, age, contact, username, password);
        patients.add(patient);
        System.out.println("\n  ✅ Registration successful! Welcome, " + name + ".");
        return patient;
    }

    // ─── Login ────────────────────────────────────────────────────────────────

    /**
     * Authenticates a patient by username and password.
     *
     * @return the authenticated Patient, or null if credentials are invalid
     */
    public Patient login(String username, String password) {
        Optional<Patient> patient = patients.stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(username)
                        && p.getPassword().equals(password))
                .findFirst();

        if (patient.isPresent()) {
            System.out.println("\n  ✅ Login successful! Welcome back, " + patient.get().getName() + ".");
            return patient.get();
        } else {
            System.out.println("\n  ❌ Invalid username or password. Please try again.");
            return null;
        }
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public Optional<Patient> findById(int id) {
        return patients.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }
}