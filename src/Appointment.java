public class Appointment {

    public enum Status { SCHEDULED, CANCELLED, COMPLETED }

    private int id;
    private int patientId;
    private String patientName;
    private int doctorId;
    private String doctorName;
    private String slot;
    private String reason;
    private Status status;

    public Appointment(int id, int patientId, String patientName,
                       int doctorId, String doctorName, String slot, String reason) {
        this.id          = id;
        this.patientId   = patientId;
        this.patientName = patientName;
        this.doctorId    = doctorId;
        this.doctorName  = doctorName;
        this.slot        = slot;
        this.reason      = reason;
        this.status      = Status.SCHEDULED;
    }

    public void cancel() {
        if (this.status != Status.SCHEDULED)
            throw new IllegalStateException("Only SCHEDULED appointments can be cancelled.");
        this.status = Status.CANCELLED;
    }

    public void complete() {
        if (this.status != Status.SCHEDULED)
            throw new IllegalStateException("Only SCHEDULED appointments can be completed.");
        this.status = Status.COMPLETED;
    }

    public int getId()             { return id; }
    public int getPatientId()      { return patientId; }
    public String getPatientName() { return patientName; }
    public int getDoctorId()       { return doctorId; }
    public String getDoctorName()  { return doctorName; }
    public String getSlot()        { return slot; }
    public String getReason()      { return reason; }
    public Status getStatus()      { return status; }
    public void setStatus(Status s){ this.status = s; }

    @Override
    public String toString() {
        return  "  +---------------------------------------------+\n"  +
                String.format("  | Appointment ID : %-27d|%n", id)      +
                String.format("  | Patient        : %-27s|%n", patientName) +
                String.format("  | Doctor         : Dr. %-24s|%n", doctorName) +
                String.format("  | Slot           : %-27s|%n", slot)    +
                String.format("  | Reason         : %-27s|%n", reason)  +
                String.format("  | Status         : %-27s|%n", status)  +
                "  +---------------------------------------------+";
    }
}
