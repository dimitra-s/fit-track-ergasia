package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;

@Entity
public class ProgressRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name= "id")
    private Long id;

    @ManyToOne
    private User user;

    @Column
    private String date;

    @Column
    private Long weight;

    @Column
    private String notes;

    //constructor
    public ProgressRecord() {
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
