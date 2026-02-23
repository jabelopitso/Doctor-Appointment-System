package service;

import model.Appointment;
import model.Doctor;
import model.Patient;
import util.DateValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentService {

    private final AppointmentManager manager;

    public AppointmentService(AppointmentManager manager) {
        this.manager = manager;
    }

    public Appointment book(Patient patient, Doctor doctor, String slot, String reason) {

        // 1. Validate slot is not in the past
        if (!DateValidator.isValid(slot)) {
            return null;
        }

        // 2. Doctor must have this slot open
        if (!doctor.isSlotAvailable(slot)) {
            System.out.println("\n  ERROR: Dr. " + doctor.getName() + " is not available at " + slot);
            return null;
        }

        // 3. Patient must not already have appointment at same time
        if (manager.patientHasConflict(patient.getId(), slot)) {
            System.out.println("\n  ERROR: You already have an appointment at " + slot);
            return null;
        }

        // 4. All checks passed — create appointment
        Appointment appt = manager.createAppointment(patient, doctor, slot, reason);
        doctor.removeSlot(slot);
        patient.addAppointmentId(appt.getId());
        System.out.println("\n  SUCCESS: Appointment booked!");
        System.out.println(appt);
        return appt;
    }

    public boolean cancel(int appointmentId, Patient patient, DoctorService doctorService) {
        Optional<Appointment> opt = manager.findById(appointmentId);
        if (opt.isEmpty()) {
            System.out.println("\n  ERROR: Appointment #" + appointmentId + " not found.");
            return false;
        }
        Appointment appt = opt.get();
        if (appt.getPatientId() != patient.getId()) {
            System.out.println("\n  ERROR: You can only cancel your own appointments.");
            return false;
        }
        try {
            appt.cancel();
            doctorService.findById(appt.getDoctorId())
                    .ifPresent(d -> d.addAvailableSlot(appt.getSlot()));
            patient.removeAppointmentId(appointmentId);
            manager.persistCancellation();
            System.out.println("\n  SUCCESS: Appointment #" + appointmentId + " cancelled. Slot restored.");
            return true;
        } catch (IllegalStateException e) {
            System.out.println("\n  ERROR: " + e.getMessage());
            return false;
        }
    }

    public void showPatientHistory(Patient patient) {
        List<Appointment> history = manager.getByPatient(patient.getId());
        System.out.println("\n  +======================================================+");
        System.out.printf ("  | APPOINTMENT HISTORY -- %-30s|%n", patient.getName());
        System.out.println("  +======================================================+");
        if (history.isEmpty()) { System.out.println("  No appointments found."); return; }
        for (Appointment a : history) System.out.println("\n" + a);
    }

    public void showDoctorSchedule(Doctor doctor) {
        List<Appointment> schedule = manager.getByDoctor(doctor.getId());
        System.out.println("\n  +======================================================+");
        System.out.printf ("  | SCHEDULE -- Dr. %-37s|%n", doctor.getName());
        System.out.println("  +======================================================+");
        if (schedule.isEmpty()) { System.out.println("  No upcoming appointments."); return; }
        for (Appointment a : schedule) System.out.println("\n" + a);
    }

    public List<Appointment> getScheduledForPatient(int patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : manager.getByPatient(patientId))
            if (a.getStatus() == Appointment.Status.SCHEDULED) result.add(a);
        return result;
    }
}
