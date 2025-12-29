package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.Appointment;

public record CreateAppointmentResult (
    boolean created,
    String reason,
    Appointment appointment
) {

        public static CreateAppointmentResult success(final Appointment appointment) {
            if (appointment == null) throw new NullPointerException();
            return new CreateAppointmentResult(true, null, appointment);
        }

        public static CreateAppointmentResult fail(final String reason) {
            if (reason == null) throw new NullPointerException();
            if (reason.isBlank()) throw new IllegalArgumentException();
            return new CreateAppointmentResult(false, reason, null);
        }
}
