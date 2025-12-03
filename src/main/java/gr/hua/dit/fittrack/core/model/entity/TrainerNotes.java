package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;

@Entity
public class TrainerNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

   // @ManyToOne
    private Long appointmentId;//temporary for Apointment apointment

    @ManyToOne
    private Trainer trainer;

    @Column(name = "text")
    private String text;

    //Constructor
    public TrainerNotes() {
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
