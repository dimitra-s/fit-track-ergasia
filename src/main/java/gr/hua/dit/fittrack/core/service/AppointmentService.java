package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;

public interface AppointmentService {

    CreateAppointmentResult createAppointment(final CreateAppointmentRequest createAppointmentRequest, final boolean notify);

    default CreateAppointmentResult createAppointment(final CreateAppointmentRequest createAppointmentRequest) {
        return this.createAppointment(createAppointmentRequest, true);
    }

    void deleteAppointment(Long id);
}