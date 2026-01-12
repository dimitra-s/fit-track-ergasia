package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

//@Entity
//@Table(name = "appointments")
@Entity
@Table(
        name = "appointments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_appointments_trainer_datetime",
                        columnNames = {"trainer_id", "date_time"}
                )
        }
)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(nullable = false)
    private LocalDateTime dateTime;

//    private String type; // π.χ. "Yoga", "Crossfit", "Personal"

    @Column(length = 80)
    private String location;

    @Column(length = 255)
    private String weatherSummary;

//    private String status = "PENDING"; // Default τιμή για τα νέα ραντεβού

    @Column(length = 2000) // Μεγάλο μήκος για να χωράει το πλάνο
    private String notes;

    @Enumerated(EnumType.STRING)
    private AppointmentType type;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    // Constructors
    public Appointment() {
    }

    public Appointment(User user, Trainer trainer, LocalDateTime dateTime, AppointmentType type, String notes, String location, String weatherSummary) {
        this.user = user;
        this.trainer = trainer;
        this.dateTime = dateTime;
        this.type = type;
        this.notes = notes;
        this.location = location;
        this.weatherSummary = weatherSummary;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

//    public String getType() { return type; }
//    public void setType(String type) { this.type = type; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getWeatherSummary() { return weatherSummary; }
    public void setWeatherSummary(String weatherSummary) { this.weatherSummary = weatherSummary; }

    public AppointmentType getType() { return type; }
    public void setType(AppointmentType type) { this.type = type; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
}