import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientService {

    private final List<Patient> patients = new ArrayList<>();
    private int nextId = 1;

    public Patient register(String name, int age, String contact,
                             String username, String password) {
        boolean taken = patients.stream()
                .anyMatch(p -> p.getUsername().equalsIgnoreCase(username));
        if (taken) {
            System.out.println("\n  ERROR: Username '" + username + "' is already taken.");
            return null;
        }
        Patient p = new Patient(nextId++, name, age, contact, username, password);
        patients.add(p);
        System.out.println("\n  SUCCESS: Registration complete! Welcome, " + name + ".");
        return p;
    }

    public Patient login(String username, String password) {
        Optional<Patient> found = patients.stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(username)
                          && p.getPassword().equals(password))
                .findFirst();
        if (found.isPresent()) {
            System.out.println("\n  SUCCESS: Welcome back, " + found.get().getName() + "!");
            return found.get();
        }
        System.out.println("\n  ERROR: Invalid username or password.");
        return null;
    }

    public Optional<Patient> findById(int id) {
        return patients.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Patient> getAllPatients() { return new ArrayList<>(patients); }
}
