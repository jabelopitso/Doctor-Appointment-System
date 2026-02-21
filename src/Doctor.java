package main;

import model.Appointment;
import model.Doctor;
import model.Patient;
import service.AppointmentService;
import service.DoctorService;
import service.PatientService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════════════╗
 * ║      DOCTOR APPOINTMENT BOOKING SYSTEM           ║
 * ║      Author: Jabelo Pitso                        ║
 * ║      Built with Java OOP Principles              ║
 * ╚══════════════════════════════════════════════════╝
 *
 * Entry point for the application.
 * Handles all user interaction through a console menu.
 */
public class Main {

    // ─── Services (Dependency Injection style) ────────────────────────────────
    private static final DoctorService doctorService         = new DoctorService();
    private static final PatientService patientService       = new PatientService();
    private static final AppointmentService appointmentService = new AppointmentService();

    private static final Scanner scanner = new Scanner(System.in);

    // Currently logged-in patient (null when no one is logged in)
    private static Patient currentPatient = null;

    // ─── Main Entry ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        printBanner();
        mainMenu();
        System.out.println("\n  👋 Thank you for using the Doctor Appointment System. Goodbye!");
        scanner.close();
    }

    // ─── Menus ────────────────────────────────────────────────────────────────

    private static void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n  ════════════════════════════════════════");
            System.out.println("               MAIN MENU                ");
            System.out.println("  ════════════════════════════════════════");
            System.out.println("  1. Register as a Patient");
            System.out.println("  2. Patient Login");
            System.out.println("  3. View All Doctors");
            System.out.println("  4. Exit");
            System.out.println("  ════════════════════════════════════════");
            System.out.print("  Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> registerPatient();
                case "2" -> {
                    loginPatient();
                    if (currentPatient != null) {
                        patientMenu();
                    }
                }
                case "3" -> doctorService.displayAllDoctors();
                case "4" -> running = false;
                default  -> System.out.println("\n  ⚠️  Invalid option. Please enter 1–4.");
            }
        }
    }

    private static void patientMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n  ════════════════════════════════════════");
            System.out.println("      PATIENT MENU — " + currentPatient.getName());
            System.out.println("  ════════════════════════════════════════");
            System.out.println("  1. View All Doctors & Slots");
            System.out.println("  2. Book an Appointment");
            System.out.println("  3. View My Appointments");
            System.out.println("  4. Cancel an Appointment");
            System.out.println("  5. Logout");
            System.out.println("  ════════════════════════════════════════");
            System.out.print("  Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> doctorService.displayAllDoctors();
                case "2" -> bookAppointment();
                case "3" -> appointmentService.displayPatientHistory(currentPatient);
                case "4" -> cancelAppointment();
                case "5" -> {
                    System.out.println("\n  👋 Logged out. See you soon, " + currentPatient.getName() + "!");
                    currentPatient = null;
                    loggedIn = false;
                }
                default  -> System.out.println("\n  ⚠️  Invalid option. Please enter 1–5.");
            }
        }
    }

    // ─── Patient Operations ───────────────────────────────────────────────────

    private static void registerPatient() {
        System.out.println("\n  ─── PATIENT REGISTRATION ───");
        System.out.print("  Full Name       : ");
        String name = scanner.nextLine().trim();

        System.out.print("  Age             : ");
        int age = readInt();

        System.out.print("  Contact Number  : ");
        String contact = scanner.nextLine().trim();

        System.out.print("  Choose Username : ");
        String username = scanner.nextLine().trim();

        System.out.print("  Choose Password : ");
        String password = scanner.nextLine().trim();

        patientService.register(name, age, contact, username, password);
    }

    private static void loginPatient() {
        System.out.println("\n  ─── PATIENT LOGIN ───");
        System.out.print("  Username : ");
        String username = scanner.nextLine().trim();

        System.out.print("  Password : ");
        String password = scanner.nextLine().trim();

        currentPatient = patientService.login(username, password);
    }

    // ─── Appointment Operations ───────────────────────────────────────────────

    private static void bookAppointment() {
        System.out.println("\n  ─── BOOK AN APPOINTMENT ───");
        doctorService.displayAllDoctors();

        System.out.print("\n  Enter Doctor ID : ");
        int doctorId = readInt();

        Optional<Doctor> doctorOpt = doctorService.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            System.out.println("\n  ❌ Doctor not found.");
            return;
        }

        Doctor doctor = doctorOpt.get();

        if (doctor.getAvailableSlots().isEmpty()) {
            System.out.println("\n  ❌ Dr. " + doctor.getName() + " has no available slots.");
            return;
        }

        System.out.println("\n  Available slots for Dr. " + doctor.getName() + ":");
        List<String> slots = doctor.getAvailableSlots();
        for (int i = 0; i < slots.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + slots.get(i));
        }

        System.out.print("\n  Select slot number : ");
        int slotIndex = readInt() - 1;

        if (slotIndex < 0 || slotIndex >= slots.size()) {
            System.out.println("\n  ❌ Invalid slot selection.");
            return;
        }

        String selectedSlot = slots.get(slotIndex);

        System.out.print("  Reason for visit : ");
        String reason = scanner.nextLine().trim();

        appointmentService.bookAppointment(currentPatient, doctor, selectedSlot, reason);
    }

    private static void cancelAppointment() {
        System.out.println("\n  ─── CANCEL AN APPOINTMENT ───");
        appointmentService.displayPatientHistory(currentPatient);

        // Only show scheduled ones
        List<Appointment> active = appointmentService
                .getPatientAppointments(currentPatient.getId())
                .stream()
                .filter(a -> a.getStatus() == Appointment.Status.SCHEDULED)
                .toList();

        if (active.isEmpty()) {
            System.out.println("\n  No active appointments to cancel.");
            return;
        }

        System.out.print("\n  Enter Appointment ID to cancel : ");
        int appointmentId = readInt();

        appointmentService.cancelAppointment(appointmentId, currentPatient, doctorService);
    }

    // ─── Utility ──────────────────────────────────────────────────────────────

    /**
     * Safely reads an integer from the console, ignoring invalid input.
     */
    private static int readInt() {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("  ⚠️  Please enter a valid number: ");
            }
        }
    }

    private static void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║       🩺 DOCTOR APPOINTMENT BOOKING SYSTEM  🩺       ║");
        System.out.println("  ║                                                      ║");
        System.out.println("  ║   Built with Java OOP Principles                     ║");
        System.out.println("  ║   Author: Jabelo Pitso                               ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
    }
}