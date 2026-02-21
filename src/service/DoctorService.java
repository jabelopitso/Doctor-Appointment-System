package service;

import model.Doctor;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorService {

    private final List<Doctor> doctors;
    private int nextId = 1;

    public DoctorService() {
        this.doctors = FileManager.loadDoctors();

        // Set nextId based on loaded doctors to avoid ID collisions
        for (Doctor d : doctors)
            if (d.getId() >= nextId) nextId = d.getId() + 1;

        // Only pre-load sample doctors if no doctors exist in file yet
        if (doctors.isEmpty()) {
            seedDefaultDoctors();
        } else {
            System.out.println("  INFO: Loaded " + doctors.size() + " doctor(s) from file.");
        }
    }

    // ── Seed default doctors on first run ────────────────────────────────────

    private void seedDefaultDoctors() {
        Doctor d1 = new Doctor(nextId++, "Sarah Johnson", "Cardiologist",
                "011-555-0101", "sarah", "sarah123");
        d1.addAvailableSlot("2024-06-10 09:00");
        d1.addAvailableSlot("2024-06-10 11:00");
        d1.addAvailableSlot("2024-06-11 10:00");

        Doctor d2 = new Doctor(nextId++, "Michael Dlamini", "General Practitioner",
                "011-555-0202", "michael", "michael123");
        d2.addAvailableSlot("2024-06-10 08:00");
        d2.addAvailableSlot("2024-06-10 14:00");
        d2.addAvailableSlot("2024-06-12 09:00");

        Doctor d3 = new Doctor(nextId++, "Priya Naidoo", "Dermatologist",
                "011-555-0303", "priya", "priya123");
        d3.addAvailableSlot("2024-06-11 11:00");
        d3.addAvailableSlot("2024-06-13 15:00");

        doctors.add(d1);
        doctors.add(d2);
        doctors.add(d3);

        FileManager.saveDoctors(doctors);
        System.out.println("  INFO: Default doctors loaded and saved to file.");
    }

    // ── Register ─────────────────────────────────────────────────────────────

    public Doctor register(String name, String specialization,
                            String contact, String username, String password) {

        // Check for duplicate username
        boolean taken = doctors.stream()
                .anyMatch(d -> d.getUsername().equalsIgnoreCase(username));
        if (taken) {
            System.out.println("\n  ERROR: Username '" + username + "' is already taken.");
            return null;
        }

        Doctor d = new Doctor(nextId++, name, specialization, contact, username, password);
        doctors.add(d);
        FileManager.saveDoctors(doctors);
        System.out.println("\n  SUCCESS: Doctor registered! Welcome, Dr. " + name + ".");
        return d;
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    public Optional<Doctor> login(String username, String password) {
        Optional<Doctor> found = doctors.stream()
                .filter(d -> d.getUsername().equalsIgnoreCase(username)
                          && d.getPassword().equals(password))
                .findFirst();
        if (found.isPresent()) {
            System.out.println("\n  SUCCESS: Welcome, Dr. " + found.get().getName() + "!");
            return found;
        }
        System.out.println("\n  ERROR: Invalid doctor credentials.");
        return Optional.empty();
    }

    // ── Save (called after slot changes) ──────────────────────────────────────

    public void saveDoctors() {
        FileManager.saveDoctors(doctors);
    }

    // ── Lookup ────────────────────────────────────────────────────────────────

    public Optional<Doctor> findById(int id) {
        return doctors.stream().filter(d -> d.getId() == id).findFirst();
    }

    public List<Doctor> getAllDoctors() { return new ArrayList<>(doctors); }

    // ── Display ───────────────────────────────────────────────────────────────

    public void displayAllDoctors() {
        System.out.println("\n  +======================================================+");
        System.out.println("  |               AVAILABLE DOCTORS                      |");
        System.out.println("  +======================================================+");
        if (doctors.isEmpty()) {
            System.out.println("  No doctors registered yet.");
            return;
        }
        for (Doctor d : doctors) {
            System.out.println("\n" + d);
            System.out.println("  Available Slots:");
            if (d.getAvailableSlots().isEmpty()) {
                System.out.println("    -> No slots available.");
            } else {
                for (String slot : d.getAvailableSlots())
                    System.out.println("    -> " + slot);
            }
            System.out.println("  ------------------------------------------------------");
        }
    }
}
