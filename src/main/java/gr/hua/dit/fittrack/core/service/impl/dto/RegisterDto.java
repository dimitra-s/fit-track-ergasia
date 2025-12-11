package gr.hua.dit.fittrack.core.service.impl.dto;

public record RegisterDto(
        String username,
        String role,
        String email,
        String password
){}
