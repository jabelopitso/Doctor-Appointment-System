package util;

import model.Appointment;
import model.Doctor;
import model.Patient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String DATA_DIR          = "data/";
    private static final String PATIENTS_FILE     = DATA_DIR + "patients.txt";
    private static final String APPOINTMENTS_FILE = DATA_DIR + "appointments.txt";
    private static final String DOCTORS_FILE      = DATA_DIR + "doctors.txt";

    static {
        new File(DATA_DIR).mkdirs();
    }

    // ── Doctors ───────────────────────────────────────────────────────────────

    public static void saveDoctors(List<Doctor> doctors) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DOCTORS_FILE))) {
            for (Doctor d : doctors) {
                // FORMAT: id|name|specialization|contact|username|password|slot1,slot2,...
                String slots = String.join(",", d.getAvailableSlots());
                bw.write(d.getId()             + "|" + d.getName()          + "|"
                       + d.getSpecialization() + "|" + d.getContactNumber() + "|"
                       + d.getUsername()       + "|" + d.getPassword()      + "|"
                       + slots);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("  WARNING: Could not save doctors -- " + e.getMessage());
        }
    }

    public static List<Doctor> loadDoctors() {
        List<Doctor> list = new ArrayList<>();
        File file = new File(DOCTORS_FILE);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split("\\|", -1);
                if (f.length < 7) continue;
                Doctor d = new Doctor(
                        Integer.parseInt(f[0].trim()), f[1].trim(),
                        f[2].trim(), f[3].trim(),
                        f[4].trim(), f[5].trim()
                );
                // Reload saved slots
                if (!f[6].trim().isEmpty()) {
                    for (String slot : f[6].trim().split(","))
                        d.addAvailableSlot(slot.trim());
                }
                list.add(d);
            }
        } catch (IOException e) {
            System.out.println("  WARNING: Could not load doctors -- " + e.getMessage());
        }
        return list;
    }

    // ── Patients ──────────────────────────────────────────────────────────────

    public static void savePatients(List<Patient> patients) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATIENTS_FILE))) {
            for (Patient p : patients) {
                bw.write(p.getId()             + "|" + p.getName()          + "|"
                       + p.getAge()            + "|" + p.getContactNumber() + "|"
                       + p.getUsername()       + "|" + p.getPassword());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("  WARNING: Could not save patients -- " + e.getMessage());
        }
    }

    public static List<Patient> loadPatients() {
        List<Patient> list = new ArrayList<>();
        File file = new File(PATIENTS_FILE);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split("\\|");
                if (f.length < 6) continue;
                list.add(new Patient(
                        Integer.parseInt(f[0].trim()), f[1].trim(),
                        Integer.parseInt(f[2].trim()), f[3].trim(),
                        f[4].trim(), f[5].trim()
                ));
            }
        } catch (IOException e) {
            System.out.println("  WARNING: Could not load patients -- " + e.getMessage());
        }
        return list;
    }

    // ── Appointments ──────────────────────────────────────────────────────────

    public static void saveAppointments(List<Appointment> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE))) {
            for (Appointment a : appointments) {
                bw.write(a.getId()          + "|" + a.getPatientId()   + "|"
                       + a.getPatientName() + "|" + a.getDoctorId()    + "|"
                       + a.getDoctorName()  + "|" + a.getSlot()        + "|"
                       + a.getReason()      + "|" + a.getStatus());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("  WARNING: Could not save appointments -- " + e.getMessage());
        }
    }

    public static List<Appointment> loadAppointments() {
        List<Appointment> list = new ArrayList<>();
        File file = new File(APPOINTMENTS_FILE);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split("\\|");
                if (f.length < 8) continue;
                Appointment a = new Appointment(
                        Integer.parseInt(f[0].trim()), Integer.parseInt(f[1].trim()),
                        f[2].trim(), Integer.parseInt(f[3].trim()), f[4].trim(),
                        f[5].trim(), f[6].trim()
                );
                a.setStatus(Appointment.Status.valueOf(f[7].trim()));
                list.add(a);
            }
        } catch (IOException e) {
            System.out.println("  WARNING: Could not load appointments -- " + e.getMessage());
        }
        return list;
    }
}
