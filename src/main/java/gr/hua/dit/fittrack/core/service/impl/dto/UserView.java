package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.Role;

public record UserView(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) {

    public String fullName() {
        return firstName + " " + lastName;
    }
}
