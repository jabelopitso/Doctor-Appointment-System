package main;

import model.Appointment;
import model.Doctor;
import model.Patient;
import service.AppointmentManager;
import service.AppointmentService;
import service.DoctorService;
import service.PatientService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final AppointmentManager  manager            = new AppointmentManager();
    private static final DoctorService       doctorService      = new DoctorService();
    private static final PatientService      patientService     = new PatientService();
    private static final AppointmentService  appointmentService = new AppointmentService(manager);
    private static final Scanner             scanner            = new Scanner(System.in);

    private static Patient currentPatient = null;
    private static Doctor  currentDoctor  = null;

    public static void main(String[] args) {
        banner();
        mainMenu();
        System.out.println("\n  Goodbye! Thank you for using the system.");
        scanner.close();
    }

    // ── Main Menu ─────────────────────────────────────────────────────────────

    private static void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n  ========================================");
            System.out.println("               MAIN MENU                 ");
            System.out.println("  ========================================");
            System.out.println("  1. Register as Patient");
            System.out.println("  2. Patient Login");
            System.out.println("  3. Register as Doctor");
            System.out.println("  4. Doctor Login");
            System.out.println("  5. View All Doctors");
            System.out.println("  6. Exit");
            System.out.println("  ========================================");
            System.out.print("  Choice: ");
            switch (input()) {
                case "1" -> registerPatient();
                case "2" -> { loginPatient(); if (currentPatient != null) patientMenu(); }
                case "3" -> registerDoctor();
                case "4" -> { loginDoctor();  if (currentDoctor  != null) doctorMenu();  }
                case "5" -> doctorService.displayAllDoctors();
                case "6" -> running = false;
                default  -> System.out.println("\n  Please enter 1-6.");
            }
        }
    }

    // ── Patient Menu ──────────────────────────────────────────────────────────

    private static void patientMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n  ========================================");
            System.out.println("  PATIENT MENU -- " + currentPatient.getName());
            System.out.println("  ========================================");
            System.out.println("  1. View Doctors & Available Slots");
            System.out.println("  2. Book an Appointment");
            System.out.println("  3. My Appointment History");
            System.out.println("  4. Cancel an Appointment");
            System.out.println("  5. Logout");
            System.out.println("  ========================================");
            System.out.print("  Choice: ");
            switch (input()) {
                case "1" -> doctorService.displayAllDoctors();
                case "2" -> bookAppointment();
                case "3" -> appointmentService.showPatientHistory(currentPatient);
                case "4" -> cancelAppointment();
                case "5" -> {
                    System.out.println("\n  Logged out.");
                    currentPatient = null;
                    active = false;
                }
                default -> System.out.println("\n  Please enter 1-5.");
            }
        }
    }

    // ── Doctor Menu ───────────────────────────────────────────────────────────

    private static void doctorMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n  ========================================");
            System.out.println("  DOCTOR MENU -- Dr. " + currentDoctor.getName());
            System.out.println("  ========================================");
            System.out.println("  1. View My Schedule");
            System.out.println("  2. View My Available Slots");
            System.out.println("  3. Add a Slot");
            System.out.println("  4. Remove a Slot");
            System.out.println("  5. Logout");
            System.out.println("  ========================================");
            System.out.print("  Choice: ");
            switch (input()) {
                case "1" -> appointmentService.showDoctorSchedule(currentDoctor);
                case "2" -> showDoctorSlots();
                case "3" -> addDoctorSlot();
                case "4" -> removeDoctorSlot();
                case "5" -> {
                    System.out.println("\n  Logged out.");
                    currentDoctor = null;
                    active = false;
                }
                default -> System.out.println("\n  Please enter 1-5.");
            }
        }
    }

    // ── Auth ──────────────────────────────────────────────────────────────────

    private static void registerPatient() {
        System.out.println("\n  --- PATIENT REGISTER ---------------------------");
        System.out.print("  Full Name       : "); String name     = input();
        System.out.print("  Age             : "); int    age      = readInt();
        System.out.print("  Contact Number  : "); String contact  = input();
        System.out.print("  Username        : "); String username = input();
        System.out.print("  Password        : "); String password = input();
        patientService.register(name, age, contact, username, password);
    }

    private static void loginPatient() {
        System.out.println("\n  --- PATIENT LOGIN ------------------------------");
        System.out.print("  Username : "); String username = input();
        System.out.print("  Password : "); String password = input();
        currentPatient = patientService.login(username, password);
    }

    private static void registerDoctor() {
        System.out.println("\n  --- DOCTOR REGISTER ----------------------------");
        System.out.print("  Full Name        : "); String name           = input();
        System.out.print("  Specialization   : "); String specialization = input();
        System.out.print("  Contact Number   : "); String contact        = input();
        System.out.print("  Username         : "); String username       = input();
        System.out.print("  Password         : "); String password       = input();
        doctorService.register(name, specialization, contact, username, password);
    }

    private static void loginDoctor() {
        System.out.println("\n  --- DOCTOR LOGIN --------------------------------");
        System.out.print("  Username : "); String username = input();
        System.out.print("  Password : "); String password = input();
        currentDoctor = doctorService.login(username, password).orElse(null);
    }

    // ── Patient Actions ───────────────────────────────────────────────────────

    private static void bookAppointment() {
        System.out.println("\n  --- BOOK APPOINTMENT ---------------------------");
        doctorService.displayAllDoctors();
        System.out.print("\n  Enter Doctor ID : ");
        int doctorId = readInt();
        Optional<Doctor> doctorOpt = doctorService.findById(doctorId);
        if (doctorOpt.isEmpty()) { System.out.println("\n  ERROR: Doctor not found."); return; }
        Doctor doctor = doctorOpt.get();
        List<String> slots = doctor.getAvailableSlots();
        if (slots.isEmpty()) { System.out.println("\n  ERROR: No available slots."); return; }
        System.out.println("\n  Slots for Dr. " + doctor.getName() + ":");
        for (int i = 0; i < slots.size(); i++)
            System.out.println("  " + (i + 1) + ". " + slots.get(i));
        System.out.print("\n  Select slot number : ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= slots.size()) { System.out.println("\n  ERROR: Invalid selection."); return; }
        System.out.print("  Reason for visit   : ");
        appointmentService.book(currentPatient, doctor, slots.get(idx), input());
    }

    private static void cancelAppointment() {
        System.out.println("\n  --- CANCEL APPOINTMENT -------------------------");
        appointmentService.showPatientHistory(currentPatient);
        List<Appointment> active = appointmentService.getScheduledForPatient(currentPatient.getId());
        if (active.isEmpty()) { System.out.println("\n  No active appointments to cancel."); return; }
        System.out.print("\n  Enter Appointment ID to cancel : ");
        appointmentService.cancel(readInt(), currentPatient, doctorService);
    }

    // ── Doctor Actions ────────────────────────────────────────────────────────

    private static void showDoctorSlots() {
        List<String> slots = currentDoctor.getAvailableSlots();
        System.out.println("\n  --- AVAILABLE SLOTS -- Dr. " + currentDoctor.getName() + " ---");
        if (slots.isEmpty()) { System.out.println("  No available slots."); return; }
        for (int i = 0; i < slots.size(); i++)
            System.out.println("  " + (i + 1) + ". " + slots.get(i));
    }

    private static void addDoctorSlot() {
        System.out.println("\n  --- ADD SLOT -----------------------------------");
        System.out.println("  Format: YYYY-MM-DD HH:mm  (e.g. 2024-07-15 10:00)");
        System.out.print("  New slot : ");
        String slot = input();
        if (slot.isEmpty()) { System.out.println("  ERROR: Slot cannot be empty."); return; }
        currentDoctor.addAvailableSlot(slot);
        doctorService.saveDoctors();
        System.out.println("  SUCCESS: Slot '" + slot + "' added and saved.");
    }

    private static void removeDoctorSlot() {
        showDoctorSlots();
        List<String> slots = currentDoctor.getAvailableSlots();
        if (slots.isEmpty()) return;
        System.out.print("\n  Select slot number to remove : ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= slots.size()) { System.out.println("  ERROR: Invalid selection."); return; }
        String removed = slots.get(idx);
        currentDoctor.removeSlot(removed);
        doctorService.saveDoctors();
        System.out.println("  SUCCESS: Slot '" + removed + "' removed and saved.");
    }

    // ── Utilities ─────────────────────────────────────────────────────────────

    private static String input() { return scanner.nextLine().trim(); }

    private static int readInt() {
        while (true) {
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("  Enter a valid number: "); }
        }
    }

    private static void banner() {
        System.out.println("\n  +======================================================+");
        System.out.println("  |      DOCTOR APPOINTMENT BOOKING SYSTEM               |");
        System.out.println("  |      Author: Jabelo Pitso                            |");
        System.out.println("  +======================================================+");
    }
}
