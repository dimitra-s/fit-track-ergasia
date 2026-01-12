package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.*;
import gr.hua.dit.fittrack.core.repository.AppointmentRepository;
import gr.hua.dit.fittrack.core.repository.TrainerAvailabilityRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.WeatherService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final WeatherService weatherService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final TrainerRepository trainerRepository;
    private final TrainerAvailabilityRepository trainerAvailabilityRepository;

    public AppointmentServiceImpl(
            UserRepository userRepository,
            AppointmentRepository appointmentRepository,
            TrainerRepository trainerRepository,
            TrainerAvailabilityRepository trainerAvailabilityRepository,
            WeatherService weatherService
    ) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.trainerRepository = trainerRepository;
        this.trainerAvailabilityRepository = trainerAvailabilityRepository;
        this.weatherService = weatherService;
    }

    @Override
    @Transactional
    public CreateAppointmentResult createAppointment(CreateAppointmentRequest req, boolean notify) {
        if (req.dateTime().isBefore(LocalDateTime.now())) {
            return CreateAppointmentResult.fail("Δεν επιτρέπονται ραντεβού στο παρελθόν.");
        }

        // Προσοχή στο όνομα εδώ αν το Repository σου έχει Trainer_Id
        boolean hasAvailability = trainerAvailabilityRepository
                .existsByTrainer_IdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        req.trainerId(), req.dateTime(), req.dateTime()
                );

//        if (!hasAvailability) {
//            return CreateAppointmentResult.fail("Ο trainer δεν είναι διαθέσιμος αυτή την ώρα.");
//        }

        // Χρησιμοποιούμε τη νέα μέθοδο findByTrainerId
        boolean trainerBusy = appointmentRepository.existsByTrainerIdAndDateTime(req.trainerId(), req.dateTime());
        if (trainerBusy) {
            return CreateAppointmentResult.fail("Υπάρχει ήδη ραντεβού του trainer την ίδια ώρα.");
        }

        User user = userRepository.findById(req.userId()).orElse(null);
        Trainer trainer = trainerRepository.findById(req.trainerId()).orElse(null);

        if (user == null || trainer == null) {
            return CreateAppointmentResult.fail("Δεν βρέθηκε ο χρήστης ή ο trainer.");
        }

        Appointment appt = new Appointment();
        appt.setUser(user);
        appt.setTrainer(trainer);
        appt.setDateTime(req.dateTime());
        appt.setType(req.type());
        appt.setStatus(AppointmentStatus.PENDING);
        appt.setNotes(req.notes());

        if (req.type() == AppointmentType.OUTDOOR) {
            try {
                var weather = weatherService.getWeatherFor(req.dateTime(), "Athens");

                if (weather != null && weather.getSummary() != null) {
                    appt.setWeatherSummary(weather.getSummary());
                }
            } catch (Exception e) {
                appt.setWeatherSummary("Weather unavailable");
            }
        }

        Appointment saved = appointmentRepository.save(appt);
        return CreateAppointmentResult.success(saved);
    }

    @Override
    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Το ραντεβού δεν βρέθηκε."));
    }

    @Override
    public List<Appointment> getAppointmentsForTrainer(String email) {
        return appointmentRepository.findByTrainer_Email(email);
    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Το ραντεβού δεν βρέθηκε.");
        }
        appointmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String status) {
        Appointment app = findById(id);
        app.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
        appointmentRepository.save(app);
    }

    @Override
    @Transactional
    public void updateNotes(Long id, String notes) {
        Appointment app = findById(id);
        app.setNotes(notes);
        appointmentRepository.save(app);
    }

    @Override
    public List<LocalDateTime> getAvailableSlots(Long trainerId) {
        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        List<TrainerAvailability> availabilityList = trainerAvailabilityRepository.findByTrainer_Id(trainerId);

        for (TrainerAvailability av : availabilityList) {
            LocalDateTime tempSlot = av.getStartTime();
            while (tempSlot.isBefore(av.getEndTime())) {
                if (tempSlot.isAfter(now)) {
                    slots.add(tempSlot);
                }
                tempSlot = tempSlot.plusHours(1);
            }
        }

        // Χρησιμοποιούμε τη νέα μέθοδο findByTrainerId
        List<LocalDateTime> bookedTimes = appointmentRepository.findByTrainerId(trainerId)
                .stream()
                .map(Appointment::getDateTime)
                .toList();

        slots.removeAll(bookedTimes);
        return slots.stream().sorted().collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void setTrainerAvailability(Long trainerId, LocalDateTime start, LocalDateTime end) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        TrainerAvailability availability = new TrainerAvailability();
        availability.setTrainer(trainer);
        availability.setStartTime(start);
        availability.setEndTime(end);

        trainerAvailabilityRepository.save(availability);
    }

    @Override
    public List<Appointment> getAppointmentsByTrainer(String trainerEmail) {
        return appointmentRepository.findByTrainer_Email(trainerEmail);
    }

    @Override
    public List<Appointment> getAppointmentsByUser(String userEmail) {
        return appointmentRepository.findByUser_EmailAddress(userEmail);
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }


}