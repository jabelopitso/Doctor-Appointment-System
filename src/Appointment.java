package model;

/**
 * Represents a scheduled Appointment between a Patient and a Doctor.
 * Uses an enum for appointment status to enforce valid state transitions.
 */
public class Appointment {

    // Status enum — models real-world appointment states
    public enum Status {
        SCHEDULED,
        CANCELLED,
        COMPLETED
    }

    private int id;
    private int patientId;
    private int doctorId;
    private String patientName;
    private String doctorName;
    private String appointmentSlot; // Format: "YYYY-MM-DD HH:mm"
    private String reason;
    private Status status;

    // ─── Constructor ───────────────────────────────────────────────────────────

    public Appointment(int id, int patientId, String patientName,
                       int doctorId, String doctorName,
                       String appointmentSlot, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentSlot = appointmentSlot;
        this.reason = reason;
        this.status = Status.SCHEDULED; // default state
    }

    // ─── Status Transitions ───────────────────────────────────────────────────

    public void cancel() {
        if (this.status == Status.SCHEDULED) {
            this.status = Status.CANCELLED;
        } else {
            throw new IllegalStateException("Only SCHEDULED appointments can be cancelled.");
        }
    }

    public void complete() {
        if (this.status == Status.SCHEDULED) {
            this.status = Status.COMPLETED;
        } else {
            throw new IllegalStateException("Only SCHEDULED appointments can be completed.");
        }
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }

    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }

    public int getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }

    public String getAppointmentSlot() { return appointmentSlot; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    // ─── Display ───────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format(
                "┌─────────────────────────────────────────────┐%n" +
                        "│ Appointment ID : %-27d│%n" +
                        "│ Patient        : %-27s│%n" +
                        "│ Doctor         : Dr. %-24s│%n" +
                        "│ Slot           : %-27s│%n" +
                        "│ Reason         : %-27s│%n" +
                        "│ Status         : %-27s│%n" +
                        "└─────────────────────────────────────────────┘",
                id, patientName, doctorName, appointmentSlot, reason, status
        );
    }
}