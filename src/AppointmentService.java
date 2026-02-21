package service;

import model.Appointment;
import model.Doctor;
import model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Core business logic for the appointment booking system.
 * Handles booking, cancellation, and retrieval of appointments.
 * Enforces conflict prevention — no double booking.
 */
public class AppointmentService {

    private List<Appointment> appointments = new ArrayList<>();
    private int nextId = 1;

    // ─── Book Appointment ─────────────────────────────────────────────────────

    /**
     * Books an appointment if the slot is available on the doctor's side
     * and the patient has no conflicting appointment at the same time.
     *
     * @return the booked Appointment, or null if booking failed
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor,
                                       String slot, String reason) {

        // 1. Check if doctor has this slot available
        if (!doctor.isSlotAvailable(slot)) {
            System.out.println("\n  ❌ Error: Dr. " + doctor.getName() +
                    " is not available at " + slot);
            return null;
        }

        // 2. Conflict prevention — patient can't have two appointments at same time
        boolean patientConflict = appointments.stream()
                .filter(a -> a.getPatientId() == patient.getId())
                .filter(a -> a.getStatus() == Appointment.Status.SCHEDULED)
                .anyMatch(a -> a.getAppointmentSlot().equals(slot));

        if (patientConflict) {
            System.out.println("\n  ❌ Error: You already have an appointment booked at " + slot);
            return null;
        }

        // 3. Create the appointment and remove the slot from doctor's availability
        Appointment appointment = new Appointment(
                nextId++, patient.getId(), patient.getName(),
                doctor.getId(), doctor.getName(), slot, reason
        );

        appointments.add(appointment);
        doctor.removeSlot(slot);                       // slot is now taken
        patient.addAppointmentId(appointment.getId()); // link to patient

        System.out.println("\n  ✅ Appointment successfully booked!");
        return appointment;
    }

    // ─── Cancel Appointment ───────────────────────────────────────────────────

    /**
     * Cancels a scheduled appointment and restores the doctor's slot.
     */
    public boolean cancelAppointment(int appointmentId, Patient patient,
                                     DoctorService doctorService) {
        Optional<Appointment> apptOpt = findById(appointmentId);

        if (apptOpt.isEmpty()) {
            System.out.println("\n  ❌ Appointment not found.");
            return false;
        }

        Appointment appt = apptOpt.get();

        // Security: ensure this patient owns the appointment
        if (appt.getPatientId() != patient.getId()) {
            System.out.println("\n  ❌ You can only cancel your own appointments.");
            return false;
        }

        try {
            appt.cancel();

            // Restore the slot to the doctor's availability
            doctorService.findById(appt.getDoctorId())
                    .ifPresent(doc -> doc.addAvailableSlot(appt.getAppointmentSlot()));

            patient.removeAppointmentId(appointmentId);
            System.out.println("\n  ✅ Appointment #" + appointmentId + " cancelled successfully.");
            return true;

        } catch (IllegalStateException e) {
            System.out.println("\n  ❌ " + e.getMessage());
            return false;
        }
    }

    // ─── View Appointments ────────────────────────────────────────────────────

    /**
     * Get all scheduled appointments for a specific patient.
     */
    public List<Appointment> getPatientAppointments(int patientId) {
        return appointments.stream()
                .filter(a -> a.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    /**
     * Get all scheduled appointments for a specific doctor.
     */
    public List<Appointment> getDoctorAppointments(int doctorId) {
        return appointments.stream()
                .filter(a -> a.getDoctorId() == doctorId
                        && a.getStatus() == Appointment.Status.SCHEDULED)
                .collect(Collectors.toList());
    }

    /**
     * Display appointment history for a patient (all statuses).
     */
    public void displayPatientHistory(Patient patient) {
        List<Appointment> history = getPatientAppointments(patient.getId());

        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║             APPOINTMENT HISTORY — " + patient.getName());
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");

        if (history.isEmpty()) {
            System.out.println("  No appointment history found.");
            return;
        }

        history.forEach(a -> System.out.println("\n" + a));
    }

    /**
     * Display all upcoming scheduled appointments for a doctor.
     */
    public void displayDoctorSchedule(Doctor doctor) {
        List<Appointment> schedule = getDoctorAppointments(doctor.getId());

        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║       UPCOMING SCHEDULE — Dr. " + doctor.getName());
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");

        if (schedule.isEmpty()) {
            System.out.println("  No upcoming appointments scheduled.");
            return;
        }

        schedule.forEach(a -> System.out.println("\n" + a));
    }

    // ─── Utility ──────────────────────────────────────────────────────────────

    public Optional<Appointment> findById(int id) {
        return appointments.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }
}