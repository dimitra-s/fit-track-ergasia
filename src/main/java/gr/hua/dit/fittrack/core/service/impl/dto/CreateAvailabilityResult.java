package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;

public record CreateAvailabilityResult(
        boolean created,
        String reason,
        TrainerAvailability slot
) {

    public static CreateAvailabilityResult success(final TrainerAvailability slot) {
        if (slot == null) throw new NullPointerException();
        return new CreateAvailabilityResult(true, null, slot);
    }

    public static CreateAvailabilityResult fail(final String reason) {
        if (reason == null) throw new NullPointerException();
        return new CreateAvailabilityResult(false, reason, null);
    }
}