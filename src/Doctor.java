import java.util.ArrayList;
import java.util.List;

public class Doctor {

    private int id;
    private String name;
    private String specialization;
    private String contactNumber;
    private List<String> availableSlots;

    public Doctor(int id, String name, String specialization, String contactNumber) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
        this.availableSlots = new ArrayList<>();
    }

    public void addAvailableSlot(String slot) {
        if (!availableSlots.contains(slot)) availableSlots.add(slot);
    }

    public void removeSlot(String slot) { availableSlots.remove(slot); }

    public boolean isSlotAvailable(String slot) { return availableSlots.contains(slot); }

    public int getId()                      { return id; }
    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }
    public String getSpecialization()       { return specialization; }
    public void setSpecialization(String s) { this.specialization = s; }
    public String getContactNumber()        { return contactNumber; }
    public void setContactNumber(String c)  { this.contactNumber = c; }
    public List<String> getAvailableSlots() { return availableSlots; }

    @Override
    public String toString() {
        return String.format("  [ID: %d] Dr. %-20s | %-22s | Contact: %s",
                id, name, specialization, contactNumber);
    }
}
