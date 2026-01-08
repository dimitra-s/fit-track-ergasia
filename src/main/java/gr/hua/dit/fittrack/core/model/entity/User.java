package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    private String userFirstName;
    private String userLastName;
    private String emailAddress;

    // Πεδία για το Profile (Απαίτηση Εκφώνησης)
    private String fitnessGoal;

    // Προσθήκη πεδίων για άμεση εμφάνιση στο Profile
    private Double currentWeight;      // π.χ. 75.0
    private Double runningTime;        // π.χ. 25.5 (λεπτά)

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgressRecord> progressRecords = new ArrayList<>();

    // Constructors
    public User() {
    }

    public User(String username, String password, String userFirstName, String userLastName, String emailAddress, Role role) {
        this.username = username;
        this.password = password;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.emailAddress = emailAddress;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserFirstName() { return userFirstName; }
    public void setUserFirstName(String userFirstName) { this.userFirstName = userFirstName; }

    public String getUserLastName() { return userLastName; }
    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getFitnessGoal() { return fitnessGoal; }
    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }

    public Double getCurrentWeight() { return currentWeight; }
    public void setCurrentWeight(Double currentWeight) { this.currentWeight = currentWeight; }

    public Double getRunningTime() { return runningTime; }
    public void setRunningTime(Double runningTime) { this.runningTime = runningTime; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public List<ProgressRecord> getProgressRecords() { return progressRecords; }
    public void setProgressRecords(List<ProgressRecord> progressRecords) { this.progressRecords = progressRecords; }
}