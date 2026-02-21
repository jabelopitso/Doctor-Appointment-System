package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Patient in the appointment system.
 * Stores patient personal info and their appointment history.
 */
public class Patient {

    private int id;
    private String name;
    private int age;
    private String contactNumber;
    private String username;
    private String password;  // simple plaintext for this educational project
    private List<Integer> appointmentIds; // tracks appointments by ID

    // ─── Constructor ───────────────────────────────────────────────────────────

    public Patient(int id, String name, int age, String contactNumber,
                   String username, String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contactNumber = contactNumber;
        this.username = username;
        this.password = password;
        this.appointmentIds = new ArrayList<>();
    }

    // ─── Appointment Tracking ─────────────────────────────────────────────────

    public void addAppointmentId(int appointmentId) {
        appointmentIds.add(appointmentId);
    }

    public void removeAppointmentId(int appointmentId) {
        appointmentIds.remove(Integer.valueOf(appointmentId));
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Integer> getAppointmentIds() { return appointmentIds; }

    // ─── Display ───────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format("Patient: %-20s | Age: %-3d | Contact: %s | ID: %d",
                name, age, contactNumber, id);
    }
}