package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.TrainerNotes;
import gr.hua.dit.fittrack.core.repository.TrainerNotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerNotesService {

    private final TrainerNotesRepository trainerNotesRepository;

    public TrainerNotesService(TrainerNotesRepository trainerNotesRepository) {

        //checking if the repository is null
        if(trainerNotesRepository == null) throw new NullPointerException();
        this.trainerNotesRepository = trainerNotesRepository;
    }

    public TrainerNotes addNotes(Long trainerid, Long appointmentid, String text){
        TrainerNotes trainerNotes = new TrainerNotes();
        //trainerNotes.setId(userid);
       // trainerNotes.setTrainer(trainerid);
       // trainerNotes.setAppointment(appointmentid);
        trainerNotes.setText(text);

        return trainerNotesRepository.save(trainerNotes);
    }

    public List<TrainerNotes> getNotesByAppointment(Long appointmentid){
        return trainerNotesRepository.findByAppointmentId(appointmentid);
    }
}
