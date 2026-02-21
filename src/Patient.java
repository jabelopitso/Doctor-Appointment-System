import java.util.ArrayList;
import java.util.List;

public class Patient {

    private int id;
    private String name;
    private int age;
    private String contactNumber;
    private String username;
    private String password;
    private List<Integer> appointmentIds;

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

    public void addAppointmentId(int id)    { appointmentIds.add(id); }
    public void removeAppointmentId(int id) { appointmentIds.remove(Integer.valueOf(id)); }

    public int getId()                       { return id; }
    public String getName()                  { return name; }
    public void setName(String name)         { this.name = name; }
    public int getAge()                      { return age; }
    public void setAge(int age)              { this.age = age; }
    public String getContactNumber()         { return contactNumber; }
    public void setContactNumber(String c)   { this.contactNumber = c; }
    public String getUsername()              { return username; }
    public String getPassword()              { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<Integer> getAppointmentIds() { return appointmentIds; }

    @Override
    public String toString() {
        return String.format("  [ID: %d] %-20s | Age: %-3d | Contact: %s",
                id, name, age, contactNumber);
    }
}
