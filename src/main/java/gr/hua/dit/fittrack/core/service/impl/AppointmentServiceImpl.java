package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.AppointmentRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final Validator validator;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;

    public AppointmentServiceImpl(
            final Validator validator,
            final AppointmentRepository appointmentRepository,
            final UserRepository userRepository,
            final TrainerRepository trainerRepository
    ) {
        if (validator == null) throw new NullPointerException();
        if (appointmentRepository == null) throw new NullPointerException();
        if (userRepository == null) throw new NullPointerException();
        if (trainerRepository == null) throw new NullPointerException();

        this.validator = validator;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
    }

    @Transactional
    @Override
    public CreateAppointmentResult createAppointment(
            final CreateAppointmentRequest request,
            final boolean notify
    ) {
        if (request == null) throw new NullPointerException();

        // Validate request
        final Set<ConstraintViolation<CreateAppointmentRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<CreateAppointmentRequest> v : violations) {
                sb.append(v.getPropertyPath())
                        .append(": ")
                        .append(v.getMessage())
                        .append("\n");
            }
            return CreateAppointmentResult.fail(sb.toString());
        }

        // Fetch User & Trainer
        User user = userRepository.findById(request.userId()).orElse(null);
        if (user == null) return CreateAppointmentResult.fail("User not found");

        Trainer trainer = trainerRepository.findById(request.trainerId()).orElse(null);
        if (trainer == null) return CreateAppointmentResult.fail("Trainer not found");

        LocalDateTime dateTime = request.dateTime();

        // Business rules
        if (dateTime.isBefore(LocalDateTime.now())) {
            return CreateAppointmentResult.fail("No past appointments allowed");
        }

        // Check overlapping appointments
        List<Appointment> trainerAppointments = appointmentRepository.findByTrainerId(trainer.getId());
        for (Appointment a : trainerAppointments) {
            if (a.getDateTime().equals(dateTime)) {
                return CreateAppointmentResult.fail("Trainer already has appointment at this time");
            }
        }

        // Create Appointment entity
        Appointment appointment = new Appointment();
        appointment.setId(null);
        appointment.setUserId(user);
        appointment.setTrainerId(trainer);
        appointment.setDateTime(dateTime);
        appointment.setType(request.type());

        // Entity validation (programmer error)
        final Set<ConstraintViolation<Appointment>> entityViolations = validator.validate(appointment);
        if (!entityViolations.isEmpty()) {
            throw new RuntimeException("Invalid Appointment entity");
        }

        // Save to DB
        appointment = appointmentRepository.save(appointment);

        // Notify if requested
        if (notify) {
            // π.χ. email / SMS notification logic εδώ
        }

        return CreateAppointmentResult.success(appointment);
    }
}
