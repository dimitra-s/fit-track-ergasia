package gr.hua.dit.fittrack.core.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class AvailabilityForm {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    public AvailabilityForm() {} // χρειάζεται για Spring

    public AvailabilityForm(LocalDateTime startTime, LocalDateTime endTime) {
       this.startTime = startTime;
       this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
       return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}