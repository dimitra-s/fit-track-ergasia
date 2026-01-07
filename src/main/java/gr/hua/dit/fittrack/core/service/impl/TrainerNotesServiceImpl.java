package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.model.entity.TrainerNotes;
import gr.hua.dit.fittrack.core.repository.AppointmentRepository;
import gr.hua.dit.fittrack.core.repository.TrainerNotesRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.security.CurrentUserProvider;
import gr.hua.dit.fittrack.core.service.TrainerNotesService;
import gr.hua.dit.fittrack.core.service.impl.dto.AddTrainerNoteResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerNotesServiceImpl implements TrainerNotesService {

    private final TrainerNotesRepository notesRepository;
    private final AppointmentRepository appointmentRepository;
    private final TrainerRepository trainerRepository;
    private final CurrentUserProvider currentUserProvider;

    public TrainerNotesServiceImpl(
            TrainerNotesRepository notesRepository,
            AppointmentRepository appointmentRepository,
            TrainerRepository trainerRepository,
            CurrentUserProvider currentUserProvider
    ) {
        if (notesRepository == null) throw new NullPointerException();
        if (appointmentRepository == null) throw new NullPointerException();
        if (trainerRepository == null) throw new NullPointerException();
        if (currentUserProvider == null) throw new NullPointerException();

        this.notesRepository = notesRepository;
        this.appointmentRepository = appointmentRepository;
        this.trainerRepository = trainerRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    @Transactional
    public AddTrainerNoteResult addNotes(Long appointmentId, String text) {

        if (text == null || text.isBlank()) {
            return AddTrainerNoteResult.fail("Το κείμενο είναι υποχρεωτικό");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            return AddTrainerNoteResult.fail("Το ραντεβού δεν βρέθηκε");
        }

        long trainerId = currentUserProvider.requiredTrainerId();
        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);

        TrainerNotes note = new TrainerNotes();
        note.setAppointment(appointment);
        note.setTrainer(trainer);
        note.setText(text);

        notesRepository.save(note);

        return AddTrainerNoteResult.success(note);
    }

    @Override
    public List<TrainerNotes> listNotes(Long appointmentId) {
        return notesRepository.findByAppointmentId(appointmentId);
    }
}