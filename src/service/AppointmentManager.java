package service;

import model.Appointment;
import model.Doctor;
import model.Patient;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentManager {

    private final List<Appointment> appointments;
    private int nextId = 1;

    public AppointmentManager() {
        this.appointments = FileManager.loadAppointments();
        for (Appointment a : appointments)
            if (a.getId() >= nextId) nextId = a.getId() + 1;
        if (!appointments.isEmpty())
            System.out.println("  INFO: Loaded " + appointments.size() + " appointment(s) from file.");
    }

    public Appointment createAppointment(Patient patient, Doctor doctor,
                                          String slot, String reason) {
        Appointment appt = new Appointment(
                nextId++,
                patient.getId(), patient.getName(),
                doctor.getId(),  doctor.getName(),
                slot, reason
        );
        appointments.add(appt);
        FileManager.saveAppointments(appointments);
        return appt;
    }

    public void persistCancellation() {
        FileManager.saveAppointments(appointments);
    }

    public Optional<Appointment> findById(int id) {
        return appointments.stream().filter(a -> a.getId() == id).findFirst();
    }

    public List<Appointment> getByPatient(int patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments)
            if (a.getPatientId() == patientId) result.add(a);
        return result;
    }

    public List<Appointment> getByDoctor(int doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments)
            if (a.getDoctorId() == doctorId && a.getStatus() == Appointment.Status.SCHEDULED)
                result.add(a);
        return result;
    }

    public boolean patientHasConflict(int patientId, String slot) {
        return appointments.stream()
                .anyMatch(a -> a.getPatientId() == patientId
                            && a.getStatus()    == Appointment.Status.SCHEDULED
                            && a.getSlot().equals(slot));
    }
}
