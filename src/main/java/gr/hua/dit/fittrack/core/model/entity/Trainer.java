package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;

@Entity
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName; // Προσθήκη για την εκφώνηση
    private String lastName;// Προσθήκη για την εκφώνηση
    private String email;
    private String specialization;
    private String area;

    public Trainer() {}

    public Trainer(String firstName, String lastName, String specialization, String area) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.area = area;
    }

    // Getters & Setters
    public Long getId()
    { return id; }

    public void setId(Long id)
    { this.id = id; }

    public String getFirstName()
    { return firstName; }

    public void setFirstName(String firstName)
    { this.firstName = firstName; }

    public String getLastName()
    { return lastName; }

    public void setLastName(String lastName)
    { this.lastName = lastName; }

    public String getSpecialization()
    { return specialization; }

    public void setSpecialization(String specialization)
    { this.specialization = specialization; }

    public String getArea()
    { return area; }

    public void setArea(String area)
    { this.area = area; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}