package gr.hua.dit.fittrack.core.service.impl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TrainerRegistrationRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String specialization,
        @NotBlank String area
) {}