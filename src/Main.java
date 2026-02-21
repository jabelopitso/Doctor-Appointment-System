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

    public static void main(String[] args) {
        banner();
        mainMenu();
        System.out.println("\n  Goodbye! Thank you for using the system.");
        scanner.close();
    }

    private static void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n  ========================================");
            System.out.println("               MAIN MENU                 ");
            System.out.println("  ========================================");
            System.out.println("  1. Register as Patient");
            System.out.println("  2. Patient Login");
            System.out.println("  3. View All Doctors");
            System.out.println("  4. Exit");
            System.out.println("  ========================================");
            System.out.print("  Choice: ");
            switch (input()) {
                case "1" -> registerPatient();
                case "2" -> { loginPatient(); if (currentPatient != null) patientMenu(); }
                case "3" -> doctorService.displayAllDoctors();
                case "4" -> running = false;
                default  -> System.out.println("\n  Please enter 1-4.");
            }
        }
    }

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
                case "5" -> { System.out.println("\n  Logged out."); currentPatient = null; active = false; }
                default  -> System.out.println("\n  Please enter 1-5.");
            }
        }
    }

    private static void registerPatient() {
        System.out.println("\n  --- REGISTER -----------------------------------");
        System.out.print("  Full Name       : "); String name     = input();
        System.out.print("  Age             : "); int    age      = readInt();
        System.out.print("  Contact Number  : "); String contact  = input();
        System.out.print("  Username        : "); String username = input();
        System.out.print("  Password        : "); String password = input();
        patientService.register(name, age, contact, username, password);
    }

    private static void loginPatient() {
        System.out.println("\n  --- LOGIN --------------------------------------");
        System.out.print("  Username : "); String username = input();
        System.out.print("  Password : "); String password = input();
        currentPatient = patientService.login(username, password);
    }

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
