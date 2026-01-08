package gr.hua.dit.fittrack.core.service.impl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 4, max = 24) String password,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String fitnessGoal // Βεβαιώσου ότι αυτή η γραμμή υπάρχει!
) {}