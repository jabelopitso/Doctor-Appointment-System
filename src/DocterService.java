package service;

import model.Doctor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages all Doctor-related operations.
 * Acts as the data access layer for Doctor objects.
 */
public class DoctorService {

    private List<Doctor> doctors = new ArrayList<>();
    private int nextId = 1;

    // ─── Constructor: Pre-load sample doctors ─────────────────────────────────

    public DoctorService() {
        // Pre-load doctors so the system has data to work with immediately
        Doctor d1 = new Doctor(nextId++, "Sarah Johnson", "Cardiologist", "011-555-0101");
        d1.addAvailableSlot("2024-06-10 09:00");
        d1.addAvailableSlot("2024-06-10 11:00");
        d1.addAvailableSlot("2024-06-11 10:00");

        Doctor d2 = new Doctor(nextId++, "Michael Dlamini", "General Practitioner", "011-555-0202");
        d2.addAvailableSlot("2024-06-10 08:00");
        d2.addAvailableSlot("2024-06-10 14:00");
        d2.addAvailableSlot("2024-06-12 09:00");

        Doctor d3 = new Doctor(nextId++, "Priya Naidoo", "Dermatologist", "011-555-0303");
        d3.addAvailableSlot("2024-06-11 11:00");
        d3.addAvailableSlot("2024-06-13 15:00");

        doctors.add(d1);
        doctors.add(d2);
        doctors.add(d3);
    }

    // ─── CRUD Operations ──────────────────────────────────────────────────────

    /**
     * Register a new doctor into the system.
     */
    public Doctor addDoctor(String name, String specialization, String contact) {
        Doctor doctor = new Doctor(nextId++, name, specialization, contact);
        doctors.add(doctor);
        return doctor;
    }

    /**
     * Find a doctor by their ID.
     */
    public Optional<Doctor> findById(int id) {
        return doctors.stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }

    /**
     * Get all doctors in the system.
     */
    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors);
    }

    /**
     * Add an available slot to a specific doctor.
     */
    public boolean addSlotToDoctor(int doctorId, String slot) {
        Optional<Doctor> doctor = findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().addAvailableSlot(slot);
            return true;
        }
        return false;
    }

    /**
     * Display all doctors with their available slots.
     */
    public void displayAllDoctors() {
        if (doctors.isEmpty()) {
            System.out.println("  No doctors registered in the system.");
            return;
        }
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║                   AVAILABLE DOCTORS                     ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
        for (Doctor d : doctors) {
            System.out.println("\n  " + d);
            System.out.println("  Available Slots:");
            if (d.getAvailableSlots().isEmpty()) {
                System.out.println("    → No available slots at this time.");
            } else {
                for (String slot : d.getAvailableSlots()) {
                    System.out.println("    → " + slot);
                }
            }
            System.out.println("  ─────────────────────────────────────────────────────────");
        }
    }
}