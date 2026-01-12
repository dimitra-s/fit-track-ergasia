package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotNull Long userId,
        @NotNull Long trainerId,
        @NotNull
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime dateTime,
        @NotNull AppointmentType type,
        @Size(max = 255) String notes
) {
    // default constructor για Thymeleaf
//    public CreateAppointmentRequest() {
//        this(0L, 0L, LocalDateTime.now().plusDays(1), "", "");
//    }
}
