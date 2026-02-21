import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorService {

    private final List<Doctor> doctors = new ArrayList<>();
    private int nextId = 1;

    public DoctorService() {
        Doctor d1 = new Doctor(nextId++, "Sarah Johnson", "Cardiologist", "011-555-0101");
        d1.addAvailableSlot("2024-06-10 09:00");
        d1.addAvailableSlot("2024-06-10 11:00");
        d1.addAvailableSlot("2024-06-11 10:00");

        Doctor d2 = new Doctor(nextId++, "Michael Dlamini", "General Practitioner", "011-555-0202");
        d2.addAvailableSlot("2024-06-10 08:00");
        d2.addAvailableSlot("2024-06-10 14:00");
        d2.addAvailableSlot("2024-06-12 09:00");

        Doctor d3 = new Doctor(nextId++, "Priya Naidoo", "Dermatologist", "011-555-0303");
        d3.addAvailableSlot("2024-06-11 11:00");
        d3.addAvailableSlot("2024-06-13 15:00");

        doctors.add(d1);
        doctors.add(d2);
        doctors.add(d3);
    }

    public Optional<Doctor> findById(int id) {
        return doctors.stream().filter(d -> d.getId() == id).findFirst();
    }

    public List<Doctor> getAllDoctors() { return new ArrayList<>(doctors); }

    public void displayAllDoctors() {
        System.out.println("\n  +======================================================+");
        System.out.println("  |               AVAILABLE DOCTORS                      |");
        System.out.println("  +======================================================+");
        for (Doctor d : doctors) {
            System.out.println("\n" + d);
            System.out.println("  Available Slots:");
            if (d.getAvailableSlots().isEmpty()) {
                System.out.println("    -> No slots available.");
            } else {
                for (String slot : d.getAvailableSlots())
                    System.out.println("    -> " + slot);
            }
            System.out.println("  ------------------------------------------------------");
        }
    }
}
