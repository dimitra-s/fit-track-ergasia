package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.AppointmentRepository;
import gr.hua.dit.fittrack.core.repository.TrainerAvailabilityRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final TrainerRepository trainerRepository;
    private final TrainerAvailabilityRepository trainerAvailabilityRepository;

    public AppointmentServiceImpl(
            UserRepository userRepository,
            AppointmentRepository appointmentRepository,
            TrainerRepository trainerRepository,
            TrainerAvailabilityRepository trainerAvailabilityRepository
    ) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.trainerRepository = trainerRepository;
        this.trainerAvailabilityRepository = trainerAvailabilityRepository;
    }

    @Override
    public CreateAppointmentResult createAppointment(CreateAppointmentRequest req, boolean notify) {

        // 1) όχι past appointments
        if (req.dateTime().isBefore(LocalDateTime.now())) {
            return CreateAppointmentResult.fail("Δεν επιτρέπονται ραντεβού στο παρελθόν.");
        }

        // 2) availability
        boolean hasAvailability =
                trainerAvailabilityRepository
                        .existsByTrainer_IdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                                req.trainerId(),
                                req.dateTime(),
                                req.dateTime()
                        );

        if (!hasAvailability) {
            return CreateAppointmentResult.fail("Ο trainer δεν είναι διαθέσιμος σε αυτό το slot.");
        }

        // 3) φόρτωσε User / Trainer
        User user = userRepository.findById(req.userId()).orElse(null);
        if (user == null) {
            return CreateAppointmentResult.fail("Δεν βρέθηκε ο χρήστης.");
        }

        Trainer trainer = trainerRepository.findById(req.trainerId()).orElse(null);
        if (trainer == null) {
            return CreateAppointmentResult.fail("Δεν βρέθηκε ο trainer.");
        }

        // 4) trainer busy?
        boolean trainerBusy =
                appointmentRepository.existsByTrainer_IdAndDateTime(req.trainerId(), req.dateTime());

        if (trainerBusy) {
            return CreateAppointmentResult.fail("Υπάρχει ήδη ραντεβού του trainer την ίδια ώρα.");
        }

        // 5) create + save
        Appointment appt = new Appointment();
        appt.setUser(user);
        appt.setTrainer(trainer);
        appt.setDateTime(req.dateTime());
        appt.setType(req.type());

        Appointment saved = appointmentRepository.save(appt);

        return CreateAppointmentResult.success(saved);
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Το ραντεβού δεν βρέθηκε.");
        }
        appointmentRepository.deleteById(id);
    }

}
